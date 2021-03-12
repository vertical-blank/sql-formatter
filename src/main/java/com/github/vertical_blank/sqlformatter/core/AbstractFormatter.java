package com.github.vertical_blank.sqlformatter.core;

import com.github.vertical_blank.sqlformatter.core.util.JSLikeList;
import com.github.vertical_blank.sqlformatter.core.util.Util;
import com.github.vertical_blank.sqlformatter.languages.DialectConfigurator;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractFormatter implements DialectConfigurator {
  private final FormatConfig cfg;
  private final Indentation indentation;
  private final InlineBlock inlineBlock;
  private final Params params;
  protected Token previousReservedToken;
  private JSLikeList<Token> tokens;
  private int index;

  /** @param cfg FormatConfig */
  public AbstractFormatter(FormatConfig cfg) {
    this.cfg = cfg;
    this.indentation = new Indentation(cfg.indent);
    this.inlineBlock = new InlineBlock(cfg.maxColumnLength);
    this.params = cfg.params;
    this.previousReservedToken = null;
    this.index = 0;
  }

  public Tokenizer tokenizer() {
    return new Tokenizer(this.dialectConfig());
  }

  /**
   * Reprocess and modify a token based on parsed context.
   *
   * @param token The token to modify
   * @return token
   */
  protected Token tokenOverride(Token token) {
    // subclasses can override this to modify tokens during formatting
    return token;
  }

  /**
   * Formats whitespaces in a SQL string to make it easier to read.
   *
   * @param query The SQL query string
   * @return formatted query
   */
  public String format(String query) {
    this.tokens = this.tokenizer().tokenize(query);
    String formattedQuery = this.getFormattedQueryFromTokens();

    return formattedQuery.trim();
  }

  private String getFormattedQueryFromTokens() {
    String formattedQuery = "";

    int _index = -1;
    for (Token token : this.tokens) {
      this.index = ++_index;

      token = this.tokenOverride(token);

      if (token.type == TokenTypes.LINE_COMMENT) {
        formattedQuery = this.formatLineComment(token, formattedQuery);
      } else if (token.type == TokenTypes.BLOCK_COMMENT) {
        formattedQuery = this.formatBlockComment(token, formattedQuery);
      } else if (token.type == TokenTypes.RESERVED_TOP_LEVEL) {
        formattedQuery = this.formatToplevelReservedWord(token, formattedQuery);
        this.previousReservedToken = token;
      } else if (token.type == TokenTypes.RESERVED_TOP_LEVEL_NO_INDENT) {
        formattedQuery = this.formatTopLevelReservedWordNoIndent(token, formattedQuery);
        this.previousReservedToken = token;
      } else if (token.type == TokenTypes.RESERVED_NEWLINE) {
        formattedQuery = this.formatNewlineReservedWord(token, formattedQuery);
        this.previousReservedToken = token;
      } else if (token.type == TokenTypes.RESERVED) {
        formattedQuery = this.formatWithSpaces(token, formattedQuery);
        this.previousReservedToken = token;
      } else if (token.type == TokenTypes.OPEN_PAREN) {
        formattedQuery = this.formatOpeningParentheses(token, formattedQuery);
      } else if (token.type == TokenTypes.CLOSE_PAREN) {
        formattedQuery = this.formatClosingParentheses(token, formattedQuery);
      } else if (token.type == TokenTypes.PLACEHOLDER) {
        formattedQuery = this.formatPlaceholder(token, formattedQuery);
      } else if (token.value.equals(",")) {
        formattedQuery = this.formatComma(token, formattedQuery);
      } else if (token.value.equals(":")) {
        formattedQuery = this.formatWithSpaceAfter(token, formattedQuery);
      } else if (token.value.equals(".")) {
        formattedQuery = this.formatWithoutSpaces(token, formattedQuery);
      } else if (token.value.equals(";")) {
        formattedQuery = this.formatQuerySeparator(token, formattedQuery);
      } else {
        formattedQuery = this.formatWithSpaces(token, formattedQuery);
      }
    }

    return formattedQuery;
  }

  private String formatLineComment(Token token, String query) {
    return this.addNewline(query + this.show(token));
  }

  private String formatBlockComment(Token token, String query) {
    return this.addNewline(this.addNewline(query) + this.indentComment(token.value));
  }

  private String indentComment(String comment) {
    return comment.replaceAll("\n", "\n" + this.indentation.getIndent());
  }

  private String formatTopLevelReservedWordNoIndent(Token token, String query) {
    this.indentation.decreaseTopLevel();
    query = this.addNewline(query) + this.equalizeWhitespace(this.show(token));
    return this.addNewline(query);
  }

  private String formatToplevelReservedWord(Token token, String query) {
    this.indentation.decreaseTopLevel();

    query = this.addNewline(query);

    this.indentation.increaseToplevel();

    query += this.equalizeWhitespace(this.show(token));
    return this.addNewline(query);
  }

  private String formatNewlineReservedWord(Token token, String query) {
    if (Token.isAnd(token) && Token.isBetween(this.tokenLookBehind(2))) {
      return this.formatWithSpaces(token, query);
    }
    return this.addNewline(query) + this.equalizeWhitespace(this.show(token)) + " ";
  }

  // Replace any sequence of whitespace characters with single space
  private String equalizeWhitespace(String string) {
    return string.replaceAll("\\s+", " ");
  }

  private static final Set<TokenTypes> preserveWhitespaceFor =
      EnumSet.of(
          TokenTypes.OPEN_PAREN,
          TokenTypes.LINE_COMMENT,
          TokenTypes.OPERATOR,
          TokenTypes.RESERVED_NEWLINE);

  // Opening parentheses increase the block indent level and start a new line
  private String formatOpeningParentheses(Token token, String query) {
    // Take out the preceding space unless there was whitespace there in the original query
    // or another opening parens or line comment
    if (token.whitespaceBefore.isEmpty()
        && !Optional.ofNullable(this.tokenLookBehind())
            .map(t -> preserveWhitespaceFor.contains(t.type))
            .orElse(false)) {
      query = Util.trimSpacesEnd(query);
    }
    query += this.show(token);

    this.inlineBlock.beginIfPossible(this.tokens, this.index);

    if (!this.inlineBlock.isActive()) {
      this.indentation.increaseBlockLevel();
      query = this.addNewline(query);
    }
    return query;
  }

  // Closing parentheses decrease the block indent level
  private String formatClosingParentheses(Token token, String query) {
    if (this.inlineBlock.isActive()) {
      this.inlineBlock.end();
      return this.formatWithSpaceAfter(token, query);
    } else {
      this.indentation.decreaseBlockLevel();
      return this.formatWithSpaces(token, this.addNewline(query));
    }
  }

  private String formatPlaceholder(Token token, String query) {
    return query + this.params.get(token) + " ";
  }

  // Commas start a new line (unless within inline parentheses or SQL "LIMIT" clause)
  private String formatComma(Token token, String query) {
    query = Util.trimSpacesEnd(query) + this.show(token) + " ";

    if (this.inlineBlock.isActive()) {
      return query;
    } else if (Token.isLimit(this.previousReservedToken)) {
      return query;
    } else {
      return this.addNewline(query);
    }
  }

  private String formatWithSpaceAfter(Token token, String query) {
    return Util.trimSpacesEnd(query) + this.show(token) + " ";
  }

  private String formatWithoutSpaces(Token token, String query) {
    return Util.trimSpacesEnd(query) + this.show(token);
  }

  private String formatWithSpaces(Token token, String query) {
    return query + this.show(token) + " ";
  }

  private String formatQuerySeparator(Token token, String query) {
    this.indentation.resetIndentation();
    return Util.trimSpacesEnd(query)
        + this.show(token)
        + Util.repeat("\n", Optional.ofNullable(this.cfg.linesBetweenQueries).orElse(1));
  }

  // Converts token to string (uppercasing it if needed)
  private String show(Token token) {
    if (this.cfg.uppercase
        && (token.type == TokenTypes.RESERVED
            || token.type == TokenTypes.RESERVED_TOP_LEVEL
            || token.type == TokenTypes.RESERVED_TOP_LEVEL_NO_INDENT
            || token.type == TokenTypes.RESERVED_NEWLINE
            || token.type == TokenTypes.OPEN_PAREN
            || token.type == TokenTypes.CLOSE_PAREN)) {
      return token.value.toUpperCase();
    } else {
      return token.value;
    }
  }

  private String addNewline(String query) {
    query = Util.trimSpacesEnd(query);
    if (!query.endsWith("\n")) {
      query += "\n";
    }
    return query + this.indentation.getIndent();
  }

  protected Token tokenLookBehind() {
    return this.tokenLookBehind(1);
  }

  protected Token tokenLookBehind(int n) {
    return this.tokens.get(this.index - n);
  }

  protected Token tokenLookAhead() {
    return this.tokenLookAhead(1);
  }

  protected Token tokenLookAhead(int n) {
    return this.tokens.get(this.index + n);
  }
}

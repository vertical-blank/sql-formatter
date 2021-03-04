package com.github.vertical_blank.sqlformatter.core;

import com.github.vertical_blank.sqlformatter.core.util.JSLikeList;
import com.github.vertical_blank.sqlformatter.core.util.RegexUtil;
import com.github.vertical_blank.sqlformatter.core.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
  // private final Pattern WHITESPACE_PATTERN;
  private final Pattern NUMBER_PATTERN;
  private final Pattern OPERATOR_PATTERN;

  private final Pattern BLOCK_COMMENT_PATTERN;
  private final Pattern LINE_COMMENT_PATTERN;

  private final Pattern RESERVED_TOP_LEVEL_PATTERN;
  private final Pattern RESERVED_TOP_LEVEL_NO_INDENT_PATTERN;
  private final Pattern RESERVED_NEWLINE_PATTERN;
  private final Pattern RESERVED_PLAIN_PATTERN;

  private final Pattern WORD_PATTERN;
  private final Pattern STRING_PATTERN;

  private final Pattern OPEN_PAREN_PATTERN;
  private final Pattern CLOSE_PAREN_PATTERN;

  private final Pattern INDEXED_PLACEHOLDER_PATTERN;
  private final Pattern IDENT_NAMED_PLACEHOLDER_PATTERN;
  private final Pattern STRING_NAMED_PLACEHOLDER_PATTERN;

  /**
   * @param cfg {String[]} cfg.reservedWords Reserved words in SQL {String[]}
   *     cfg.reservedTopLevelWords Words that are set to new line separately {String[]}
   *     cfg.reservedNewlineWords Words that are set to newline {String[]} cfg.stringTypes String
   *     types to enable: "", "", ``, [], N"" {String[]} cfg.openParens Opening parentheses to
   *     enable, like (, [ {String[]} cfg.closeParens Closing parentheses to enable, like ), ]
   *     {String[]} cfg.indexedPlaceholderTypes Prefixes for indexed placeholders, like ? {String[]}
   *     cfg.namedPlaceholderTypes Prefixes for named placeholders, like @ and : {String[]}
   *     cfg.lineCommentTypes Line comments to enable, like # and -- {String[]} cfg.specialWordChars
   *     Special chars that can be found inside of words, like @ and #
   */
  public Tokenizer(DialectConfig cfg) {
    // this.WHITESPACE_PATTERN = Pattern.compile("^(\\s+)");
    this.NUMBER_PATTERN =
        Pattern.compile(
            "^((-\\s*)?[0-9]+(\\.[0-9]+)?([eE]-?[0-9]+(\\.[0-9]+)?)?|0x[0-9a-fA-F]+|0b[01]+)\\b");
    this.OPERATOR_PATTERN =
        Pattern.compile(
            RegexUtil.createOperatorRegex(
                new JSLikeList<>(Arrays.asList("<>", "<=", ">=")).with(cfg.operators)));

    //        this.BLOCK_COMMENT_REGEX = /^(\/\*[^]*?(?:\*\/|$))/;
    this.BLOCK_COMMENT_PATTERN = Pattern.compile("^(/\\*(?s).*?(?:\\*/|$))");
    this.LINE_COMMENT_PATTERN =
        Pattern.compile(RegexUtil.createLineCommentRegex(new JSLikeList<>(cfg.lineCommentTypes)));

    this.RESERVED_TOP_LEVEL_PATTERN =
        Pattern.compile(
            RegexUtil.createReservedWordRegex(new JSLikeList<>(cfg.reservedTopLevelWords)));
    this.RESERVED_TOP_LEVEL_NO_INDENT_PATTERN =
        Pattern.compile(
            RegexUtil.createReservedWordRegex(new JSLikeList<>(cfg.reservedTopLevelWordsNoIndent)));
    this.RESERVED_NEWLINE_PATTERN =
        Pattern.compile(
            RegexUtil.createReservedWordRegex(new JSLikeList<>(cfg.reservedNewlineWords)));
    this.RESERVED_PLAIN_PATTERN =
        Pattern.compile(RegexUtil.createReservedWordRegex(new JSLikeList<>(cfg.reservedWords)));

    this.WORD_PATTERN =
        Pattern.compile(RegexUtil.createWordRegex(new JSLikeList<>(cfg.specialWordChars)));
    this.STRING_PATTERN =
        Pattern.compile(RegexUtil.createStringRegex(new JSLikeList<>(cfg.stringTypes)));

    this.OPEN_PAREN_PATTERN =
        Pattern.compile(RegexUtil.createParenRegex(new JSLikeList<>(cfg.openParens)));
    this.CLOSE_PAREN_PATTERN =
        Pattern.compile(RegexUtil.createParenRegex(new JSLikeList<>(cfg.closeParens)));

    this.INDEXED_PLACEHOLDER_PATTERN =
        RegexUtil.createPlaceholderRegexPattern(
            new JSLikeList<>(cfg.indexedPlaceholderTypes), "[0-9]*");
    this.IDENT_NAMED_PLACEHOLDER_PATTERN =
        RegexUtil.createPlaceholderRegexPattern(
            new JSLikeList<>(cfg.namedPlaceholderTypes), "[a-zA-Z0-9._$]+");
    this.STRING_NAMED_PLACEHOLDER_PATTERN =
        RegexUtil.createPlaceholderRegexPattern(
            new JSLikeList<>(cfg.namedPlaceholderTypes),
            RegexUtil.createStringPattern(new JSLikeList<>(cfg.stringTypes)));
  }

  /**
   * Takes a SQL string and breaks it into tokens. Each token is an object with type and value.
   *
   * @param input input The SQL string
   * @return {Object[]} tokens An array of tokens.
   */
  public JSLikeList<Token> tokenize(String input) {
    List<Token> tokens = new ArrayList<>();
    Token token = null;

    // Keep processing the string until it is empty
    while (!input.isEmpty()) {
      // grab any preceding whitespace
      String[] findBeforeWhitespace = findBeforeWhitespace(input);
      String whitespaceBefore = findBeforeWhitespace[0];
      input = findBeforeWhitespace[1];

      if (!input.isEmpty()) {
        // Get the next token and the token type
        token = this.getNextToken(input, token);
        // Advance the string
        input = input.substring(token.value.length());

        tokens.add(token.withWhitespaceBefore(whitespaceBefore));
      }
    }
    return new JSLikeList<>(tokens);
  }

  private String[] findBeforeWhitespace(String input) {
    int index = 0;
    char[] chars = input.toCharArray();
    int beforeLength = chars.length;
    while (index != beforeLength && Character.isWhitespace(chars[index])) {
      index++;
    }
    return new String[] {
      new String(chars, 0, index), new String(chars, index, beforeLength - index)
    };
  }

  // private String getWhitespace(String input) {
  //   String firstMatch = getFirstMatch(input, this.WHITESPACE_PATTERN);
  //   return firstMatch != null ? firstMatch : "";
  // }

  private Token getNextToken(String input, Token previousToken) {
    return Util.firstNotnull(
        () -> this.getCommentToken(input),
        () -> this.getStringToken(input),
        () -> this.getOpenParenToken(input),
        () -> this.getCloseParenToken(input),
        () -> this.getPlaceholderToken(input),
        () -> this.getNumberToken(input),
        () -> this.getReservedWordToken(input, previousToken),
        () -> this.getWordToken(input),
        () -> this.getOperatorToken(input));
  }

  private Token getCommentToken(String input) {
    return Util.firstNotnull(
        () -> this.getLineCommentToken(input), () -> this.getBlockCommentToken(input));
  }

  private Token getLineCommentToken(String input) {
    return this.getTokenOnFirstMatch(input, TokenTypes.LINE_COMMENT, this.LINE_COMMENT_PATTERN);
  }

  private Token getBlockCommentToken(String input) {
    return this.getTokenOnFirstMatch(input, TokenTypes.BLOCK_COMMENT, this.BLOCK_COMMENT_PATTERN);
  }

  private Token getStringToken(String input) {
    return this.getTokenOnFirstMatch(input, TokenTypes.STRING, this.STRING_PATTERN);
  }

  private Token getOpenParenToken(String input) {
    return this.getTokenOnFirstMatch(input, TokenTypes.OPEN_PAREN, this.OPEN_PAREN_PATTERN);
  }

  private Token getCloseParenToken(String input) {
    return this.getTokenOnFirstMatch(input, TokenTypes.CLOSE_PAREN, this.CLOSE_PAREN_PATTERN);
  }

  private Token getPlaceholderToken(String input) {
    return Util.firstNotnull(
        () -> this.getIdentNamedPlaceholderToken(input),
        () -> this.getStringNamedPlaceholderToken(input),
        () -> this.getIndexedPlaceholderToken(input));
  }

  private Token getIdentNamedPlaceholderToken(String input) {
    return this.getPlaceholderTokenWithKey(
        input, this.IDENT_NAMED_PLACEHOLDER_PATTERN, v -> v.substring(1));
  }

  private Token getStringNamedPlaceholderToken(String input) {
    return this.getPlaceholderTokenWithKey(
        input,
        this.STRING_NAMED_PLACEHOLDER_PATTERN,
        v ->
            this.getEscapedPlaceholderKey(
                v.substring(2, v.length() - 1), v.substring(v.length() - 1)));
  }

  private Token getIndexedPlaceholderToken(String input) {
    return this.getPlaceholderTokenWithKey(
        input, this.INDEXED_PLACEHOLDER_PATTERN, v -> v.substring(1));
  }

  private Token getPlaceholderTokenWithKey(
      String input, Pattern regex, java.util.function.Function<String, String> parseKey) {
    Token token = this.getTokenOnFirstMatch(input, TokenTypes.PLACEHOLDER, regex);
    if (token != null) {
      return token.withKey(parseKey.apply(token.value));
    }
    return token;
  }

  private String getEscapedPlaceholderKey(String key, String quoteChar) {
    return key.replaceAll(RegexUtil.escapeRegExp("\\") + quoteChar, quoteChar);
  }

  // Decimal, binary, or hex numbers
  private Token getNumberToken(String input) {
    return this.getTokenOnFirstMatch(input, TokenTypes.NUMBER, this.NUMBER_PATTERN);
  }

  // Punctuation and symbols
  private Token getOperatorToken(String input) {
    return this.getTokenOnFirstMatch(input, TokenTypes.OPERATOR, this.OPERATOR_PATTERN);
  }

  private Token getReservedWordToken(String input, Token previousToken) {
    // A reserved word cannot be preceded by a "."
    // this makes it so in "mytable.from", "from" is not considered a reserved word
    if (previousToken != null && previousToken.value != null && previousToken.value.equals(".")) {
      return null;
    }
    return Util.firstNotnull(
        () -> this.getToplevelReservedToken(input),
        () -> this.getNewlineReservedToken(input),
        () -> this.getTopLevelReservedTokenNoIndent(input),
        () -> this.getPlainReservedToken(input));
  }

  private Token getToplevelReservedToken(String input) {
    return this.getTokenOnFirstMatch(
        input, TokenTypes.RESERVED_TOP_LEVEL, this.RESERVED_TOP_LEVEL_PATTERN);
  }

  private Token getNewlineReservedToken(String input) {
    return this.getTokenOnFirstMatch(
        input, TokenTypes.RESERVED_NEWLINE, this.RESERVED_NEWLINE_PATTERN);
  }

  private Token getTopLevelReservedTokenNoIndent(String input) {
    return this.getTokenOnFirstMatch(
        input, TokenTypes.RESERVED_TOP_LEVEL_NO_INDENT, this.RESERVED_TOP_LEVEL_NO_INDENT_PATTERN);
  }

  private Token getPlainReservedToken(String input) {
    return this.getTokenOnFirstMatch(input, TokenTypes.RESERVED, this.RESERVED_PLAIN_PATTERN);
  }

  private Token getWordToken(String input) {
    return this.getTokenOnFirstMatch(input, TokenTypes.WORD, this.WORD_PATTERN);
  }

  private static String getFirstMatch(String input, Pattern regex) {
    if (regex == null) {
      return null;
    }

    Matcher matcher = regex.matcher(input);
    if (matcher.find()) {
      return matcher.group(1);
    } else {
      return null;
    }
  }

  private Token getTokenOnFirstMatch(String input, TokenTypes type, Pattern regex) {
    String firstMatch = getFirstMatch(input, regex);

    if (firstMatch != null) {
      return new Token(type, firstMatch);
    } else {
      return null;
    }
  }
}

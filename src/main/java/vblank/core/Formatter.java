package vblank.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static vblank.core.util.Util.nullToEmpty;
import static vblank.core.util.Util.trimEnd;

public class Formatter {
	private final Indentation indentation;
	private final InlineBlock inlineBlock;
	private final Params params;
	private final Tokenizer tokenizer;
	private Token previousReservedWord;
	private List<Token> tokens;
	private int index;

	/**
	 * @param cfg cfg.indent cfg.params
	 * @param tokenizer Tokenizer
	 */
	public Formatter(Config cfg, Tokenizer tokenizer) {
		this.indentation = new Indentation(cfg.indent);
		this.inlineBlock = new InlineBlock();
		this.params = new Params(cfg.params);
		this.tokenizer = tokenizer;
		this.previousReservedWord = null;
		this.tokens = Collections.emptyList();
		this.index = 0;
	}

	/**
	 * Formats whitespaces in a SQL string to make it easier to read.
	 *
	 * @param query The SQL query string
	 * @return formatted query
	 */
	public String format(String query) {
		this.tokens = this.tokenizer.tokenize(query);
		String formattedQuery = this.getFormattedQueryFromTokens();

		return formattedQuery.trim();
	}

	private String getFormattedQueryFromTokens() {
		String formattedQuery = "";

		int _index = -1;
		for (Token token : this.tokens) {
			this.index = ++_index;

			if (token.type == TokenTypes.WHITESPACE) {
				// ignore (we do our own whitespace formatting)
			} else if (token.type == TokenTypes.LINE_COMMENT) {
				formattedQuery = this.formatLineComment(token, formattedQuery);
			} else if (token.type == TokenTypes.BLOCK_COMMENT) {
				formattedQuery = this.formatBlockComment(token, formattedQuery);
			} else if (token.type == TokenTypes.RESERVED_TOPLEVEL) {
				formattedQuery = this.formatToplevelReservedWord(token, formattedQuery);
				this.previousReservedWord = token;
			} else if (token.type == TokenTypes.RESERVED_NEWLINE) {
				formattedQuery = this.formatNewlineReservedWord(token, formattedQuery);
				this.previousReservedWord = token;
			} else if (token.type == TokenTypes.RESERVED) {
				formattedQuery = this.formatWithSpaces(token, formattedQuery);
				this.previousReservedWord = token;
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
		return this.addNewline(query + token.value);
	}

	private String formatBlockComment(Token token, String query) {
		return this.addNewline(this.addNewline(query) + this.indentComment(token.value));
	}

	private String indentComment(String comment) {
		return comment.replaceAll("\n", "\n" + this.indentation.getIndent());
	}

	private String formatToplevelReservedWord(Token token, String query) {
		this.indentation.decreaseTopLevel();

		query = this.addNewline(query);

		this.indentation.increaseToplevel();

		query += this.equalizeWhitespace(token.value);
		return this.addNewline(query);
	}

	private String formatNewlineReservedWord(Token token, String query) {
		return this.addNewline(query) + this.equalizeWhitespace(token.value) + " ";
	}

	// Replace any sequence of whitespace characters with single space
	private String equalizeWhitespace(String string) {
		return string.replaceAll("\\s+", " ");
	}

	// Opening parentheses increase the block indent level and start a new line
	private String formatOpeningParentheses(Token token, String query) {
		// Take out the preceding space unless there was whitespace there in the original query
		// or another opening parens or line comment
		List<TokenTypes> preserveWhitespaceFor = Arrays.asList(
						TokenTypes.WHITESPACE,
						TokenTypes.OPEN_PAREN,
						TokenTypes.LINE_COMMENT
		);
		if (this.previousToken() == null || !nullToEmpty(preserveWhitespaceFor).contains(this.previousToken().type)) {
			query = trimEnd(query);
		}
		query += token.value;

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
		query = this.trimTrailingWhitespace(query) + token.value + " ";

		if (this.inlineBlock.isActive()) {
			return query;
		} else if (this.previousReservedWord.value.matches("(?i)^LIMIT$")){
			return query;
		} else{
			return this.addNewline(query);
		}
	}

	private String formatWithSpaceAfter(Token token, String query) {
		return this.trimTrailingWhitespace(query) + token.value + " ";
	}

	private String formatWithoutSpaces(Token token, String query) {
		return this.trimTrailingWhitespace(query) + token.value;
	}

	private String formatWithSpaces(Token token, String query) {
		return query + token.value + " ";
	}

	private String formatQuerySeparator(Token token, String query) {
		return this.trimTrailingWhitespace(query) + token.value + "\n";
	}

	private String addNewline(String query) {
		return trimEnd(query) + "\n" + this.indentation.getIndent();
	}

	private String trimTrailingWhitespace(String query) {
		Token token = this.previousNonWhitespaceToken();
		if (token != null && token.type == TokenTypes.LINE_COMMENT) {
			return trimEnd(query) + "\n";
		} else {
			return trimEnd(query);
		}
	}

	private Token previousNonWhitespaceToken() {
		int n = 1;
		while (this.previousToken(n) != null && this.previousToken(n).type == TokenTypes.WHITESPACE) {
			n++;
		}
		return this.previousToken(n);
	}

	private Token previousToken(int offset) {
		if (this.index - offset < 0){
			return null;
		} else {
			return this.tokens.get(this.index - offset);
		}
	}

	private Token previousToken() {
		return this.previousToken(1);
	}
}

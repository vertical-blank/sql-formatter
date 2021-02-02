package com.github.vertical_blank.sqlformatter.core;

import com.github.vertical_blank.sqlformatter.core.util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Formatter {
	private final FormatConfig cfg;
	private final Indentation indentation;
	private final InlineBlock inlineBlock;
	private final Params params;
	private final Tokenizer tokenizer;
	private Token previousReservedWord;
	private List<Token> tokens;
	private int index;

	/**
	 * @param cfg       cfg.indent cfg.params
	 * @param tokenizer Tokenizer
	 */
	public Formatter(FormatConfig cfg, Tokenizer tokenizer) {
		this.cfg = cfg;
		this.indentation = new Indentation(cfg.indent);
		this.inlineBlock = new InlineBlock(cfg.maxColumnLength);
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
		return this.addNewline(query + this.show(token));
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

		query += this.equalizeWhitespace(this.show(token));
		return this.addNewline(query);
	}

	private String formatNewlineReservedWord(Token token, String query) {
		return this.addNewline(query) + this.equalizeWhitespace(this.show(token)) + " ";
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
		if (!(this.previousToken().filter(t -> Util.nullToEmpty(preserveWhitespaceFor).contains(t.type)).isPresent())) {
			query = Util.trimEnd(query);
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
		query = this.trimTrailingWhitespace(query) + this.show(token) + " ";

		if (this.inlineBlock.isActive()) {
			return query;
		} else if (this.previousReservedWord != null && this.previousReservedWord.value.matches("(?i)^LIMIT$")) {
			return query;
		} else {
			return this.addNewline(query);
		}
	}

	private String formatWithSpaceAfter(Token token, String query) {
		return this.trimTrailingWhitespace(query) + this.show(token) + " ";
	}

	private String formatWithoutSpaces(Token token, String query) {
		return this.trimTrailingWhitespace(query) + this.show(token);
	}

	private String formatWithSpaces(Token token, String query) {
		return query + this.show(token) + " ";
	}

	private String formatQuerySeparator(Token token, String query) {
		return this.trimTrailingWhitespace(query) + this.show(token) + "\n";
	}

	// Converts token to string (uppercasing it if needed)
	private String show(Token token) {
		if (
			this.cfg.uppercase &&
			(token.type == TokenTypes.RESERVED ||
				// type == TokenTypes.RESERVED_TOP_LEVEL ||
				// type == TokenTypes.RESERVED_TOP_LEVEL_NO_INDENT ||
				token.type == TokenTypes.RESERVED_NEWLINE ||
				token.type == TokenTypes.OPEN_PAREN ||
				token.type == TokenTypes.CLOSE_PAREN)
		) {
			return token.value.toUpperCase();
		} else {
			return token.value;
		}
	}

	private String addNewline(String query) {
		return Util.trimEnd(query) + "\n" + this.indentation.getIndent();
	}

	private String trimTrailingWhitespace(String query) {
		Optional<Token> token = this.previousNonWhitespaceToken();
		if (token.filter(t -> t.type == TokenTypes.LINE_COMMENT).isPresent()) {
			return Util.trimEnd(query) + "\n";
		} else {
			return Util.trimEnd(query);
		}
	}

	private Optional<Token> previousNonWhitespaceToken() {
		int n = 1;
		while (this.previousToken(n).filter(t -> t.type == TokenTypes.WHITESPACE).isPresent()) {
			n++;
		}
		return this.previousToken(n);
	}

	private Optional<Token> previousToken(int offset) {
		if (this.index - offset < 0) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(this.tokens.get(this.index - offset));
		}
	}

	private Optional<Token> previousToken() {
		return this.previousToken(1);
	}
}

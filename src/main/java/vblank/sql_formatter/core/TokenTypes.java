package vblank.sql_formatter.core;

/**
 * Constants for token types
 * TODO should be enum.
 */
public enum TokenTypes {
	WHITESPACE,
	WORD,
	STRING,
	RESERVED,
	RESERVED_TOPLEVEL,
	RESERVED_NEWLINE,
	OPERATOR,
	OPEN_PAREN,
	CLOSE_PAREN,
	LINE_COMMENT,
	BLOCK_COMMENT,
	NUMBER,
	PLACEHOLDER,
}

package com.github.vertical.blank.sqlformatter.core;

/**
 * Constants for token types
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

package com.github.vertical_blank.sqlformatter.core;

/**
 * Constants for token types
 */
public enum TokenTypes {
	WORD,
	STRING,
	RESERVED,
	RESERVED_TOP_LEVEL,
	RESERVED_TOP_LEVEL_NO_INDENT,
	RESERVED_NEWLINE,
	OPERATOR,
	OPEN_PAREN,
	CLOSE_PAREN,
	LINE_COMMENT,
	BLOCK_COMMENT,
	NUMBER,
	PLACEHOLDER,
}

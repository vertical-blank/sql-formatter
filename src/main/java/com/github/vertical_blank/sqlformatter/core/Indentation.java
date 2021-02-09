package com.github.vertical_blank.sqlformatter.core;

import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Manages indentation levels.
 * <p>
 * There are two types of indentation levels:
 * <p>
 * - BLOCK_LEVEL : increased by open-parenthesis
 * - TOP_LEVEL : increased by RESERVED_TOPLEVEL words
 */
public class Indentation {

	enum IndentTypes {
		INDENT_TYPE_TOP_LEVEL,
		INDENT_TYPE_BLOCK_LEVEL
	}

	private final String indent;
	private final Stack<IndentTypes> indentTypes;


	/**
	 * @param indent Indent value, default is "  " (2 spaces)
	 */
	Indentation(String indent) {
		this.indent = indent;
		this.indentTypes = new Stack<>();
	}

	public Indentation() {
		this("  ");
	}

	/**
	 * Returns current indentation string.
	 *
	 * @return {String}
	 */
	public String getIndent() {
		return IntStream.range(0, this.indentTypes.size()).mapToObj(i -> this.indent).collect(Collectors.joining());
	}

	/**
	 * Increases indentation by one top-level indent.
	 */
	void increaseToplevel() {
		this.indentTypes.push(IndentTypes.INDENT_TYPE_TOP_LEVEL);
	}

	/**
	 * Increases indentation by one block-level indent.
	 */
	void increaseBlockLevel() {
		this.indentTypes.push(IndentTypes.INDENT_TYPE_BLOCK_LEVEL);
	}

	/**
	 * Decreases indentation by one top-level indent.
	 * Does nothing when the previous indent is not top-level.
	 */
	void decreaseTopLevel() {
		if (!this.indentTypes.isEmpty() && this.indentTypes.peek() == IndentTypes.INDENT_TYPE_TOP_LEVEL) {
			this.indentTypes.pop();
		}
	}

	/**
	 * Decreases indentation by one block-level indent.
	 * If there are top-level indents within the block-level indent,
	 * throws away these as well.
	 */
	void decreaseBlockLevel() {
		while (this.indentTypes.size() > 0) {
			IndentTypes type = this.indentTypes.pop();
			if (type != IndentTypes.INDENT_TYPE_TOP_LEVEL) {
				break;
			}
		}
	}

  void resetIndentation() {
    this.indentTypes.clear();
  }
}

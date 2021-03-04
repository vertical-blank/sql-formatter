package com.github.vertical_blank.sqlformatter.core;

import com.github.vertical_blank.sqlformatter.core.util.JSLikeList;

/**
 * Bookkeeper for inline blocks.
 *
 * <p>Inline blocks are parenthized expressions that are shorter than maxColumnLength. These blocks
 * are formatted on a single line, unlike longer parenthized expressions where open-parenthesis
 * causes newline and increase of indentation.
 */
class InlineBlock {

  private int level;
  private final int maxColumnLength;

  InlineBlock(int maxColumnLength) {
    this.maxColumnLength = maxColumnLength;
    this.level = 0;
  }

  /**
   * Begins inline block when lookahead through upcoming tokens determines that the block would be
   * smaller than INLINE_MAX_LENGTH.
   *
   * @param tokens Array of all tokens
   * @param index Current token position
   */
  void beginIfPossible(JSLikeList<Token> tokens, int index) {
    if (this.level == 0 && this.isInlineBlock(tokens, index)) {
      this.level = 1;
    } else if (this.level > 0) {
      this.level++;
    } else {
      this.level = 0;
    }
  }

  /** Finishes current inline block. There might be several nested ones. */
  public void end() {
    this.level--;
  }

  /**
   * True when inside an inline block
   *
   * @return {Boolean}
   */
  boolean isActive() {
    return this.level > 0;
  }

  // Check if this should be an inline parentheses block
  // Examples are "NOW()", "COUNT(*)", "int(10)", key(`somecolumn`), DECIMAL(7,2)
  private boolean isInlineBlock(JSLikeList<Token> tokens, int index) {
    int length = 0;
    int level = 0;

    for (int i = index; i < tokens.size(); i++) {
      Token token = tokens.get(i);
      length += token.value.length();

      // Overran max length
      if (length > maxColumnLength) {
        return false;
      }

      if (token.type == TokenTypes.OPEN_PAREN) {
        level++;
      } else if (token.type == TokenTypes.CLOSE_PAREN) {
        level--;
        if (level == 0) {
          return true;
        }
      }

      if (this.isForbiddenToken(token)) {
        return false;
      }
    }
    return false;
  }

  // Reserved words that cause newlines, comments and semicolons
  // are not allowed inside inline parentheses block
  private boolean isForbiddenToken(Token token) {
    return token.type == TokenTypes.RESERVED_TOP_LEVEL
        || token.type == TokenTypes.RESERVED_NEWLINE
        ||
        //                originally `TokenTypes.LINE_COMMENT` but this symbol is not defined
        //                token.type == TokenTypes.LINE_COMMENT ||
        token.type == TokenTypes.BLOCK_COMMENT
        || token.value.equals(";");
  }
}

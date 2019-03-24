package vblank.core;

/**
 * Constants for token types
 * TODO should be enum.
 */
public enum TokenTypes {
    WHITESPACE("whitespace"),
    WORD("word"),
    STRING("string"),
    RESERVED("reserved"),
    RESERVED_TOPLEVEL("reserved-toplevel"),
    RESERVED_NEWLINE("reserved-newline"),
    OPERATOR("operator"),
    OPEN_PAREN("open-paren"),
    CLOSE_PAREN("close-paren"),
    LINE_COMMENT("line-comment"),
    BLOCK_COMMENT("block-comment"),
    NUMBER("number"),
    PLACEHOLDER("placeholder"),
    ;

    public final String name;
    TokenTypes(String name) {
        this.name = name;
    }
}

package vblank.core;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.regex.Matcher;

import lombok.Builder;
import vblank.Util;

import static vblank.Util.escapeRegExp;


public class Tokenizer {
    private String WHITESPACE_REGEX;
    private String NUMBER_REGEX;
    private String OPERATOR_REGEX;

    private String BLOCK_COMMENT_REGEX;
    private String LINE_COMMENT_REGEX;

    private String RESERVED_TOPLEVEL_REGEX;
    private String RESERVED_NEWLINE_REGEX;
    private String RESERVED_PLAIN_REGEX;

    private String WORD_REGEX;
    private String STRING_REGEX;

    private String OPEN_PAREN_REGEX;
    private String CLOSE_PAREN_REGEX;

    private String INDEXED_PLACEHOLDER_REGEX;
    private String IDENT_NAMED_PLACEHOLDER_REGEX;
    private String STRING_NAMED_PLACEHOLDER_REGEX;


    /**
     * @param cfg
     *   {String[]} cfg.reservedWords Reserved words in SQL
     *   {String[]} cfg.reservedToplevelWords Words that are set to new line separately
     *   {String[]} cfg.reservedNewlineWords Words that are set to newline
     *  {String[]} cfg.stringTypes String types to enable: "", '', ``, [], N''
     *  {String[]} cfg.openParens Opening parentheses to enable, like (, [
     *  {String[]} cfg.closeParens Closing parentheses to enable, like ), ]
     *  {String[]} cfg.indexedPlaceholderTypes Prefixes for indexed placeholders, like ?
     *  {String[]} cfg.namedPlaceholderTypes Prefixes for named placeholders, like @ and :
     *  {String[]} cfg.lineCommentTypes Line comments to enable, like # and --
     *  {String[]} cfg.specialWordChars Special chars that can be found inside of words, like @ and #
     */
    public Tokenizer(Config cfg) {
        this.WHITESPACE_REGEX = "^(\\s+)";
        this.NUMBER_REGEX = "^((-\\s*)?[0-9]+(\\.[0-9]+)?|0x[0-9a-fA-F]+|0b[01]+)\\b";
        this.OPERATOR_REGEX = "^(!=|<>|==|<=|>=|!<|!>|\\|\\||::|->>|->|~~\\*|~~|!~~\\*|!~~|~\\*|!~\\*|!~|.)";

//        this.BLOCK_COMMENT_REGEX = /^(\/\*[^]*?(?:\*\/|$))/;
        this.BLOCK_COMMENT_REGEX = "^(/\\*(?s).*?(?:\\*/|$))";
        this.LINE_COMMENT_REGEX = this.createLineCommentRegex(cfg.lineCommentTypes);

        this.RESERVED_TOPLEVEL_REGEX = this.createReservedWordRegex(cfg.reservedToplevelWords);
        this.RESERVED_NEWLINE_REGEX = this.createReservedWordRegex(cfg.reservedNewlineWords);
        this.RESERVED_PLAIN_REGEX = this.createReservedWordRegex(cfg.reservedWords);

        this.WORD_REGEX = this.createWordRegex(cfg.specialWordChars);
        this.STRING_REGEX = this.createStringRegex(cfg.stringTypes);

        this.OPEN_PAREN_REGEX = this.createParenRegex(cfg.openParens);
        this.CLOSE_PAREN_REGEX = this.createParenRegex(cfg.closeParens);

        this.INDEXED_PLACEHOLDER_REGEX = createPlaceholderRegex(cfg.indexedPlaceholderTypes, "[0-9]*");
        this.IDENT_NAMED_PLACEHOLDER_REGEX = createPlaceholderRegex(cfg.namedPlaceholderTypes, "[a-zA-Z0-9._$]+");
        this.STRING_NAMED_PLACEHOLDER_REGEX = createPlaceholderRegex(
            cfg.namedPlaceholderTypes,
            this.createStringPattern(cfg.stringTypes)
        );
    }

    private String createLineCommentRegex(List<String> lineCommentTypes) {
        return String.format(
            "^((?:%s).*?(?:\n|$))",
            lineCommentTypes.stream().map(Util::escapeRegExp).collect(Collectors.joining("|"))
        );
    }

    private String createReservedWordRegex(List<String> reservedWords) {
        String reservedWordsPattern = String.join("|", reservedWords)
            .replaceAll(" ", "\\\\s+");
        return "(?i)" + "^(" + reservedWordsPattern + ")\\b";
    }

    private String createWordRegex(List<String> specialChars) {
        return "^([\\w" + String.join("", Util.nullToEmpty(specialChars)) + "]+)";
    }

    private String createStringRegex(List<String> stringTypes) {
        return "^(" + this.createStringPattern(stringTypes) + ")";
    }

    // This enables the following string patterns:
    // 1. backtick quoted string using `` to escape
    // 2. square bracket quoted string (SQL Server) using ]] to escape
    // 3. double quoted string using "" or \" to escape
    // 4. single quoted string using '' or \' to escape
    // 5. national character quoted string using N'' or N\' to escape
    private String createStringPattern(List<String> stringTypes) {
        Map<String, String> patterns = new HashMap<>();
        patterns.put("``", "((`[^`]*($|`))+)");
        patterns.put("[]", "((\\[[^\\]]*($|\\]))(\\][^\\]]*($|\\]))*)");
        patterns.put("\"\"", "((\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*(\"|$))+)");
        patterns.put("''", "(('[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)");
        patterns.put("N''", "((N'[^N'\\\\]*(?:\\\\.[^N'\\\\]*)*('|$))+)");

        return stringTypes.stream().map(patterns::get).collect(Collectors.joining("|"));
    }

    private String createParenRegex(List<String> parens) {
        return "(?i)" + "^(" + parens.stream().map(Tokenizer::escapeParen).collect(Collectors.joining("|")) + ")";
    }

    private static String escapeParen(String paren) {
        if (paren.length() == 1) {
            // A single punctuation character
//            return "\\${paren}";
            return escapeRegExp(paren);
        }
        else {
            // longer word
            return "\\b" + paren + "\\b";
        }
    }

    private static String createPlaceholderRegex(List<String> types, String pattern) {
        if (types.isEmpty()) {
            return null;
        }
        String typesRegex = types.stream().map(Util::escapeRegExp).collect(Collectors.joining("|"));

        return String.format("^((?:%s)(?:%s))", typesRegex, pattern);
    }

    /**
     * Takes a SQL string and breaks it into tokens.
     * Each token is an object with type and value.
     *
     * @param input input The SQL string
     * @return {Object[]} tokens An array of tokens.
     */
    List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Token token = null;

        // Keep processing the string until it is empty
        while (input.length() != 0) {
            // Get the next token and the token type
            token = this.getNextToken(input, token);
            // Advance the string
            input = input.substring(token.value.length());

            tokens.add(token);
        }
        return tokens;
    }

    private Token getNextToken(String input, Token previousToken) {
        return Util.firstNotnull(
            () -> this.getWhitespaceToken(input),
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

    private Token getWhitespaceToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.WHITESPACE,
            this.WHITESPACE_REGEX
        );
    }

    private Token getCommentToken(String input) {
        return Util.firstNotnull(
            () -> this.getLineCommentToken(input),
            () -> this.getBlockCommentToken(input));
    }

    private Token getLineCommentToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.LINE_COMMENT,
            this.LINE_COMMENT_REGEX
        );
    }

    private Token getBlockCommentToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.BLOCK_COMMENT,
            this.BLOCK_COMMENT_REGEX
        );
    }

    private Token getStringToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.STRING,
            this.STRING_REGEX
        );
    }

    private Token getOpenParenToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.OPEN_PAREN,
            this.OPEN_PAREN_REGEX
        );
    }

    private Token getCloseParenToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.CLOSE_PAREN,
            this.CLOSE_PAREN_REGEX
        );
    }

    private Token getPlaceholderToken(String input) {
        return Util.firstNotnull(
            () -> this.getIdentNamedPlaceholderToken(input),
            () -> this.getStringNamedPlaceholderToken(input),
            () -> this.getIndexedPlaceholderToken(input));
    }

    private Token getIdentNamedPlaceholderToken(String input) {
        return this.getPlaceholderTokenWithKey(
            input,
            this.IDENT_NAMED_PLACEHOLDER_REGEX,
            v -> v.substring(1)
        );
    }

    private Token getStringNamedPlaceholderToken(String input) {
        return this.getPlaceholderTokenWithKey(
            input,
            this.STRING_NAMED_PLACEHOLDER_REGEX,
            v -> this.getEscapedPlaceholderKey(v.substring(2, v.length() - 1), v.substring(v.length() - 1))
        );
    }

    private Token getIndexedPlaceholderToken(String input) {
        return this.getPlaceholderTokenWithKey(
            input,
            this.INDEXED_PLACEHOLDER_REGEX,
            v -> v.substring(1)
        );
    }

    private Token getPlaceholderTokenWithKey(String input, String regex, java.util.function.Function<String, String> parseKey) {
        Token token = this.getTokenOnFirstMatch(input, TokenTypes.PLACEHOLDER, regex);
        if (token != null) {
            token.key = parseKey.apply(token.value);
        }
        return token;
    }

    private String getEscapedPlaceholderKey(String key, String quoteChar) {
       return key.replaceAll("\\" + quoteChar, quoteChar);
    }

    // Decimal, binary, or hex numbers
    private Token getNumberToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.NUMBER,
            this.NUMBER_REGEX
        );
    }

    // Punctuation and symbols
    private Token getOperatorToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.OPERATOR,
            this.OPERATOR_REGEX
        );
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
            () -> this.getPlainReservedToken(input));
    }

    private Token getToplevelReservedToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.RESERVED_TOPLEVEL,
            this.RESERVED_TOPLEVEL_REGEX
        );
    }

    private Token getNewlineReservedToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.RESERVED_NEWLINE,
            this.RESERVED_NEWLINE_REGEX
        );
    }

    private Token getPlainReservedToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.RESERVED,
            this.RESERVED_PLAIN_REGEX
        );
    }

    private Token getWordToken(String input) {
        return this.getTokenOnFirstMatch(
            input,
            TokenTypes.WORD,
            this.WORD_REGEX
        );
    }

    private String getFirstMatch(String input, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()){
            return matcher.group();
        } else {
            return null;
        }
    }

    private Token getTokenOnFirstMatch(String input, TokenTypes type, String regex) {
        String matches = getFirstMatch(input, regex);

        if (matches != null) {
            return new Token(type, matches);
        } else {
            return null;
        }
    }

    public static class Token {
        TokenTypes type;
        String value;
        String regex;
        String key;

        Token(TokenTypes type, String value, String regex) {
            this.type = type;
            this.value = value;
            this.regex = regex;
        }
        Token(TokenTypes type, String value) {
            this(type, value, null);
        }

        @Override
        public String toString() {
            return "type: " + type + ", value: [" + value + "], regex: /" + regex + "/";
        }
    }

    @Builder(toBuilder = true)
    public static class Config {
        public final List<String> lineCommentTypes;
        public final List<String> reservedToplevelWords;
        public final List<String> reservedNewlineWords;
        public final List<String> reservedWords;
        public final List<String> specialWordChars;
        public final List<String> stringTypes;
        public final List<String> openParens;
        public final List<String> closeParens;
        public final List<String> indexedPlaceholderTypes;
        public final List<String> namedPlaceholderTypes;

        public final String language;
        public final String indent;
        public final LinkedHashMap<String, Object> params;
    }

}

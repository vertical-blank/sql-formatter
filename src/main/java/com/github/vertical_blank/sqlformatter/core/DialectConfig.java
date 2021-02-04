package com.github.vertical_blank.sqlformatter.core;

import java.util.List;

import com.github.vertical_blank.sqlformatter.enums.StringLiteral;

public class DialectConfig {
    public final List<String> lineCommentTypes;
    public final List<String> reservedTopLevelWords;
    public final List<String> reservedTopLevelWordsNoIndent;
    public final List<String> reservedNewlineWords;
    public final List<String> reservedWords;
    public final List<String> specialWordChars;
    public final List<StringLiteral> stringTypes;
    public final List<String> openParens;
    public final List<String> closeParens;
    public final List<String> indexedPlaceholderTypes;
    public final List<String> namedPlaceholderTypes;

    DialectConfig(List<String> lineCommentTypes, List<String> reservedTopLevelWords, List<String> reservedNewlineWords, List<String> reservedTopLevelWordsNoIndent, List<String> reservedWords, List<String> specialWordChars, List<StringLiteral> stringTypes, List<String> openParens, List<String> closeParens, List<String> indexedPlaceholderTypes, List<String> namedPlaceholderTypes) {
        this.lineCommentTypes = lineCommentTypes;
        this.reservedTopLevelWords = reservedTopLevelWords;
        this.reservedTopLevelWordsNoIndent = reservedTopLevelWordsNoIndent;
        this.reservedNewlineWords = reservedNewlineWords;
        this.reservedWords = reservedWords;
        this.specialWordChars = specialWordChars;
        this.stringTypes = stringTypes;
        this.openParens = openParens;
        this.closeParens = closeParens;
        this.indexedPlaceholderTypes = indexedPlaceholderTypes;
        this.namedPlaceholderTypes = namedPlaceholderTypes;
    }

    public static DialectConfigBuilder builder() {
        return new DialectConfigBuilder();
    }

    public static class DialectConfigBuilder {
        private List<String> lineCommentTypes;
        private List<String> reservedTopLevelWords;
        private List<String> reservedNewlineWords;
        private List<String> reservedTopLevelWordsNoIndent;
        private List<String> reservedWords;
        private List<String> specialWordChars;
        private List<StringLiteral> stringTypes;
        private List<String> openParens;
        private List<String> closeParens;
        private List<String> indexedPlaceholderTypes;
        private List<String> namedPlaceholderTypes;

        DialectConfigBuilder() {
        }

        public DialectConfigBuilder lineCommentTypes(List<String> lineCommentTypes) {
            this.lineCommentTypes = lineCommentTypes;
            return this;
        }

        public DialectConfigBuilder reservedTopLevelWords(List<String> reservedTopLevelWords) {
            this.reservedTopLevelWords = reservedTopLevelWords;
            return this;
        }

        public DialectConfigBuilder reservedNewlineWords(List<String> reservedNewlineWords) {
            this.reservedNewlineWords = reservedNewlineWords;
            return this;
        }

        public DialectConfigBuilder reservedTopLevelWordsNoIndent(List<String> reservedTopLevelWordsNoIndent) {
            this.reservedTopLevelWordsNoIndent = reservedTopLevelWordsNoIndent;
            return this;
        }

        public DialectConfigBuilder reservedWords(List<String> reservedWords) {
            this.reservedWords = reservedWords;
            return this;
        }

        public DialectConfigBuilder specialWordChars(List<String> specialWordChars) {
            this.specialWordChars = specialWordChars;
            return this;
        }

        public DialectConfigBuilder stringTypes(List<StringLiteral> stringTypes) {
            this.stringTypes = stringTypes;
            return this;
        }

        public DialectConfigBuilder openParens(List<String> openParens) {
            this.openParens = openParens;
            return this;
        }

        public DialectConfigBuilder closeParens(List<String> closeParens) {
            this.closeParens = closeParens;
            return this;
        }

        public DialectConfigBuilder indexedPlaceholderTypes(List<String> indexedPlaceholderTypes) {
            this.indexedPlaceholderTypes = indexedPlaceholderTypes;
            return this;
        }

        public DialectConfigBuilder namedPlaceholderTypes(List<String> namedPlaceholderTypes) {
            this.namedPlaceholderTypes = namedPlaceholderTypes;
            return this;
        }

        public DialectConfig build() {
            return new DialectConfig(lineCommentTypes, reservedTopLevelWords, reservedNewlineWords, reservedTopLevelWordsNoIndent, reservedWords, specialWordChars, stringTypes, openParens, closeParens, indexedPlaceholderTypes, namedPlaceholderTypes);
        }
    }
}

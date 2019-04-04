package com.github.vertical.blank.core;

import java.util.List;

public class DialectConfig {
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

    DialectConfig(List<String> lineCommentTypes, List<String> reservedToplevelWords, List<String> reservedNewlineWords, List<String> reservedWords, List<String> specialWordChars, List<String> stringTypes, List<String> openParens, List<String> closeParens, List<String> indexedPlaceholderTypes, List<String> namedPlaceholderTypes) {
        this.lineCommentTypes = lineCommentTypes;
        this.reservedToplevelWords = reservedToplevelWords;
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
        private List<String> reservedToplevelWords;
        private List<String> reservedNewlineWords;
        private List<String> reservedWords;
        private List<String> specialWordChars;
        private List<String> stringTypes;
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

        public DialectConfigBuilder reservedToplevelWords(List<String> reservedToplevelWords) {
            this.reservedToplevelWords = reservedToplevelWords;
            return this;
        }

        public DialectConfigBuilder reservedNewlineWords(List<String> reservedNewlineWords) {
            this.reservedNewlineWords = reservedNewlineWords;
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

        public DialectConfigBuilder stringTypes(List<String> stringTypes) {
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
            return new DialectConfig(lineCommentTypes, reservedToplevelWords, reservedNewlineWords, reservedWords, specialWordChars, stringTypes, openParens, closeParens, indexedPlaceholderTypes, namedPlaceholderTypes);
        }
    }
}

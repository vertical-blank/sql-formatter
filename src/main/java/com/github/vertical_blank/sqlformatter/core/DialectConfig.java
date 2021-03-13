package com.github.vertical_blank.sqlformatter.core;

import com.github.vertical_blank.sqlformatter.core.util.Util;
import java.util.Arrays;
import java.util.List;

public class DialectConfig {
  public final List<String> lineCommentTypes;
  public final List<String> reservedTopLevelWords;
  public final List<String> reservedTopLevelWordsNoIndent;
  public final List<String> reservedNewlineWords;
  public final List<String> reservedWords;
  public final List<String> specialWordChars;
  public final List<String> stringTypes;
  public final List<String> openParens;
  public final List<String> closeParens;
  public final List<String> indexedPlaceholderTypes;
  public final List<String> namedPlaceholderTypes;
  public final List<String> operators;

  DialectConfig(
      List<String> lineCommentTypes,
      List<String> reservedTopLevelWords,
      List<String> reservedNewlineWords,
      List<String> reservedTopLevelWordsNoIndent,
      List<String> reservedWords,
      List<String> specialWordChars,
      List<String> stringTypes,
      List<String> openParens,
      List<String> closeParens,
      List<String> indexedPlaceholderTypes,
      List<String> namedPlaceholderTypes,
      List<String> operators) {
    this.lineCommentTypes = Util.nullToEmpty(lineCommentTypes);
    this.reservedTopLevelWords = Util.nullToEmpty(reservedTopLevelWords);
    this.reservedTopLevelWordsNoIndent = Util.nullToEmpty(reservedTopLevelWordsNoIndent);
    this.reservedNewlineWords = Util.nullToEmpty(reservedNewlineWords);
    this.reservedWords = Util.nullToEmpty(reservedWords);
    this.specialWordChars = Util.nullToEmpty(specialWordChars);
    this.stringTypes = Util.nullToEmpty(stringTypes);
    this.openParens = Util.nullToEmpty(openParens);
    this.closeParens = Util.nullToEmpty(closeParens);
    this.indexedPlaceholderTypes = Util.nullToEmpty(indexedPlaceholderTypes);
    this.namedPlaceholderTypes = Util.nullToEmpty(namedPlaceholderTypes);
    this.operators = Util.nullToEmpty(operators);
  }

  public DialectConfig withLineCommentTypes(List<String> lineCommentTypes) {
    return this.toBuilder().lineCommentTypes(lineCommentTypes).build();
  }

  public DialectConfig plusLineCommentTypes(String... lineCommentTypes) {
    return this.plusLineCommentTypes(Arrays.asList(lineCommentTypes));
  }

  public DialectConfig plusLineCommentTypes(List<String> lineCommentTypes) {
    return this.toBuilder()
        .lineCommentTypes(Util.concat(this.lineCommentTypes, lineCommentTypes))
        .build();
  }

  public DialectConfig withReservedTopLevelWords(List<String> reservedTopLevelWords) {
    return this.toBuilder().reservedTopLevelWords(reservedTopLevelWords).build();
  }

  public DialectConfig plusReservedTopLevelWords(String... reservedTopLevelWords) {
    return this.plusReservedTopLevelWords(Arrays.asList(reservedTopLevelWords));
  }

  public DialectConfig plusReservedTopLevelWords(List<String> reservedTopLevelWords) {
    return this.toBuilder()
        .reservedTopLevelWords(Util.concat(this.reservedTopLevelWords, reservedTopLevelWords))
        .build();
  }

  public DialectConfig withReservedNewlineWords(List<String> reservedNewlineWords) {
    return this.toBuilder().reservedNewlineWords(reservedNewlineWords).build();
  }

  public DialectConfig plusReservedNewlineWords(String... reservedNewlineWords) {
    return this.plusReservedNewlineWords(Arrays.asList(reservedNewlineWords));
  }

  public DialectConfig plusReservedNewlineWords(List<String> reservedNewlineWords) {
    return this.toBuilder()
        .reservedNewlineWords(Util.concat(this.reservedNewlineWords, reservedNewlineWords))
        .build();
  }

  public DialectConfig withReservedTopLevelWordsNoIndent(
      List<String> reservedTopLevelWordsNoIndent) {
    return this.toBuilder().reservedTopLevelWordsNoIndent(reservedTopLevelWordsNoIndent).build();
  }

  public DialectConfig plusReservedTopLevelWordsNoIndent(String... reservedTopLevelWordsNoIndent) {
    return this.plusReservedTopLevelWordsNoIndent(Arrays.asList(reservedTopLevelWordsNoIndent));
  }

  public DialectConfig plusReservedTopLevelWordsNoIndent(
      List<String> reservedTopLevelWordsNoIndent) {
    return this.toBuilder()
        .reservedTopLevelWordsNoIndent(
            Util.concat(this.reservedTopLevelWordsNoIndent, reservedTopLevelWordsNoIndent))
        .build();
  }

  public DialectConfig withReservedWords(List<String> reservedWords) {
    return this.toBuilder().reservedWords(reservedWords).build();
  }

  public DialectConfig plusReservedWords(String... reservedWords) {
    return this.plusReservedWords(Arrays.asList(reservedWords));
  }

  public DialectConfig plusReservedWords(List<String> reservedWords) {
    return this.toBuilder().reservedWords(Util.concat(this.reservedWords, reservedWords)).build();
  }

  public DialectConfig withSpecialWordChars(List<String> specialWordChars) {
    return this.toBuilder().specialWordChars(specialWordChars).build();
  }

  public DialectConfig plusSpecialWordChars(String... specialWordChars) {
    return this.plusSpecialWordChars(Arrays.asList(specialWordChars));
  }

  public DialectConfig plusSpecialWordChars(List<String> specialWordChars) {
    return this.toBuilder()
        .specialWordChars(Util.concat(this.specialWordChars, specialWordChars))
        .build();
  }

  public DialectConfig withStringTypes(List<String> stringTypes) {
    return this.toBuilder().stringTypes(stringTypes).build();
  }

  public DialectConfig plusStringTypes(String... stringTypes) {
    return this.plusStringTypes(Arrays.asList(stringTypes));
  }

  public DialectConfig plusStringTypes(List<String> stringTypes) {
    return this.toBuilder().stringTypes(Util.concat(this.stringTypes, stringTypes)).build();
  }

  public DialectConfig withOpenParens(List<String> openParens) {
    return this.toBuilder().openParens(openParens).build();
  }

  public DialectConfig plusOpenParens(String... openParens) {
    return this.plusOpenParens(Arrays.asList(openParens));
  }

  public DialectConfig plusOpenParens(List<String> openParens) {
    return this.toBuilder().openParens(Util.concat(this.openParens, openParens)).build();
  }

  public DialectConfig withCloseParens(List<String> closeParens) {
    return this.toBuilder().closeParens(closeParens).build();
  }

  public DialectConfig plusCloseParens(String... closeParens) {
    return this.plusCloseParens(Arrays.asList(closeParens));
  }

  public DialectConfig plusCloseParens(List<String> closeParens) {
    return this.toBuilder().closeParens(Util.concat(this.closeParens, closeParens)).build();
  }

  public DialectConfig withIndexedPlaceholderTypes(List<String> indexedPlaceholderTypes) {
    return this.toBuilder().indexedPlaceholderTypes(indexedPlaceholderTypes).build();
  }

  public DialectConfig plusIndexedPlaceholderTypes(String... indexedPlaceholderTypes) {
    return this.plusIndexedPlaceholderTypes(Arrays.asList(indexedPlaceholderTypes));
  }

  public DialectConfig plusIndexedPlaceholderTypes(List<String> indexedPlaceholderTypes) {
    return this.toBuilder()
        .indexedPlaceholderTypes(Util.concat(this.indexedPlaceholderTypes, indexedPlaceholderTypes))
        .build();
  }

  public DialectConfig withNamedPlaceholderTypes(List<String> namedPlaceholderTypes) {
    return this.toBuilder().namedPlaceholderTypes(namedPlaceholderTypes).build();
  }

  public DialectConfig plusNamedPlaceholderTypes(String... namedPlaceholderTypes) {
    return this.plusNamedPlaceholderTypes(Arrays.asList(namedPlaceholderTypes));
  }

  public DialectConfig plusNamedPlaceholderTypes(List<String> namedPlaceholderTypes) {
    return this.toBuilder()
        .namedPlaceholderTypes(Util.concat(this.namedPlaceholderTypes, namedPlaceholderTypes))
        .build();
  }

  public DialectConfig withOperators(List<String> Operators) {
    return this.toBuilder().operators(Operators).build();
  }

  public DialectConfig plusOperators(String... operators) {
    return this.plusOperators(Arrays.asList(operators));
  }

  public DialectConfig plusOperators(List<String> operators) {
    return this.toBuilder().operators(Util.concat(this.operators, operators)).build();
  }

  public DialectConfigBuilder toBuilder() {
    return DialectConfig.builder()
        .reservedWords(this.reservedWords)
        .reservedTopLevelWords(this.reservedTopLevelWords)
        .reservedTopLevelWordsNoIndent(this.reservedTopLevelWordsNoIndent)
        .reservedNewlineWords(this.reservedNewlineWords)
        .stringTypes(this.stringTypes)
        .openParens(this.openParens)
        .closeParens(this.closeParens)
        .indexedPlaceholderTypes(this.indexedPlaceholderTypes)
        .namedPlaceholderTypes(this.namedPlaceholderTypes)
        .lineCommentTypes(this.lineCommentTypes)
        .specialWordChars(this.specialWordChars)
        .operators(this.operators);
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
    private List<String> stringTypes;
    private List<String> openParens;
    private List<String> closeParens;
    private List<String> indexedPlaceholderTypes;
    private List<String> namedPlaceholderTypes;
    private List<String> operators;

    DialectConfigBuilder() {}

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

    public DialectConfigBuilder reservedTopLevelWordsNoIndent(
        List<String> reservedTopLevelWordsNoIndent) {
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

    public DialectConfigBuilder operators(List<String> operators) {
      this.operators = operators;
      return this;
    }

    public DialectConfig build() {
      return new DialectConfig(
          lineCommentTypes,
          reservedTopLevelWords,
          reservedNewlineWords,
          reservedTopLevelWordsNoIndent,
          reservedWords,
          specialWordChars,
          stringTypes,
          openParens,
          closeParens,
          indexedPlaceholderTypes,
          namedPlaceholderTypes,
          operators);
    }
  }
}

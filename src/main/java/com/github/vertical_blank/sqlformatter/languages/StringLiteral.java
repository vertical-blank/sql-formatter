package com.github.vertical_blank.sqlformatter.languages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StringLiteral {
  public static final String BACK_QUOTE = "``";
  public static final String DOUBLE_QUOTE = "\"\"";
  public static final String U_DOUBLE_QUOTE = "U&\"\"";
  public static final String U_SINGLE_QUOTE = "U&''";
  public static final String E_SINGLE_QUOTE = "E''";
  public static final String N_SINGLE_QUOTE = "N''";
  public static final String Q_SINGLE_QUOTE = "Q''";
  public static final String SINGLE_QUOTE = "''";
  public static final String BRACE = "{}";
  public static final String DOLLAR = "$$";
  public static final String BRACKET = "[]";

  private static final Map<String, String> literals;

  static {
    literals =
        new HashMap<>(
            Arrays.stream(Preset.values())
                .collect(Collectors.toMap(Preset::getKey, Preset::getRegex)));
  }

  public static String get(String key) {
    return literals.get(key);
  }

  private enum Preset {
    /** `` */
    BACK_QUOTE(StringLiteral.BACK_QUOTE, "((`[^`]*($|`))+)"),
    /** "" */
    DOUBLE_QUOTE(
        StringLiteral.DOUBLE_QUOTE,
        "((\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*(\"|$))+)"), // "((^\"((?:\"\"|[^\"])*)\")+)"),
    /** [] */
    BRACKET(StringLiteral.BRACKET, "((\\[[^\\]]*($|\\]))(\\][^\\]]*($|\\]))*)"),
    /** {} */
    BRACE(StringLiteral.BRACE, "((\\{[^\\}]*($|\\}))+)"),
    /** '' */
    SINGLE_QUOTE(
        StringLiteral.SINGLE_QUOTE,
        "(('[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)"), // "((^'((?:''|[^'])*)')+)"),
    /** N'' */
    N_SINGLE_QUOTE(StringLiteral.N_SINGLE_QUOTE, "((N'[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)"),
    /** q'' */
    Q_SINGLE_QUOTE(
        StringLiteral.Q_SINGLE_QUOTE,
        "(?i)"
            + String.join(
                "|",
                "((n?q'\\{(?:(?!\\}'|\\\\).)*\\}')+)",
                "((n?q'\\[(?:(?!\\]'|\\\\).)*\\]')+)",
                "((n?q'<(?:(?!>'|\\\\).)*>')+)",
                "((n?q'\\((?:(?!\\)'|\\\\).)*\\)')+)")),
    // single_quote("((^'((?:''|[^'])*)')+)"),
    E_SINGLE_QUOTE(StringLiteral.E_SINGLE_QUOTE, "((E'[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)"),
    /** U&amp;'' */
    U_SINGLE_QUOTE(StringLiteral.U_SINGLE_QUOTE, "((U&'[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)"),
    /** U&amp;"" */
    U_DOUBLE_QUOTE(StringLiteral.U_DOUBLE_QUOTE, "((U&\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*(\"|$))+)"),
    /** $$ */
    DOLLAR(StringLiteral.DOLLAR, "((?<tag>\\$\\w*\\$)[\\s\\S]*?(?:\\k<tag>|$))"),
    ;

    public final String key;
    public final String regex;

    Preset(String key, String regex) {
      this.key = key;
      this.regex = regex;
    }

    public String getKey() {
      return this.key;
    }

    public String getRegex() {
      return this.regex;
    }
  }
}

package com.github.vertical_blank.sqlformatter.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum StringLiteral {
  /** `` */
  BackQuote("((`[^`]*($|`))+)"),
  /** "" */
  DoubleQuote("((\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*(\"|$))+)"),
  /** '' */
  SingleQuote("(('[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)"),
  /** N'' */
  NSingleQuote("((N'[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)"),
  /** [] */
  Bracket("((\\[[^\\]]*($|\\]))(\\][^\\]]*($|\\]))*)"),
  /** {} */
  Brace("((\\{[^\\}]*($|\\}))+)"),
  /** U&'' */
  USingleQuote("((U&'[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)"),
  /** U&"" */
  UDoubleQuote("((U&\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*(\"|$))+)"),
  /** $$ */
  Dollar("((?<tag>\\$\\w*\\$)[\\s\\S]*?(?:\\k<tag>|$))"),
  ;

  public static final Map<StringLiteral, String> regexMap =
      Arrays.stream(StringLiteral.values()).collect(Collectors.toMap(e -> e, e -> e.regex));

  public final String regex;

  StringLiteral(String regex) {
    this.regex = regex;
  }
}

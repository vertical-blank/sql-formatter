package com.github.vertical_blank.sqlformatter.core;

import java.util.List;
import java.util.Map;

/** Configurations for formatting. */
public class FormatConfig {

  public static final String DEFAULT_INDENT = "  ";
  public static final int DEFAULT_COLUMN_MAX_LENGTH = 50;

  public final String indent;
  public final int maxColumnLength;
  public final Params params;
  public final boolean uppercase;
  public final Integer linesBetweenQueries;

  FormatConfig(
      String indent,
      int maxColumnLength,
      Params params,
      boolean uppercase,
      Integer linesBetweenQueries) {
    this.indent = indent;
    this.maxColumnLength = maxColumnLength;
    this.params = params == null ? Params.EMPTY : params;
    this.uppercase = uppercase;
    this.linesBetweenQueries = linesBetweenQueries;
  }

  /**
   * Returns a new empty Builder.
   *
   * @return A new empty Builder
   */
  public static FormatConfigBuilder builder() {
    return new FormatConfigBuilder();
  }

  /** FormatConfigBuilder */
  public static class FormatConfigBuilder {
    private String indent = DEFAULT_INDENT;
    private int maxColumnLength = DEFAULT_COLUMN_MAX_LENGTH;
    private Params params;
    private boolean uppercase;
    private Integer linesBetweenQueries;

    FormatConfigBuilder() {}

    /**
     * @param indent Characters used for indentation, default is " " (2 spaces)
     * @return This
     */
    public FormatConfigBuilder indent(String indent) {
      this.indent = indent;
      return this;
    }

    /**
     * @param maxColumnLength Maximum length to treat inline block as one line
     * @return This
     */
    public FormatConfigBuilder maxColumnLength(int maxColumnLength) {
      this.maxColumnLength = maxColumnLength;
      return this;
    }

    /**
     * @param params Collection of params for placeholder replacement
     * @return This
     */
    public FormatConfigBuilder params(Params params) {
      this.params = params;
      return this;
    }

    /**
     * @param params Collection of params for placeholder replacement
     * @return This
     */
    public FormatConfigBuilder params(Map<String, ?> params) {
      return params(Params.of(params));
    }

    /**
     * @param params Collection of params for placeholder replacement
     * @return This
     */
    public FormatConfigBuilder params(List<?> params) {
      return params(Params.of(params));
    }

    /**
     * @param uppercase Converts keywords to uppercase
     * @return This
     */
    public FormatConfigBuilder uppercase(boolean uppercase) {
      this.uppercase = uppercase;
      return this;
    }

    /**
     * @param linesBetweenQueries How many line breaks between queries
     * @return This
     */
    public FormatConfigBuilder linesBetweenQueries(int linesBetweenQueries) {
      this.linesBetweenQueries = linesBetweenQueries;
      return this;
    }

    /**
     * Returns an instance of FormatConfig created from the fields set on this builder.
     *
     * @return FormatConfig
     */
    public FormatConfig build() {
      return new FormatConfig(
          this.indent, this.maxColumnLength, this.params, this.uppercase, this.linesBetweenQueries);
    }
  }
}

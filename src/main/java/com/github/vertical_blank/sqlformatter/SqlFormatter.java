package com.github.vertical_blank.sqlformatter;

import com.github.vertical_blank.sqlformatter.core.AbstractFormatter;
import com.github.vertical_blank.sqlformatter.core.DialectConfig;
import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.languages.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class SqlFormatter {
  /**
   * FormatConfig whitespaces in a query to make it easier to read.
   *
   * @param query sql
   * @param cfg FormatConfig
   * @return Formatted query
   */
  public static String format(String query, FormatConfig cfg) {
    return standard().format(query, cfg);
  }

  public static String format(String query, String indent, List<?> params) {
    return standard().format(query, indent, params);
  }

  public static String format(String query, List<?> params) {
    return standard().format(query, params);
  }

  public static String format(String query, String indent, Map<String, ?> params) {
    return standard().format(query, indent, params);
  }

  public static String format(String query, Map<String, ?> params) {
    return standard().format(query, params);
  }

  public static String format(String query, String indent) {
    return standard().format(query, indent);
  }

  public static String format(String query) {
    return standard().format(query);
  }

  public static Formatter extend(UnaryOperator<DialectConfig> operator) {
    return standard().extend(operator);
  }

  public static Formatter standard() {
    return of(Dialect.StandardSql);
  }

  public static Formatter of(String name) {
    return Dialect.nameOf(name)
        .map(Formatter::new)
        .orElseThrow(() -> new RuntimeException("Unsupported SQL dialect: " + name));
  }

  public static Formatter of(Dialect dialect) {
    return new Formatter(dialect);
  }

  public static class Formatter {

    private final Function<FormatConfig, AbstractFormatter> underlying;

    private Formatter(Function<FormatConfig, AbstractFormatter> underlying) {
      this.underlying = underlying;
    }

    private Formatter(Dialect dialect) {
      this(dialect.func);
    }

    public String format(String query, FormatConfig cfg) {
      return this.underlying.apply(cfg).format(query);
    }

    public String format(String query, String indent, List<?> params) {
      return format(query, FormatConfig.builder().indent(indent).params(params).build());
    }

    public String format(String query, List<?> params) {
      return format(query, FormatConfig.builder().params(params).build());
    }

    public String format(String query, String indent, Map<String, ?> params) {
      return format(query, FormatConfig.builder().indent(indent).params(params).build());
    }

    public String format(String query, Map<String, ?> params) {
      return format(query, FormatConfig.builder().params(params).build());
    }

    public String format(String query, String indent) {
      return format(query, FormatConfig.builder().indent(indent).build());
    }

    public String format(String query) {
      return format(query, FormatConfig.builder().build());
    }

    public Formatter extend(UnaryOperator<DialectConfig> operator) {
      return new Formatter(
          cfg ->
              new AbstractFormatter(cfg) {
                @Override
                public DialectConfig dialectConfig() {
                  return operator.apply(Formatter.this.underlying.apply(cfg).dialectConfig());
                }
              });
    }
  }
}

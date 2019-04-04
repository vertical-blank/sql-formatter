package com.github.vertical_blank.sqlformatter;

import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.languages.AbstractFormatter;
import com.github.vertical_blank.sqlformatter.languages.Db2Formatter;
import com.github.vertical_blank.sqlformatter.languages.N1qlFormatter;
import com.github.vertical_blank.sqlformatter.languages.PlSqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.StandardSqlFormatter;

import java.util.List;
import java.util.Map;

public class SqlFormatter {
	/**
	 * FormatConfig whitespaces in a query to make it easier to read.
	 *
	 * @param query sql
	 * @param cfg   cfg.indent Characters used for indentation, default is "  " (2 spaces)
	 *              cfg.params Collection of params for placeholder replacement
	 * @return {String}
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

	public static AbstractFormatter standard() {
		return of("sql");
	}

	public static AbstractFormatter of(String name) {
		switch (name) {
			case "db2":
				return new Db2Formatter();
			case "n1ql":
				return new N1qlFormatter();
			case "pl/sql":
				return new PlSqlFormatter();
			case "sql":
				return new StandardSqlFormatter();
			default:
				throw new RuntimeException("Unsupported SQL dialect: " + name);
		}
	}

}

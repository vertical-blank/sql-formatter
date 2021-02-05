package com.github.vertical_blank.sqlformatter;

import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.languages.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

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

	private static final Map<String, Supplier<AbstractFormatter>> formatters;

	static {
		Map<String, Supplier<AbstractFormatter>> map = new HashMap<>();
		map.put("db2", Db2Formatter::new);
		map.put("mariadb", MariaDbFormatter::new);
		map.put("mysql", MySqlFormatter::new);
		map.put("n1ql", N1qlFormatter::new);
		map.put("pl/sql", PlSqlFormatter::new);
		map.put("plsql", PlSqlFormatter::new);
		map.put("postgresql", PostgreSqlFormatter::new);
		map.put("redshift", RedshiftFormatter::new);
		map.put("spark", SparkSqlFormatter::new);
		map.put("sql", StandardSqlFormatter::new);
		map.put("tsql", TSqlFormatter::new);

		formatters = map;
	}

	public static AbstractFormatter of(String name) {
		return Optional.ofNullable(formatters.get(name))
			.map(Supplier::get)
			.orElseThrow(() -> new RuntimeException("Unsupported SQL dialect: " + name));
	}

}

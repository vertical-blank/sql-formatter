package com.github.vertical_blank.sqlformatter;

import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.core.Formatter;
import com.github.vertical_blank.sqlformatter.core.Params;
import com.github.vertical_blank.sqlformatter.languages.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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
		return standard().apply(cfg).format(query);
	}

	public static String format(String query, String indent, List<?> params) {
		return format(
			query, 
			FormatConfig.builder()
				.indent(indent)
				.params(Params.Holder.of(params))
				.build());
	}
	public static String format(String query, List<?> params) {
		return format(query,
			FormatConfig.builder()
				.params(Params.Holder.of(params))
				.build());
	}
	public static String format(String query, String indent, Map<String, ?> params) {
		return format(query, FormatConfig.builder()
			.indent(indent)
			.params(Params.Holder.of(params))
			.build());
	}
	public static String format(String query, Map<String, ?> params) {
		return format(query, FormatConfig.builder()
			.params(Params.Holder.of(params))
			.build());
	}
	public static String format(String query, String indent) {
		return format(query, FormatConfig.builder()
		.indent(indent)
		.build());
	}
	public static String format(String query) {
		return format(query, FormatConfig.builder().build());
	}

	public static AbstractFormatter standard() {
		return of("sql");
	}

	private static final Map<String, Function<FormatConfig, Formatter>> formatters;

	static {
		Map<String, Function<FormatConfig, Formatter>> map = new HashMap<>();
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
			.map(AbstractFormatter::new)
			.orElseThrow(() -> new RuntimeException("Unsupported SQL dialect: " + name));
	}

}

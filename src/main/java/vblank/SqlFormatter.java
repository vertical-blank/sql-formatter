package vblank;

import vblank.core.Config;
import vblank.languages.*;

import java.util.Optional;

public class SqlFormatter {
	/**
	 * Format whitespaces in a query to make it easier to read.
	 *
	 * @param query sql
	 * @param cfg   cfg.language Query language, default is Standard SQL
	 *              cfg.indent Characters used for indentation, default is "  " (2 spaces)
	 *              cfg.params Collection of params for placeholder replacement
	 * @return {String}
	 */
	public static String format(String query, Config cfg) {
		return getFormatter(cfg).format(query);
	}

	private static AbstractFormatter getFormatter(Config cfg) {
		String language = Optional.ofNullable(cfg.language).orElse("sql");
		switch (language) {
			case "db2":
				return new Db2Formatter(cfg);
			case "n1ql":
				return new N1qlFormatter(cfg);
			case "pl/sql":
				return new PlSqlFormatter(cfg);
			case "sql":
			case "":
				return new StandardSqlFormatter(cfg);
			default:
				throw new RuntimeException("Unsupported SQL dialect: " + cfg.language);
		}
	}

	public static String format(String query) {
		return format(query, Config.builder().language("sql").indent("  ").build());
	}

}


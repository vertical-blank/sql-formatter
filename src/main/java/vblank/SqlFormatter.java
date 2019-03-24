package vblank;

import vblank.core.Tokenizer;
import vblank.languages.StandardSqlFormatter;

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
	public static String format(String query, Tokenizer.Config cfg) {
		String language = Optional.ofNullable(cfg.language).orElse("sql");
		switch (language) {
//            case "db2":
//                return new Db2Formatter(cfg).format(query);
//            case "n1ql":
//                return new N1qlFormatter(cfg).format(query);
//            case "pl/sql":
//                return new PlSqlFormatter(cfg).format(query);
			case "sql":
			case "":
				return new StandardSqlFormatter(cfg).format(query);
			default:
				throw new RuntimeException("Unsupported SQL dialect: " + cfg.language);
		}
	}

	public static String format(String query) {
		return format(query, Tokenizer.Config.builder().language("sql").indent("  ").build());
	}

}


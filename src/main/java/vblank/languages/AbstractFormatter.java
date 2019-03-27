package vblank.languages;

import vblank.core.Config;
import vblank.core.Formatter;
import vblank.core.Tokenizer;

public abstract class AbstractFormatter {

	abstract Config dialectConfig();

	private Config cfg;

	/**
	 * @param cfg Different set of configurations
	 */
	AbstractFormatter(Config cfg) {
		this.cfg = cfg;
	}

	/**
	 * Formats DB2 query to make it easier to read
	 *
	 * @param query The DB2 query string
	 * @return formatted string
	 */
	public String format(String query) {
		Tokenizer tokenizer = new Tokenizer(this.dialectConfig());
		return new Formatter(this.cfg, tokenizer).format(query);
	}
}

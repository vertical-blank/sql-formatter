package com.github.vertical_blank.sqlformatter.core;

public class FormatConfig {

	public static final String DEFAULT_INDENT = "  ";
	public static final int DEFAULT_COLUMN_MAX_LENGTH = 50;

	public final String indent;
	public final int maxColumnLength;
	public final Params.Holder params;
	public final boolean uppercase;
	public final Integer linesBetweenQueries;

	FormatConfig(String indent, int maxColumnLength, Params.Holder params, boolean uppercase, int linesBetweenQueries) {
		this.indent = indent;
		this.maxColumnLength = maxColumnLength;
		this.params = params;
		this.uppercase = uppercase;
		this.linesBetweenQueries = linesBetweenQueries;
	}

	public static FormatConfigBuilder builder() {
		return new FormatConfigBuilder();
	}

	public static class FormatConfigBuilder {
		private String indent = DEFAULT_INDENT;
		private int maxColumnLength = DEFAULT_COLUMN_MAX_LENGTH;
		private Params.Holder params;
		private boolean uppercase;
		private Integer linesBetweenQueries;

		FormatConfigBuilder() {
		}

		/**
		 * @param indent Characters used for indentation, default is "  " (2 spaces)
		 */
		public FormatConfigBuilder indent(String indent) {
			this.indent = indent;
			return this;
		}


		public FormatConfigBuilder maxColumnLength(int maxColumnLength) {
			this.maxColumnLength = maxColumnLength;
			return this;
		}

		/**
		 * @param params Collection of params for placeholder replacement
		 */
		public FormatConfigBuilder params(Params.Holder params) {
			this.params = params;
			return this;
		}

		/**
		 * @param uppercase Converts keywords to uppercase
		 */
		public FormatConfigBuilder uppercase(boolean uppercase) {
			this.uppercase = uppercase;
			return this;
		}

		/**
		 * @param uppercase Converts keywords to uppercase
		 */
		public FormatConfigBuilder linesBetweenQueries(int linesBetweenQueries) {
			this.linesBetweenQueries = linesBetweenQueries;
			return this;
		}

		public FormatConfig build() {
			return new FormatConfig(this.indent, this.maxColumnLength, this.params, this.uppercase, this.linesBetweenQueries);
		}
	}
}

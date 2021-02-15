package com.github.vertical_blank.sqlformatter.core;

public class FormatConfig {

	public static final String DEFAULT_INDENT = "  ";
	public static final int DEFAULT_COLUMN_MAX_LENGTH = 50;

	public final String indent;
	public final int maxColumnLength;
	public final Params.Holder params;

	FormatConfig(String indent, int maxColumnLength, Params.Holder params) {
		this.indent = indent;
		this.maxColumnLength = maxColumnLength;
		this.params = params;
	}

	public static FormatConfigBuilder builder() {
		return new FormatConfigBuilder();
	}

	public static class FormatConfigBuilder {
		private String indent;
		private int maxColumnLength = DEFAULT_COLUMN_MAX_LENGTH;
		private Params.Holder params;

		FormatConfigBuilder() {
		}

		public FormatConfigBuilder indent(String indent) {
			this.indent = indent;
			return this;
		}

		public FormatConfigBuilder params(Params.Holder params) {
			this.params = params;
			return this;
		}

		public FormatConfig build() {
			return new FormatConfig(indent, maxColumnLength, params);
		}
	}
}

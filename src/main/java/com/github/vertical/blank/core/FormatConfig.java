package com.github.vertical.blank.core;

public class FormatConfig {

	public static final String DEFAULT_INDENT = "  ";

	public final String indent;
	public final Params.Holder params;

	FormatConfig(String indent, Params.Holder params) {
		this.indent = indent;
		this.params = params;
	}

	public static FormatConfigBuilder builder() {
		return new FormatConfigBuilder();
	}

	public static class FormatConfigBuilder {
		private String indent;
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
			return new FormatConfig(indent, params);
		}
	}
}

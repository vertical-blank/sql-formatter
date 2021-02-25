package com.github.vertical_blank.sqlformatter.languages;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.github.vertical_blank.sqlformatter.core.DialectConfig;
import com.github.vertical_blank.sqlformatter.core.FormatConfig;
import com.github.vertical_blank.sqlformatter.core.AbstractFormatter;
import com.github.vertical_blank.sqlformatter.core.Params;

public class Formatter implements Function<FormatConfig, AbstractFormatter> {

	private final Function<FormatConfig, AbstractFormatter> underlying;

	public Formatter(Function<FormatConfig, AbstractFormatter> underlying) {
		this.underlying = underlying;
	}

	@Override
	public AbstractFormatter apply(FormatConfig t) {
		return underlying.apply(t);
	}

	public Formatter extend(UnaryOperator<DialectConfig> operator) {
		return new Formatter(cfg -> 
			new AbstractFormatter(cfg) {
				@Override
				public DialectConfig dialectConfig() {
					return operator.apply(
						Formatter.this.underlying.apply(cfg).dialectConfig());
				}
			}
		);
	}

	public String format(String query, FormatConfig cfg) {
		return this.apply(cfg).format(query);
	}

	public String format(String query, String indent, List<?> params) {
		return format(
						query,
						FormatConfig.builder()
										.indent(indent)
										.params(Params.Holder.of(params))
										.build());
	}
	public String format(String query, List<?> params) {
		return format(
						query,
						FormatConfig.builder()
										.params(Params.Holder.of(params))
										.build());
	}

	public String format(String query, String indent, Map<String, ?> params) {
		return format(
						query,
						FormatConfig.builder()
										.indent(indent)
										.params(Params.Holder.of(params))
										.build());
	}
	public String format(String query, Map<String, ?> params) {
		return format(
						query,
						FormatConfig.builder()
										.params(Params.Holder.of(params))
										.build());
	}

	public String format(String query, String indent) {
		return format(
						query,
						FormatConfig.builder()
										.indent(indent)
										.build());
	}
	public String format(String query) {
		return format(
						query,
						FormatConfig.builder().build());
	}

}

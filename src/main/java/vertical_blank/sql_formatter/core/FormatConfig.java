package vertical_blank.sql_formatter.core;

import lombok.Builder;

@Builder(toBuilder = true)
public class FormatConfig {

	public static final String DEFAULT_INDENT = "  ";

	public final String indent;
	public final Params.Holder params;
}

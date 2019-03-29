package vertical_blank.sql_formatter.core;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public class DialectConfig {
	public final List<String> lineCommentTypes;
	public final List<String> reservedToplevelWords;
	public final List<String> reservedNewlineWords;
	public final List<String> reservedWords;
	public final List<String> specialWordChars;
	public final List<String> stringTypes;
	public final List<String> openParens;
	public final List<String> closeParens;
	public final List<String> indexedPlaceholderTypes;
	public final List<String> namedPlaceholderTypes;
}

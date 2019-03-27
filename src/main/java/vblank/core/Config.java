package vblank.core;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public class Config {
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

	public final String language;
	public final String indent;
	public final Params.Holder params;
}

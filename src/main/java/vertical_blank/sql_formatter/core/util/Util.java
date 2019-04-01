package vertical_blank.sql_formatter.core.util;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

	public static <T> List<T> nullToEmpty(List<T> ts) {
		if (ts == null) {
			return Collections.emptyList();
		} else {
			return ts;
		}
	}

	public static String trimEnd(String s) {
		return s.replaceAll("[ |\\n|\\r]*$", "");
	}

	public static String escapeRegExp(String s) {
		String regexp = Stream.of("^", "$", "\\", ".", "*", "+", "*", "?", "(", ")", "[", "]", "{", "}", "|")
						.map(spChr -> "(\\" + spChr + ")").collect(Collectors.joining("|"));

		return Pattern.compile(regexp).matcher(s).replaceAll("\\\\$0");
	}

	@SafeVarargs
	public static <R> R firstNotnull(Supplier<R>... sups) {
		for (Supplier<R> sup : sups) {
			R ret = sup.get();
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}

}


package com.github.vertical_blank.sqlformatter.core.util;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.vertical_blank.sqlformatter.enums.StringLiteral;

public class RegexUtil {
	
	private static final String ESCAPE_REGEX = Stream.of("^", "$", "\\", ".", "*", "+", "*", "?", "(", ")", "[", "]", "{", "}", "|")
		.map(spChr -> "(\\" + spChr + ")").collect(Collectors.joining("|"));
	public static final Pattern ESCAPE_REGEX_PATTERN = Pattern.compile(ESCAPE_REGEX);

	public static String escapeRegExp(String s) {
		return ESCAPE_REGEX_PATTERN.matcher(s).replaceAll("\\\\$0");
	}

	public static String createOperatorRegex(JSLikeList<String> multiLetterOperators) {
		return String.format("^(%s|.)", multiLetterOperators.map(RegexUtil::escapeRegExp).join("|"));
	}

	public static String createLineCommentRegex(JSLikeList<String> lineCommentTypes) {
		return String.format(
						"^((?:%s).*?)(?:\r\n|\r|\n|$)",
						lineCommentTypes.map(RegexUtil::escapeRegExp).join("|")
		);
	}

	public static String createReservedWordRegex(JSLikeList<String> reservedWords) {
		String reservedWordsPattern = reservedWords.join("|").replaceAll(" ", "\\\\s+");
		return "(?i)" + "^(" + reservedWordsPattern + ")\\b";
	}

	public static String createWordRegex(JSLikeList<String> specialChars) {
		return "^([\\w" + specialChars.join("") + "]+)";
	}

	public static String createStringRegex(JSLikeList<StringLiteral> stringTypes) {
		return "^(" + createStringPattern(stringTypes) + ")";
	}

	// This enables the following string patterns:
	// 1. backtick quoted string using `` to escape
	// 2. square bracket quoted string (SQL Server) using ]] to escape
	// 3. double quoted string using "" or \" to escape
	// 4. single quoted string using '' or \' to escape
	// 5. national character quoted string using N'' or N\' to escape
	public static String createStringPattern(JSLikeList<StringLiteral> stringTypes) {
		Map<StringLiteral, String> patterns = new EnumMap<>(StringLiteral.class);
		patterns.put(StringLiteral.BackQuote, "((`[^`]*($|`))+)");
		patterns.put(StringLiteral.Bracket, "((\\[[^\\]]*($|\\]))(\\][^\\]]*($|\\]))*)");
		patterns.put(StringLiteral.DoubleQuote, "((\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*(\"|$))+)");
		patterns.put(StringLiteral.SingleQuote, "(('[^'\\\\]*(?:\\\\.[^'\\\\]*)*('|$))+)");
		patterns.put(StringLiteral.NSingleQuote, "((N'[^N'\\\\]*(?:\\\\.[^N'\\\\]*)*('|$))+)");

		return stringTypes.map(patterns::get).join("|");
	}

	public static String createParenRegex(JSLikeList<String> parens) {
		return "(?i)" + "^(" + parens.map(RegexUtil::escapeParen).join("|") + ")";
	}

	public static String escapeParen(String paren) {
		if (paren.length() == 1) {
			// A single punctuation character
			return RegexUtil.escapeRegExp(paren);
		} else {
			// longer word
			return "\\b" + paren + "\\b";
		}
	}

	public static Pattern createPlaceholderRegexPattern(JSLikeList<String> types, String pattern) {
		if (types.isEmpty()) {
			return null;
		}
		String typesRegex = types.map(RegexUtil::escapeRegExp).join("|");

		return Pattern.compile(String.format("^((?:%s)(?:%s))", typesRegex, pattern));
	}

}

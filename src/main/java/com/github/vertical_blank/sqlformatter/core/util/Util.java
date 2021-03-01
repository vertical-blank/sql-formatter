package com.github.vertical_blank.sqlformatter.core.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
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

	public static String trimSpacesEnd(String s) {
		return s.replaceAll("[ \t]+$", "");
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

	@SafeVarargs
	public static <R> Optional<R> firstPresent(Supplier<Optional<R>>... sups) {
		for (Supplier<Optional<R>> sup : sups) {
			Optional<R> ret = sup.get();
			if (ret.isPresent()) {
				return ret;
			}
		}
		return Optional.empty();
	}

	public static String repeat(String s, int n) {
		return Stream.generate(() -> s).limit(n).collect(Collectors.joining());
	}
	
	public static <T> List<T> concat(List<T> l1, List<T> l2) {
		return Stream.of(l1, l2).flatMap(List::stream).collect(Collectors.toList());
	}

	public static List<String> sortByLengthDesc(List<String> strings) {
		return strings.stream().sorted(Comparator.comparingInt(String::length).reversed()).collect(Collectors.toList());
	}

}


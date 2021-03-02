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
    int endIndex = s.length();
    char[] chars = s.toCharArray();
    while (endIndex > 0 && (chars[endIndex - 1] == ' ' || chars[endIndex - 1] == '\t')) {
      endIndex--;
    }
    return new String(chars, 0, endIndex);
    // return s.replaceAll("[ \t]+$", "");
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

  public static JSLikeList<String> sortByLengthDesc(JSLikeList<String> strings) {
    return new JSLikeList<>(
        strings.stream()
            .sorted(Comparator.comparingInt(String::length).reversed())
            .collect(Collectors.toList()));
  }
}

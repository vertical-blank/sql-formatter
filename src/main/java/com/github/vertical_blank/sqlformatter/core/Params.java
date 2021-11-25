package com.github.vertical_blank.sqlformatter.core;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/** Handles placeholder replacement with given params. */
public interface Params {

  public static final Params EMPTY = new Empty();

  boolean isEmpty();

  Object get();

  Object getByName(String key);

  /** @param params query param */
  public static Params of(Map<String, ?> params) {
    return new NamedParams(params);
  }

  /** @param params query param */
  public static Params of(List<?> params) {
    return new IndexedParams(params);
  }

  /**
   * Returns param value that matches given placeholder with param key.
   *
   * @param token token.key Placeholder key token.value Placeholder value
   * @return param or token.value when params are missing
   */
  default Object get(Token token) {
    if (this.isEmpty()) {
      return token.value;
    }
    if (!(token.key == null || token.key.isEmpty())) {
      return this.getByName(token.key);
    } else {
      return this.get();
    }
  }

  public static class NamedParams implements Params {
    private final Map<String, ?> params;

    NamedParams(Map<String, ?> params) {
      this.params = params;
    }

    public boolean isEmpty() {
      return this.params.isEmpty();
    }

    @Override
    public Object get() {
      return null;
    }

    @Override
    public Object getByName(String key) {
      return this.params.get(key);
    }

    @Override
    public String toString() {
      return this.params.toString();
    }
  }

  public static class IndexedParams implements Params {
    private final Queue<?> params;

    IndexedParams(List<?> params) {
      this.params = new ArrayDeque<>(params);
    }

    public boolean isEmpty() {
      return this.params.isEmpty();
    }

    @Override
    public Object get() {
      return this.params.poll();
    }

    @Override
    public Object getByName(String key) {
      return null;
    }

    @Override
    public String toString() {
      return this.params.toString();
    }
  }

  public static class Empty implements Params {
    Empty() {}

    public boolean isEmpty() {
      return true;
    }

    @Override
    public Object get() {
      return null;
    }

    @Override
    public Object getByName(String key) {
      return null;
    }

    @Override
    public String toString() {
      return "[]";
    }
  }
}

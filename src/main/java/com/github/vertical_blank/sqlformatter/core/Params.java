package com.github.vertical_blank.sqlformatter.core;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/** Handles placeholder replacement with given params. */
public class Params {

  public static Params EMPTY = new Params();

  private Holder params;

  /** @param params query param */
  public Params(Map<String, ?> params) {
    this.params = new NamedParamHolder(params);
  }

  /** @param params query param */
  public Params(List<?> params) {
    this.params = new IndexedParamHolder(params);
  }

  private Params() {}

  /**
   * Returns param value that matches given placeholder with param key.
   *
   * @param token token.key Placeholder key token.value Placeholder value
   * @return param or token.value when params are missing
   */
  Object get(Token token) {
    if (this.params == null || this.params.isEmpty()) {
      return token.value;
    }
    if (!(token.key == null || token.key.isEmpty())) {
      return this.params.getByName(token.key);
    } else {
      return params.get();
    }
  }

  private interface Holder {
    boolean isEmpty();

    Object get();

    Object getByName(String key);

    /**
     * @param params query param
     * @return Holder
     */
    static Holder of(Map<String, ?> params) {
      return new NamedParamHolder(params);
    }

    /**
     * @param params query param
     * @return Holder
     */
    static Holder of(List<?> params) {
      return new IndexedParamHolder(params);
    }
  }

  private static class NamedParamHolder implements Holder {
    private final Map<String, ?> params;

    NamedParamHolder(Map<String, ?> params) {
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

  private static class IndexedParamHolder implements Holder {
    private final Queue<?> params;

    IndexedParamHolder(List<?> params) {
      this.params = new PriorityQueue<>(params);
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

  @Override
  public String toString() {
    return String.valueOf(this.params);
  }
}

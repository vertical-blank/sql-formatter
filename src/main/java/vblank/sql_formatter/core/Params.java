package vblank.sql_formatter.core;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Handles placeholder replacement with given params.
 */
public class Params {

	public interface Holder {
		boolean isEmpty();

		Object get();

		Object getByName(String key);

		/**
		 * @param params query param
		 */
		static Holder of(Map<String, ?> params) {
			return new NamedParamHolder(params);
		}

		/**
		 * @param params query param
		 */
		static Holder of(List<?> params) {
			return new IndexedParamHolder(params);
		}
	}

	public static class NamedParamHolder implements Holder {
		private Map<String, ?> params;

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
	}

	public static class IndexedParamHolder implements Holder {
		private Queue<?> params;

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
	}


	private Holder params;
//    private int index;

	/**
	 * @param params query param
	 */
	public Params(Map<String, ?> params) {
		this.params = new NamedParamHolder(params);
	}

	/**
	 * @param params query param
	 */
	public Params(List<?> params) {
		this.params = new IndexedParamHolder(params);
//        this.index = 0;
	}

	/**
	 * @param holder query param holder
	 */
	public Params(Holder holder) {
		this.params = holder;
	}

	/**
	 * Returns param value that matches given placeholder with param key.
	 *
	 * @param token token.key Placeholder key
	 *              token.value Placeholder value
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
}

package vblank.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Handles placeholder replacement with given params.
 */
class Params {

    private LinkedHashMap<String, Object> paramMap;
    private List<Object> params;
    private int index;

    /**
     * @param params query param
     */
    Params(LinkedHashMap<String, Object> params) {
       this.paramMap = params;
       this.index = 0;
    }
    /**
     * @param params query param
     */
    Params(List<Object> params) {
        this.params = params;
        this.index = 0;
    }

    /**
     * Returns param value that matches given placeholder with param key.
     * @param token
     *   token.key Placeholder key
     *   token.value Placeholder value
     * @return param or token.value when params are missing
     */
    Object get(Token token) {
        if (this.params == null || this.params.isEmpty()) {
            return token.value;
        }
        if (!(token.key == null || token.key.isEmpty())) {
            return this.paramMap.get(token.key);
        }
        return params.get(this.index++);
    }
}

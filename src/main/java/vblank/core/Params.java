package vblank.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles placeholder replacement with given params.
 */
public class Params {

    LinkedHashMap<String, Object> params;
    private int index;

    /**
     * @param params
     */
    public Params(LinkedHashMap<String, Object> params) {
       this.params = params;
       this.index = 0;
    }
    public Params() {
        this(new LinkedHashMap<>());
    }

    /**
     * Returns param value that matches given placeholder with param key.
     * @param token
     *   token.key Placeholder key
     *   token.value Placeholder value
     * @return param or token.value when params are missing
     */
    public Object get(Tokenizer.Token token) {
        if (this.params.isEmpty()) {
            return token.value;
        }
        if (token.key != null) {
            return this.params.get(token.key);
        }
        return new ArrayList<>(this.params.values()).get(this.index++);
    }
}

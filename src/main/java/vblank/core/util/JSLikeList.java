package vblank.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JSLikeList <T> {

    private List<T> tList;

    public JSLikeList(List<T> tList) {
        this.tList = new ArrayList<>(tList);
    }

    public List<T> toList() {
        return this.tList;
    }

    public <R> JSLikeList map(Function<T, R> mapper) {
        return new JSLikeList<>(this.tList.stream().map(mapper).collect(Collectors.toList()));
    }

    public String join(CharSequence delimiter) {
        return String.join(delimiter);
    }

    public String join() {
        return join("");
    }

    public boolean isEmpty() {
        return this.tList == null || this.tList.isEmpty();
    }
}

package com.github.vertical_blank.sqlformatter.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JSLikeList<T> implements Iterable<T> {

	private List<T> tList;

	public JSLikeList(List<T> tList) {
		this.tList = tList == null ? Collections.emptyList() : new ArrayList<>(tList);
	}

	public List<T> toList() {
		return this.tList;
	}

	public <R> JSLikeList<R> map(Function<T, R> mapper) {
		return new JSLikeList<>(this.tList.stream().map(mapper).collect(Collectors.toList()));
	}

	public String join(CharSequence delimiter) {
		return this.tList.stream().map(Optional::ofNullable).map(x -> x.map(String::valueOf).orElse(""))
				.collect(Collectors.joining(delimiter));
	}

	public JSLikeList<T> with(List<T> other) {
		List<T> list = new ArrayList<>();
		list.addAll(this.toList());
		list.addAll(other);
		return new JSLikeList<>(list);
	}

	public String join() {
		return join(",");
	}

	public boolean isEmpty() {
		return this.tList == null || this.tList.isEmpty();
	}

	public Optional<T> get(int index) {
		if (index < 0) {
			return Optional.empty();
		}
		if (tList.size() <= index) {
			return Optional.empty();
		}
		return Optional.of(this.tList.get(index));
	}

	@Override
	public Iterator<T> iterator() {
		return this.tList.iterator();
	}

	public int size() {
		return this.tList.size();
	}
}

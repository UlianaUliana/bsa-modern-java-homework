package com.binary_studio.uniq_in_sorted_stream;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class UniqueSortedStream {

	private UniqueSortedStream() {
	}
	
	public static <T> Predicate<T> distinctById(Function<? super T, ?> idSelector) {
		Map<Object, Boolean> map = new HashMap<>();
		return t -> map.putIfAbsent(idSelector.apply(t), Boolean.TRUE) == null;
	}

	public static <T> Stream<Row<T>> uniqueRowsSortedByPK(Stream<Row<T>> stream) {
		return stream.filter(distinctById(p -> p.getPrimaryId()));
	}
}

//With distinct:

//public final class UniqueSortedStream {
//
//	private UniqueSortedStream() {
//	}
//
//	public static <T> Stream<Row<T>> uniqueRowsSortedByPK(Stream<Row<T>> stream) {
//		return stream.distinct();
//	}
//}


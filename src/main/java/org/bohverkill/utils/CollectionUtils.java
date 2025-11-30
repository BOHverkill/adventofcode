package org.bohverkill.utils;

import org.bohverkill.models.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;

public final class CollectionUtils {

    private CollectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> List<Pair<T, T>> combinations(List<T> list) {
        if (list.isEmpty()) {
            return List.of();
        }
        final T first = list.getFirst();
        final List<T> nextList = list.subList(1, list.size());
        final List<Pair<T, T>> result = nextList.stream().map(t -> Pair.of(first, t)).collect(Collectors.toList());
        result.addAll(combinations(nextList));
        return result;
    }

    public static <T> List<Pair<T, T>> pairs(List<T> list) {
        return list.stream().gather(Gatherers.windowSliding(2)).map(Pair::of).toList();
    }

    public static <K, V> void putOrApply(final Map<K, V> map, final K key, final V value, BinaryOperator<V> applyOperator) {
        map.compute(key, (_, v) -> v == null ? value : applyOperator.apply(v, value));
    }

//    public static <T> List<T> fixedList(T... a) {
////        return Collections.unmodifiableList(Arrays.asList(a));
//        return List.of(a);
//    }

    public static <T> List<List<T>> copyList(final List<List<T>> src) {
        return src.stream().map(ArrayList::new).collect(Collectors.toCollection(() -> new ArrayList<>(src.size())));
    }
}

package org.bohverkill.utils;

import org.bohverkill.models.Pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        // NOTE: can be implemented with Stream Gatherers in the future, once JEP 473 is finalized
        // return list.stream().gather(Gatherers.windowSliding(2)).toList();
        return IntStream.range(0, list.size() - 1).mapToObj(i -> Pair.of(list.get(i), list.get(i + 1))).toList();
    }
}

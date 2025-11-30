package org.bohverkill.models;

import org.bohverkill.utils.AssertionUtils;

import java.util.List;

public record Pair<A, B>(A a, B b) {
    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    public static <A> Pair<A, A> of(List<A> list) {
        AssertionUtils.assertEquals(2, list.size());
        return new Pair<>(list.getFirst(), list.getLast());
    }

    public static <A> Pair<A, A> of(A[] array) {
        AssertionUtils.assertEquals(2, array.length);
        return new Pair<>(array[0], array[1]);
    }
}
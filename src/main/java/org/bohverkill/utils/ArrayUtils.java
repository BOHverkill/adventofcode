package org.bohverkill.utils;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ArrayUtils {

    private ArrayUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void reverse(final char[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            final char tmp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = tmp;
        }
    }

    public static void reverse(final int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            final int tmp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = tmp;
        }
    }

    public static char[] concat(final char[] array1, final char[] array2) {
        char[] out = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, out, array1.length, array2.length);
        return out;
    }

    public static int[] concat(final int[] array1, final int[] array2) {
        int[] out = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, out, array1.length, array2.length);
        return out;
    }

    public static void subCircularReverse(final char[] list, final int from, final int to) {
        if (to <= list.length) {
            subReverse(list, from, to);
        } else {
            final char[] first = Arrays.copyOfRange(list, from, list.length);
            final char[] second = Arrays.copyOfRange(list, 0, to % list.length);
            final char[] concat = concat(first, second);
            reverse(concat);
            System.arraycopy(concat, 0, list, from, first.length);
            System.arraycopy(concat, first.length, list, 0, second.length);
        }
    }

    public static void subCircularReverse(final int[] list, final int from, final int to) {
        if (to <= list.length) {
            subReverse(list, from, to);
        } else {
            final int[] first = Arrays.copyOfRange(list, from, list.length);
            final int[] second = Arrays.copyOfRange(list, 0, to % list.length);
            final int[] concat = concat(first, second);
            reverse(concat);
            System.arraycopy(concat, 0, list, from, first.length);
            System.arraycopy(concat, first.length, list, 0, second.length);
        }
    }

    public static void subReverse(char[] list, int from, int to) {
        final char[] copy = Arrays.copyOfRange(list, from, to);
        reverse(copy);
        System.arraycopy(copy, 0, list, from, to - from);
    }

    public static void subReverse(int[] list, int from, int to) {
        final int[] copy = Arrays.copyOfRange(list, from, to);
        reverse(copy);
        System.arraycopy(copy, 0, list, from, to - from);
    }

    public static Stream<Character> stream(final char[] array) {
        return IntStream.range(0, array.length).mapToObj(i -> array[i]);
    }
}

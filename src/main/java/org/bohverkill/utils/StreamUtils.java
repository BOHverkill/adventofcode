package org.bohverkill.utils;

import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;

public final class StreamUtils {

    private StreamUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T applyOnArray(T[] values, BinaryOperator<T> op, T initialValue) {
        T value = initialValue;
        for (T v : values) {
            value = op.apply(value, v);
        }
        return value;
    }

    public static float applyOnArray(float[] values, BinaryOperator<Float> op, float initialValue) {
        float value = initialValue;
        for (float v : values) {
            value = op.apply(value, v);
        }
        return value;
    }

    public static double applyOnArray(double[] values, DoubleBinaryOperator op, double initialValue) {
        double value = initialValue;
        for (double v : values) {
            value = op.applyAsDouble(value, v);
        }
        return value;
    }

    public static int applyOnArray(int[] values, IntBinaryOperator op, int initialValue) {
        int value = initialValue;
        for (int v : values) {
            value = op.applyAsInt(value, v);
        }
        return value;
    }

    public static long applyOnArray(long[] values, LongBinaryOperator op, long initialValue) {
        long value = initialValue;
        for (long v : values) {
            value = op.applyAsLong(value, v);
        }
        return value;
    }

    public static <T> T applyOnCollection(Collection<T> values, BinaryOperator<T> op, T initialValue) {
        return values.stream().reduce(initialValue, op);
    }
}

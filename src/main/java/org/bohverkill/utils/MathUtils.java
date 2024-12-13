package org.bohverkill.utils;

import java.util.Collection;
import java.util.Optional;

public final class MathUtils {

    private MathUtils() {
        throw new IllegalStateException("Utility class");
    }

    // from https://stackoverflow.com/a/4202114
    public static float gcd(float a, float b) {
        while (b > 0) {
            float temp = b;
            b = a % b; // % = remainder
            a = temp;
        }
        return a;
    }

    public static double gcd(double a, double b) {
        while (b > 0) {
            double temp = b;
            b = a % b; // % = remainder
            a = temp;
        }
        return a;
    }

    public static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % = remainder
            a = temp;
        }
        return a;
    }

    public static int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b; // % = remainder
            a = temp;
        }
        return a;
    }

    public static float lcm(float a, float b) {
        return a * (b / gcd(a, b));
    }

    public static double lcm(double a, double b) {
        return a * (b / gcd(a, b));
    }

    public static int lcm(int a, int b) {
        return a * (b / gcd(a, b));
    }

    public static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    public static float lcm(float[] input) {
        float result = input[0];
        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }

    public static double lcm(double[] input) {
        double result = input[0];
        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }

    public static int lcm(int[] input) {
        int result = input[0];
        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }

    public static long lcm(long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }

    // Source: https://www.cs.emory.edu/~cheung/Courses/170/Syllabus/09/gaussian-elim.html
    public static void gaussianElimination(double[][] coefficientMatrix, double[] rhs) {
        int numberUnknowns = rhs.length;

        for (int i = 0; i < numberUnknowns; i++) {
            // Select pivot
            double pivot = coefficientMatrix[i][i];
            // Normalize row i
            for (int j = 0; j < numberUnknowns; j++) {
                coefficientMatrix[i][j] = coefficientMatrix[i][j] / pivot;
            }

            rhs[i] = rhs[i] / pivot;

            // Sweep using row i
            for (int k = 0; k < numberUnknowns; k++) {
                if (k != i) {
                    double factor = coefficientMatrix[k][i];
                    for (int j = 0; j < numberUnknowns; j++)
                        coefficientMatrix[k][j] = coefficientMatrix[k][j] - factor * coefficientMatrix[i][j];
                    rhs[k] = rhs[k] - factor * rhs[i];
                }
            }
        }
        // result in rhs
    }

    // from https://stackoverflow.com/a/40087987
    public static <E> Optional<E> getRandom(Collection<E> e) {
        return e.stream().skip((int) (e.size() * Math.random())).findFirst();
    }

    public static double min(float[] floats) {
        return StreamUtils.applyOnArray(floats, Math::min, Float.MAX_VALUE);
    }

    public static double min(double[] doubles) {
        return StreamUtils.applyOnArray(doubles, Math::min, Double.MAX_VALUE);
    }

    public static double min(int[] integers) {
        return StreamUtils.applyOnArray(integers, Math::min, Integer.MAX_VALUE);
    }

    public static double min(long[] longs) {
        return StreamUtils.applyOnArray(longs, Math::min, Long.MAX_VALUE);
    }

    public static double min(Float... floats) {
        return StreamUtils.applyOnArray(floats, Math::min, Float.MAX_VALUE);
    }

    public static double min(Double... doubles) {
        return StreamUtils.applyOnArray(doubles, Math::min, Double.MAX_VALUE);
    }

    public static double min(Integer... integers) {
        return StreamUtils.applyOnArray(integers, Math::min, Integer.MAX_VALUE);
    }

    public static double min(Long... longs) {
        return StreamUtils.applyOnArray(longs, Math::min, Long.MAX_VALUE);
    }

    public static double max(float[] floats) {
        return StreamUtils.applyOnArray(floats, Math::max, Float.MIN_VALUE);
    }

    public static double max(double[] doubles) {
        return StreamUtils.applyOnArray(doubles, Math::max, Double.MIN_VALUE);
    }

    public static double max(int[] integers) {
        return StreamUtils.applyOnArray(integers, Math::max, Integer.MIN_VALUE);
    }

    public static double max(long[] longs) {
        return StreamUtils.applyOnArray(longs, Math::max, Long.MIN_VALUE);
    }

    public static double max(Float... floats) {
        return StreamUtils.applyOnArray(floats, Math::max, Float.MIN_VALUE);
    }

    public static double max(Double... doubles) {
        return StreamUtils.applyOnArray(doubles, Math::max, Double.MIN_VALUE);
    }

    public static double max(Integer... integers) {
        return StreamUtils.applyOnArray(integers, Math::max, Integer.MIN_VALUE);
    }

    public static double max(Long... longs) {
        return StreamUtils.applyOnArray(longs, Math::max, Long.MIN_VALUE);
    }

    public static boolean negative(int a) {
        return a < 0;
    }

    public static boolean isInt(double d) {
        return Double.isFinite(d) && d == Math.rint(d);
    }

    public static boolean isInt(float f) {
        return Float.isFinite(f) && f == Math.rint(f);
    }

    public static boolean isInt(double d, double delta) {
        return Double.isFinite(d) && Math.abs(d - Math.round(d)) < delta;
    }

    public static boolean isInt(float d, float delta) {
        return Double.isFinite(d) && Math.abs(d - Math.round(d)) < delta;
    }

    public static Optional<Integer> asInt(double d) {
        if (isInt(d)) {
            return Optional.of((int) d);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Integer> asInt(float f) {
        if (isInt(f)) {
            return Optional.of((int) f);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Long> asIntLong(double d) {
        if (isInt(d)) {
            return Optional.of((long) d);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Long> asIntLong(float f) {
        if (isInt(f)) {
            return Optional.of((long) f);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Integer> asInt(double d, double delta) {
        if (isInt(d, delta)) {
            return Optional.of((int) d);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Integer> asInt(float f, float delta) {
        if (isInt(f, delta)) {
            return Optional.of((int) f);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Long> asIntLong(double d, double delta) {
        if (isInt(d, delta)) {
            return Optional.of((long) d);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Long> asIntLong(float f, float delta) {
        if (isInt(f, delta)) {
            return Optional.of((long) f);
        } else {
            return Optional.empty();
        }
    }

    public static int ensureInt(double d) {
        return asInt(d).orElseThrow(MathUtils::createNotAnIntegerIllegalStateExceptionException);
    }

    public static int ensureInt(float f) {
        return asInt(f).orElseThrow(MathUtils::createNotAnIntegerIllegalStateExceptionException);
    }

    public static long ensureIntLong(double d) {
        return asIntLong(d).orElseThrow(MathUtils::createNotAnIntegerIllegalStateExceptionException);
    }

    public static long ensureIntLong(float f) {
        return asIntLong(f).orElseThrow(MathUtils::createNotAnIntegerIllegalStateExceptionException);
    }

    public static int ensureInt(double d, double delta) {
        return asInt(d, delta).orElseThrow(MathUtils::createNotAnIntegerIllegalStateExceptionException);
    }

    public static int ensureInt(float f, float delta) {
        return asInt(f, delta).orElseThrow(MathUtils::createNotAnIntegerIllegalStateExceptionException);
    }

    public static long ensureIntLong(double d, double delta) {
        return asIntLong(d, delta).orElseThrow(MathUtils::createNotAnIntegerIllegalStateExceptionException);
    }

    public static long ensureIntLong(float f, float delta) {
        return asIntLong(f, delta).orElseThrow(MathUtils::createNotAnIntegerIllegalStateExceptionException);
    }

    private static IllegalStateException createNotAnIntegerIllegalStateExceptionException() {
        return new IllegalStateException("Not an integer");
    }

    // Stream function shortcuts
    public static float plus(float a, float b) {
        return a + b;
    }

    public static double plus(double a, double b) {
        return a + b;
    }

    public static int plus(int a, int b) {
        return a + b;
    }

    public static long plus(long a, long b) {
        return a + b;
    }

    public static float minus(float a, float b) {
        return a - b;
    }

    public static double minus(double a, double b) {
        return a - b;
    }

    public static int minus(int a, int b) {
        return a - b;
    }

    public static long minus(long a, long b) {
        return a - b;
    }

    public static float times(float a, float b) {
        return a * b;
    }

    public static double times(double a, double b) {
        return a * b;
    }

    public static int times(int a, int b) {
        return a * b;
    }

    public static long times(long a, long b) {
        return a * b;
    }

    public static float divided(float a, float b) {
        return a / b;
    }

    public static double divided(double a, double b) {
        return a / b;
    }

    public static int divided(int a, int b) {
        return a / b;
    }

    public static long divided(long a, long b) {
        return a / b;
    }

    public static float square(float a) {
        return a * a;
    }

    public static double square(double a) {
        return a * a;
    }

    public static int square(int a) {
        return a * a;
    }

    public static long square(long a) {
        return a * a;
    }
}

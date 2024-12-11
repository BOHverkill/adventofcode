package org.bohverkill.utils;

public final class AssertionUtils {

    private AssertionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static float assertEquals(float a, float b) {
        if (a != b) {
            throw new AssertionError("Expected '" + a + "' and '" + b + "' to be equal");
        }
        return a;
    }

    public static double assertEquals(double a, double b) {
        if (a != b) {
            throw new AssertionError("Expected '" + a + "' and '" + b + "' to be equal");
        }
        return a;
    }

    public static int assertEquals(int a, int b) {
        if (a != b) {
            throw new AssertionError("Expected '" + a + "' and '" + b + "' to be equal");
        }
        return a;
    }

    public static long assertEquals(long a, long b) {
        if (a != b) {
            throw new AssertionError("Expected '" + a + "' and '" + b + "' to be equal");
        }
        return a;
    }

    public static <T> T assertEquals(T a, T b) {
        if (!a.equals(b)) {
            throw new AssertionError("Expected '" + a + "' and '" + b + "' to be equal");
        }
        return a;
    }

    public static Object assertEqualsObjects(Object a, Object b) {
        if (!a.equals(b)) {
            throw new AssertionError("Expected '" + a + "' and '" + b + "' to be equal");
        }
        return a;
    }

    public static Object assertEqualsReferences(Object a, Object b) {
        if (a != b) {
            throw new AssertionError("Expected '" + a + "' and '" + b + "' to be equal");
        }
        return a;
    }
}

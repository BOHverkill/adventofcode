package org.bohverkill.utils;

public final class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String[] splitAfterNChars(String text, int n) {
        return text.split("(?<=\\G.{" + n + "})");
    }

    public static String replaceCharAt(String string, int i, char c) {
        final StringBuilder stringBuilder = new StringBuilder(string);
        stringBuilder.setCharAt(i, c);
        return stringBuilder.toString();
    }

    public static String reverse(String string) {
        return new StringBuilder(string).reverse().toString();
    }

    public static int subStringCount(String string, String substring) {
        String temp = string.replace(substring, "");
        return (string.length() - temp.length()) / substring.length();
    }

    public static char[][] toCharArray(String[] strings) {
        char[][] out = new char[strings.length][strings[0].length()];
        for (int i = 0; i < strings.length; i++) {
            out[i] = strings[i].toCharArray();
        }
        return out;
    }

    public static String[] fromCharArray(char[][] chars) {
        String[] out = new String[chars.length];
        for (int i = 0; i < chars.length; i++) {
            out[i] = new String(chars[i]);
        }
        return out;
    }

}

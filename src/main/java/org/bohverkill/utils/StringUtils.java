package org.bohverkill.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String[] splitAfterNChars(String text, int n) {
        return text.split("(?<=\\G.{" + n + "})");
    }

    public static String[] splitMiddle(String text) {
        return new String[]{text.substring(0, text.length() / 2), text.substring(text.length() / 2)};
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

    public static Stream<String> parseCharToStringStream(final String string) {
        return Arrays.stream(string.split(""));
    }

    public static List<String> parseCharToStringList(final String string) {
        return parseCharToStringStream(string).toList();
    }

    public static String[] parseCharToStringToArray(final String string) {
        return parseCharToStringStream(string).toArray(String[]::new);
    }

    public static Stream<Integer> parseCharToIntegerStream(final String string) {
        return Arrays.stream(string.split("")).map(Integer::parseInt);
    }

    public static IntStream parseCharToIntStream(final String string) {
        return Arrays.stream(string.split("")).mapToInt(Integer::parseInt);
    }

    public static List<Integer> parseCharToIntegerList(final String string) {
        return parseCharToIntegerStream(string).toList();
    }

    public static int[] parseCharToIntegerToArray(final String string) {
        return parseCharToIntStream(string).toArray();
    }
}

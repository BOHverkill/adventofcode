package org.bohverkill.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class Utils {

    private static final Pattern NEWLIN_PATTERN = Pattern.compile("\n");

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static String[] getLines(String input) {
        return NEWLIN_PATTERN.split(input);
    }

    public static Stream<String> getSplit(String input, String regex) {
        return Arrays.stream(input.split(regex));
    }

    public static Stream<String> getInputLines(String name) {
        try {
            return Files.lines(Paths.get(Objects.requireNonNull(Utils.class.getResource(name)).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] getInputArray(String name) {
        return getLines(getInput(name));
    }

    public static List<String> getInputList(String name) {
        try {
            return Files.readAllLines(Paths.get(Objects.requireNonNull(Utils.class.getResource(name)).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getInput(String name) {
        try {
            return Files.readString(Paths.get(Objects.requireNonNull(Utils.class.getResource(name)).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<String> getInputSplit(String name, String regex) {
        return getSplit(getInput(name), regex);
    }

    public static Matcher getMatcher(Pattern pattern, String input) {
        final Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            throw new IllegalStateException("Malformed input: " + input);
        }
        return matcher;
    }

    public static Stream<String> getAllMatches(Pattern pattern, String input) {
        return pattern.matcher(input).results().map(MatchResult::group);
    }

    public static List<Integer> findPositions(final Pattern pattern, String input) {
        final Matcher matcher = pattern.matcher(input);
        List<Integer> positions = new ArrayList<>();
        while (matcher.find()) {
            positions.add(matcher.start());
        }
        return positions;
    }

}

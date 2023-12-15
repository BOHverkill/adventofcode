package org.bohverkill.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public static Stream<String> getExampleLines(String example) {
//        return Arrays.stream(example.split("\n"));
        return NEWLIN_PATTERN.splitAsStream(example);
    }

    public static Stream<String> getInputLines(String name) {
        try {
            return Files.lines(Paths.get(Objects.requireNonNull(Day7Part2.class.getResource(name)).getPath()));
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

    public static String replaceCharAt(String string, int i, char c) {
        final StringBuilder stringBuilder = new StringBuilder(string);
        stringBuilder.setCharAt(i, c);
        return stringBuilder.toString();
    }

}

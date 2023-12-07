package org.bohverkill.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static Stream<String> getExampleLines(String example) {
        return Arrays.stream(example.split("\n"));
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
            return Files.readString(Paths.get(Objects.requireNonNull(Day6.class.getResource(name)).getPath()));
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
}

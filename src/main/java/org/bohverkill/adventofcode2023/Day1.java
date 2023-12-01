package org.bohverkill.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day1 {
    public static void main(String[] args) {
        try (Stream<String> stream = Files.lines(Paths.get(Objects.requireNonNull(Day1.class.getResource("/2023/Day1_input")).getPath()))) {
//            System.out.println(stream.mapToInt(Day1::getCalibrationValue).sum());
            System.out.println(stream.mapToInt(Day1::getRealCalibrationValue).sum());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getCalibrationValue(String line) {
        final List<Integer> integers = Arrays.stream(line.split("\\D+")).filter(s -> !s.isEmpty()).flatMapToInt(String::chars).map(value -> Integer.parseInt(String.valueOf((char) value))).boxed().toList();
        return Integer.parseInt(String.valueOf(integers.getFirst()) + integers.getLast());
    }

    public static int getRealCalibrationValue(String line) {
        Matcher matcher = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)").matcher(line);
        String first = null;
        String last = null;

        // need to start matcher again to also find "eighthree" = 83 and "sevenine" = 79
        for (int i = 0; matcher.find(i); i++) {
            last = mapLetters(matcher.group());
            if (first == null) {
                first = last;
            }
        }
        return Integer.parseInt(first + last);
    }

    public static String mapLetters(String letters) {
        return switch (letters.toLowerCase()) {
            case "1", "2", "3", "4", "5", "6", "7", "8", "9" -> letters;
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            default -> null;
        };
    }
}

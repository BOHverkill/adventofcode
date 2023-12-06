package org.bohverkill.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day6 {
    private static final Pattern TIME_PATTERN = Pattern.compile("^Time: +([\\d ]+)$");
    private static final Pattern DISTANCE_PATTERN = Pattern.compile("^Distance: +([\\d ]+)$");

    public static void main(String[] args) {
//        String inputString = getExample();
        String inputString = getInput();
        System.out.println("Part 1: " + parseRacePart1(inputString).stream().mapToLong(Day6::getWins).reduce(1, (left, right) -> left * right));
        System.out.println("Part 2: " + getWins(parseRacePart2(inputString)));
    }

    public static String getExample() {
        try {
            return Files.readString(Paths.get(Objects.requireNonNull(Day6.class.getResource("/2023/Day6_example")).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getInput() {
        try {
            return Files.readString(Paths.get(Objects.requireNonNull(Day6.class.getResource("/2023/Day6_input")).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Race> parseRacePart1(String input) {
        final String[] lines = input.split("\n");
        if (lines.length != 2) {
            throw new IllegalStateException("Malformed input: " + input);
        }
        final String[] times = getMatch(TIME_PATTERN, lines[0]).split(" +");
        final String[] distances = getMatch(DISTANCE_PATTERN, lines[1]).split(" +");
        if (times.length != distances.length) {
            throw new IllegalStateException("Malformed input: Lines do not have the same number of numbers: " + input);
        }
        return IntStream.range(0, times.length).mapToObj(i -> new Race(Integer.parseInt(times[i]), Integer.parseInt(distances[i]))).toList();
    }

    public static Race parseRacePart2(String input) {
        final String[] lines = input.split("\n");
        if (lines.length != 2) {
            throw new IllegalStateException("Malformed input: " + input);
        }
        final long time = Long.parseLong(getMatch(TIME_PATTERN, lines[0]).replace(" ", ""));
        final long distance = Long.parseLong(getMatch(DISTANCE_PATTERN, lines[1]).replace(" ", ""));
        return new Race(time, distance);
    }

    public static long getWins(Race race) {
        return LongStream.range(1, race.time).map(i -> getDistance(i, race.time)).filter(distance -> distance > race.distance).count();
    }

    public static long getDistance(long holdTime, long raceTime) {
        return (raceTime - holdTime) * holdTime;
    }

    private static String getMatch(Pattern timePattern, String input) {
        return getMatcher(timePattern, input).group(1);
    }

    private static Matcher getMatcher(Pattern timePattern, String input) {
        final Matcher matcher = timePattern.matcher(input);
        if (!matcher.find()) {
            throw new IllegalStateException("Malformed input: " + input);
        }
        return matcher;
    }

    public record Race(long time, long distance) {
    }
}

package org.bohverkill.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 {
    private static final Map<CubeColor, Integer> POSSIBLE_CUBES = Map.of(CubeColor.RED, 12, CubeColor.GREEN, 13, CubeColor.BLUE, 14);
    private static final Pattern GAME_PATTERN = Pattern.compile("^Game (\\d+): ?(.*)$");
    private static final Pattern CUBE_PATTERN = Pattern.compile("^ ?(\\d+) (red|green|blue)$");

    public static void main(String[] args) {
//        try (Stream<String> stream = Files.lines(Paths.get(Objects.requireNonNull(Day2.class.getResource("/2023/Day2_example")).getPath()))) {
        try (Stream<String> stream = Files.lines(Paths.get(Objects.requireNonNull(Day2.class.getResource("/2023/Day2_input")).getPath()))) {
//            System.out.println(stream.map(Day2::convertGame).mapToInt(Day2::possibleGames).sum());
            System.out.println(stream.map(Day2::convertGame).mapToInt(Day2::calculatePower).sum());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Game convertGame(String line) {
        final Matcher gameMatcher = GAME_PATTERN.matcher(line);
        if (!gameMatcher.find()) {
            throw new IllegalStateException("Malformed input: " + line);
        }
        final int gameId = Integer.parseInt(gameMatcher.group(1));
        List<Map<CubeColor, Integer>> subset = Arrays.stream(gameMatcher.group(2).split(";")).map(s -> Arrays.stream(s.split(",")).map(Day2::convertCubesColor).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))).toList();
        return new Game(gameId, subset);
    }

    public static Map.Entry<CubeColor, Integer> convertCubesColor(String cubesColor) {
        final Matcher gameMatcher = CUBE_PATTERN.matcher(cubesColor);
        if (!gameMatcher.find()) {
            throw new IllegalStateException("Malformed input: " + cubesColor);
        }
        final int number = Integer.parseInt(gameMatcher.group(1));
        return Map.entry(CubeColor.parse(gameMatcher.group(2)), number);
    }

    public static int possibleGames(Game game) {
        return game.subset.stream().anyMatch(Day2::impossibleSubset) ? 0 : game.id;
    }

    public static int calculatePower(Game game) {
        final Map<CubeColor, Integer> collect = game.subset.stream().flatMap(m -> m.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Math::max));
        return collect.values().stream().mapToInt(value -> value).reduce(1, (left, right) -> left * right);
    }

    public static boolean impossibleSubset(Map<CubeColor, Integer> subset) {
        return subset.entrySet().stream().anyMatch(c -> c.getValue() > POSSIBLE_CUBES.get(c.getKey()));
    }

    public enum CubeColor {
        RED, GREEN, BLUE;

        public static CubeColor parse(String color) {
            return switch (color) {
                case "red" -> RED;
                case "green" -> GREEN;
                case "blue" -> BLUE;
                default -> null;
            };
        }
    }

    public record Game(int id, List<Map<CubeColor, Integer>> subset) {
    }
}

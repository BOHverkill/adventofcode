package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Stream;

public class Day21Part2 {
    private static final int STEP_COUNT = 26501365;

    private static final char START_CHAR = 'S';

    private static final char ROCK_CHAR = '#';

    public static void main(String[] args) {
//        final String input = Utils.getInput("/2023/Day21_example");
        final String input = Utils.getInput("/2023/Day21_input");

        final String[] map = Utils.getLines(input);

        Tile startingPosition = findStartingPosition(map);

        final Map<Tile, Integer> stepMap = calculateSteps(map, startingPosition);

//        System.out.println("Day 21 Part 1: " + stepMap.values().stream().filter(i -> i <= 64 && i % 2 == 0).count()); // alternative solution for part 1

        // from https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21
        final List<Integer> evenSteps = stepMap.values().stream().filter(i -> i % 2 == 0).toList();
        final List<Integer> oddSteps = stepMap.values().stream().filter(i -> i % 2 == 1).toList();

        final int halfDistance = map.length / 2;
        long evenCorners = evenSteps.stream().filter(i -> i > halfDistance).count();
        long oddCorners = oddSteps.stream().filter(i -> i > halfDistance).count();

        long evenFull = evenSteps.size();
        long oddFull = oddSteps.size();

        long n = ((STEP_COUNT - (halfDistance)) / map.length);
        assert n == 202300;

        long res = Math.round(Math.pow(n + 1.0, 2.0) * oddFull + Math.pow(n, 2.0) * evenFull - (n + 1) * oddCorners + n * evenCorners);

        System.out.println("Day 21 Part 2: " + res);
    }

    public static Map<Tile, Integer> calculateSteps(String[] map, Tile startingPosition) {
        Queue<Map.Entry<Tile, Integer>> queue = new ArrayDeque<>();
        Map<Tile, Integer> stepMap = new HashMap<>();

        queue.add(Map.entry(startingPosition, 0));
        while (!queue.isEmpty()) {
            final Map.Entry<Tile, Integer> current = queue.remove();
            final Tile currentTile = current.getKey();
            final int currentStep = current.getValue();

            stepMap.computeIfAbsent(currentTile, t -> {
                currentTile.neighbors(map).filter(neighbor -> !stepMap.containsKey(neighbor)).map(tile -> Map.entry(tile, currentStep + 1)).forEach(queue::add);
                return currentStep;
            });
        }
        return stepMap;
    }

    public static Tile findStartingPosition(String[] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length(); j++) {
                if (map[i].charAt(j) == START_CHAR) {
                    return new Tile(i, j);
                }
            }
        }
        throw new IllegalStateException("No start found");
    }

    public record Tile(int row, int column) {

        private static Optional<Tile> build(int row, int column, String[] map) {
            if (isValid(row, column, map.length, map[0].length()) && map[row].charAt(column) != ROCK_CHAR) {
                return Optional.of(new Tile(row, column));
            } else {
                return Optional.empty();
            }
        }

        private static boolean isValid(int row, int column, int rowSize, int columnSize) {
            return row >= 0 && row < rowSize && column >= 0 && column < columnSize;
        }

        public Stream<Tile> neighbors(String[] map) {
            return Stream.of(build(row(), column() + 1, map), build(row(), column() - 1, map), build(row() + 1, column(), map), build(row() - 1, column(), map)).flatMap(Optional::stream);
        }
    }
}

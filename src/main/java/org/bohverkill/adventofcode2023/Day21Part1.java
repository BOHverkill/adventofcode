package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Stream;

public class Day21Part1 {
//        private static final int STEP_COUNT = 6;
    private static final int STEP_COUNT = 64;

    private static final char START_CHAR = 'S';

    private static final char ROCK_CHAR = '#';

    public static void main(String[] args) {
//        final String input = Utils.getInput("/2023/Day21_example");
        final String input = Utils.getInput("/2023/Day21_input");

        final String[] map = Utils.getLines(input);

        Tile startingPosition = findStartingPosition(map);

        final int res = solve(map, startingPosition);

        System.out.println("Day 21 Part 1: " + res);
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

    public static int solve(String[] map, Tile startingPosition) {
        Queue<Tile> queue = new ArrayDeque<>();
        Set<Tile> endPositions = new HashSet<>();

        queue.add(startingPosition);
        while (!queue.isEmpty()) {
            final Tile current = queue.remove();

            if (current.stepCount() == STEP_COUNT) {
                endPositions.add(current);
            } else {
                current.neighbors(map).filter(neighbor -> !queue.contains(neighbor)).forEach(queue::add);
            }
        }
        return endPositions.size();
    }

    public record Tile(int row, int column, int stepCount) {

        public Tile(int row, int column) {
            this(row, column, 0);
        }

        private static Optional<Tile> build(int row, int column, int stepCount, String[] map) {
            if (isValid(row, column, map.length, map[0].length()) && map[row].charAt(column) != ROCK_CHAR && stepCount <= STEP_COUNT) {
                return Optional.of(new Tile(row, column, stepCount));
            } else {
                return Optional.empty();
            }
        }

        private static boolean isValid(int row, int column, int rowSize, int columnSize) {
            return row >= 0 && row < rowSize && column >= 0 && column < columnSize;
        }

        public Stream<Tile> neighbors(String[] map) {
            return Stream.of(build(row(), column() + 1, stepCount() + 1, map), build(row(), column() - 1, stepCount() + 1, map), build(row() + 1, column(), stepCount() + 1, map), build(row() - 1, column(), stepCount() + 1, map)).flatMap(Optional::stream);
        }
    }
}

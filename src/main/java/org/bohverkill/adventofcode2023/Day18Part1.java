package org.bohverkill.adventofcode2023;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day18Part1 {
    private static final Pattern TRENCH_PATTERN = Pattern.compile("^([UDLR]) (\\d+) \\(#([0-9a-f]{6})\\)$");

    public static void main(String[] args) {
//        Stream<String> input = Utils.getInputLines("/2023/Day18_example");
        Stream<String> input = Utils.getInputLines("/2023/Day18_input");
        final List<Trench> trenches = input.map(Day18Part1::mapToTrench).toList();
        final Position start = new Position(0, 0);
        List<Position> positions = new ArrayList<>();
        positions.add(start);
        Position currentPosition = start;
        for (Trench trench : trenches) {
            dig(currentPosition, trench, positions);
            currentPosition = positions.getLast();
        }
//        print(positions);
        fill(positions);
//        print(positions);
        System.out.println("Day 18 Part 1: " + (positions.size() - 1));
    }

    public static void fill(List<Position> positions) {
        final int minRow = positions.stream().min(Comparator.comparing(Position::row)).map(Position::row).orElseThrow();
        final int maxRow = positions.stream().max(Comparator.comparing(Position::row)).map(Position::row).orElseThrow();
        final int minColumn = positions.stream().min(Comparator.comparing(Position::column)).map(Position::column).orElseThrow();
        final int maxColumn = positions.stream().max(Comparator.comparing(Position::column)).map(Position::column).orElseThrow();

        Set<Position> positionsSet = new HashSet<>(positions);

        for (int i = minRow; i <= maxRow; i++) {
            boolean inside = false;
            int matches = 0;
            for (int j = minColumn; j <= maxColumn; j++) {
                final boolean contains = positionsSet.contains(new Position(i, j));
                if (contains) {
                    if (matches == 0 && positionsSet.contains(new Position(i + 1, j))) {
                        inside = !inside;
                    }
                    matches++;
                } else {
                    if (matches > 0) {
                        if (matches == 1) {
                            inside = !inside;
                        }
                        if (positionsSet.contains(new Position(i + 1, j - 1))) {
                            inside = !inside;
                        }
                        matches = 0;
                    }
                    if (inside) {
                        positions.add(new Position(i, j));
                    }
                }
            }
        }
    }

    public static void dig(Position currentPosition, Trench trench, List<Position> positions) {
        switch (trench.direction()) {
            case UP -> {
                for (int i = 1; i <= trench.number(); i++) {
                    positions.add(new Position(currentPosition.row() - i, currentPosition.column()));
                }
            }
            case DOWN -> {
                for (int i = 1; i <= trench.number(); i++) {
                    positions.add(new Position(currentPosition.row() + i, currentPosition.column()));
                }
            }
            case LEFT -> {
                for (int i = 1; i <= trench.number(); i++) {
                    positions.add(new Position(currentPosition.row(), currentPosition.column() - i));
                }
            }
            case RIGHT -> {
                for (int i = 1; i <= trench.number(); i++) {
                    positions.add(new Position(currentPosition.row(), currentPosition.column() + i));
                }
            }
        }
    }

    public static void print(List<Position> positions) {
        final int minRow = positions.stream().min(Comparator.comparing(Position::row)).map(Position::row).orElseThrow();
        final int maxRow = positions.stream().max(Comparator.comparing(Position::row)).map(Position::row).orElseThrow();
        final int minColumn = positions.stream().min(Comparator.comparing(Position::column)).map(Position::column).orElseThrow();
        final int maxColumn = positions.stream().max(Comparator.comparing(Position::column)).map(Position::column).orElseThrow();

        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minColumn; j <= maxColumn; j++) {
                int finalI = i;
                int finalJ = j;
                final Optional<Position> position = positions.stream().filter(p -> p.row == finalI && p.column == finalJ).findFirst();
                position.ifPresentOrElse(ignored -> System.out.print("#"), () -> System.out.print("."));
            }
            System.out.println();
        }
        System.out.println("=".repeat(maxColumn - minColumn));
    }

    public static Trench mapToTrench(String line) {
        final Matcher trenchMatcher = Utils.getMatcher(TRENCH_PATTERN, line);

        final Direction direction = Direction.parse(trenchMatcher.group(1));
        final int number = Integer.parseInt(trenchMatcher.group(2));
        final String color = trenchMatcher.group(3);

        return new Trench(direction, number, color);
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public static Direction parse(String direction) {
            return switch (direction) {
                case "U" -> UP;
                case "D" -> DOWN;
                case "L" -> LEFT;
                case "R" -> RIGHT;
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            };
        }
    }

    public record Position(int row, int column) {

    }

    public record Trench(Direction direction, int number, String color) {

    }
}

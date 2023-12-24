package org.bohverkill.adventofcode2023;


import java.util.*;
import java.util.stream.Stream;

public class Day23Part1 {
    public static void main(String[] args) {
//        final String input = Utils.getInput("/2023/Day23_example");
        final String input = Utils.getInput("/2023/Day23_input");

        final String[] map = Utils.getLines(input);
        final Tile start = new Tile(0, 1, Direction.DOWN, 0);
        final Tile goal = new Tile(map.length - 1, map[map.length - 1].length() - 2, Direction.DOWN, -1);

        final List<Tile> path = findPath(map, start, goal);
//        print(path, map);
        System.out.println("Day 23 Part 1: " + (path.size() - 1));
    }

    public static List<Tile> findPath(String[] map, Tile start, Tile goal) {
        Queue<Tile> queue = new ArrayDeque<>();
        Map<Tile, Tile> cameFrom = new HashMap<>();

        queue.add(start);
        while (!queue.isEmpty()) {
            final Tile current = queue.remove();

            for (Tile neighbor : current.neighbors(map)) {
                if (!cameFrom.containsKey(neighbor)) {
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                } else {
                    final Tile tile = cameFrom.get(neighbor);
                    if (current.stepCount() > tile.stepCount()) {
                        cameFrom.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
            }
        }
        return reconstructPath(cameFrom, goal);
    }

    public static List<Tile> reconstructPath(Map<Tile, Tile> cameFrom, Tile current) {
        List<Tile> totalPath = new LinkedList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.addFirst(current);
        }
        return totalPath;
    }

    public static void print(List<Tile> path, String[] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length(); j++) {
                final int finalI = i;
                final int finalJ = j;
                final Optional<Tile> first = path.stream().filter(tile -> tile.row == finalI && tile.column == finalJ).findFirst();
                System.out.print(first.map(tile -> 'O').orElseGet(() -> map[finalI].charAt(finalJ)));
            }
            System.out.println();
        }
    }

    public enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    public record Tile(int row, int column, Direction direction, int stepCount) {

        private static Optional<Tile> build(int row, int column, Direction current, Direction next, int stepCount, String[] map) {
            if (isValid(row, column, map.length, map[0].length()) && isNotBack(current, next) && (map[row].charAt(column) == '.' || map[row].charAt(column) != '#' && isDownhill(map[row].charAt(column), next))) {
                return Optional.of(new Tile(row, column, next, stepCount));
            } else {
                return Optional.empty();
            }
        }

        private static boolean isNotBack(Direction current, Direction next) {
            return switch (current) {
                case UP -> next != Direction.DOWN;
                case RIGHT -> next != Direction.LEFT;
                case DOWN -> next != Direction.UP;
                case LEFT -> next != Direction.RIGHT;
            };
        }

        private static boolean isDownhill(char character, Direction direction) {
            return switch (character) {
                case '^' -> direction == Direction.UP;
                case '>' -> direction == Direction.RIGHT;
                case 'v' -> direction == Direction.DOWN;
                case '<' -> direction == Direction.LEFT;
                default -> throw new IllegalStateException("Unexpected value: " + character);
            };
        }

        private static boolean isValid(int row, int column, int rowSize, int columnSize) {
            return row >= 0 && row < rowSize && column >= 0 && column < columnSize;
        }

        public List<Tile> neighbors(String[] map) {
            return Stream.of(build(row() - 1, column(), direction(), Direction.UP, stepCount() + 1, map), build(row(), column() + 1, direction(), Direction.RIGHT, stepCount() + 1, map), build(row() + 1, column(), direction(), Direction.DOWN, stepCount() + 1, map), build(row(), column() - 1, direction(), Direction.LEFT, stepCount() + 1, map)).flatMap(Optional::stream).toList();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return row == tile.row && column == tile.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }
}

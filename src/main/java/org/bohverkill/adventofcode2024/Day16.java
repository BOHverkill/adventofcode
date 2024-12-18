package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.CollectionUtils;
import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Stream;

public class Day16 {
    private static final int STEP_SCORE = 1;
    private static final int TURN_SCORE = 1000;

    public static void main(String[] args) {
//        final String path = "/2024/Day16_example";
        final String path = "/2024/Day16_input";
        final List<List<Tile>> map = Utils.getInputLines(path).map(line -> StringUtils.parseCharToStringStream(line).map(Tile::fromString).toList()).toList();
        final Pair<Integer, Integer> solutions = findSolutions(CollectionUtils.copyList(map));
        task1(solutions.a());
        task2(solutions.b());
    }

    private static void task1(final int solution) {
        System.out.println("Part 1: " + solution);
    }

    private static void task2(final int solution) {
        System.out.println("Part 2: " + solution);
    }

    private static Pair<Integer, Integer> findSolutions(List<List<Tile>> map) {
        final Pair<Integer, Integer> startPosition = findFirstTilePosition(map, Tile.START);

        int minimumScore = Integer.MAX_VALUE;

        Map<Position, Position> visited = new HashMap<>();
        final Position start = Position.of(startPosition, Direction.EAST, 0);
        visited.put(start, start);
        Queue<Position> queue = new PriorityQueue<>(Comparator.comparingInt(Position::score));
        queue.addAll(getNextPositions(start, map, visited));
        Set<Pair<Integer, Integer>> bestPathPositions = new HashSet<>();
        while (!queue.isEmpty()) {
            final Position current = queue.remove();
            final Tile currentTile = map.get(current.position().a()).get(current.position().b());
            if (currentTile == Tile.END) {
                if (current.score() < minimumScore) {
                    minimumScore = current.score();
                    bestPathPositions = current.previousPositions();
                    bestPathPositions.add(current.position());
                }
            } else {
                if (currentTile != Tile.WALL) {
                    queue.addAll(getNextPositions(current, map, visited));
                }
            }
        }
        return Pair.of(minimumScore, bestPathPositions.size());
    }

    private static Collection<Position> getNextPositions(final Position current, final List<List<Tile>> map, Map<Position, Position> visited) {
        return switch (current.direction()) {
            case NORTH ->
                    Stream.of(Position.of(Pair.of(current.position().a() - 1, current.position().b()), Direction.NORTH, current.score() + STEP_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.EAST, current.score() + TURN_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.WEST, current.score() + TURN_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.SOUTH, current.score() + TURN_SCORE * 2, current)).filter(position -> isValid(position, map, visited)).toList();
            case EAST ->
                    Stream.of(Position.of(Pair.of(current.position().a(), current.position().b() + 1), Direction.EAST, current.score() + STEP_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.SOUTH, current.score() + TURN_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.NORTH, current.score() + TURN_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.WEST, current.score() + TURN_SCORE * 2, current)).filter(position -> isValid(position, map, visited)).toList();
            case SOUTH ->
                    Stream.of(Position.of(Pair.of(current.position().a() + 1, current.position().b()), Direction.SOUTH, current.score() + STEP_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.WEST, current.score() + TURN_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.EAST, current.score() + TURN_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.NORTH, current.score() + TURN_SCORE * 2, current)).filter(position -> isValid(position, map, visited)).toList();
            case WEST ->
                    Stream.of(Position.of(Pair.of(current.position().a(), current.position().b() - 1), Direction.WEST, current.score() + STEP_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.NORTH, current.score() + TURN_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.SOUTH, current.score() + TURN_SCORE, current), Position.of(Pair.of(current.position().a(), current.position().b()), Direction.EAST, current.score() + TURN_SCORE * 2, current)).filter(position -> isValid(position, map, visited)).toList();
        };
    }

    private static boolean isValid(final Position position, final List<List<Tile>> map, Map<Position, Position> visited) {
        if (map.get(position.position().a()).get(position.position().b()) != Tile.WALL) {
            if (visited.containsKey(position)) {
                final Position before = visited.get(position);
                if (before.score() == position.score()) {
                    position.previousPositions().addAll(before.previousPositions());
                    visited.put(position, position);
                    return true;
                } else if (before.score() > position.score()) {
                    visited.put(position, position);
                    return true;
                }
            } else {
                visited.put(position, position);
                return true;
            }
        }
        return false;
    }

    private static Pair<Integer, Integer> findFirstTilePosition(final List<List<Tile>> map, final Tile toFind) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == toFind) {
                    return Pair.of(i, j);
                }
            }
        }
        throw new IllegalStateException(toFind + " not found");
    }

    private static void printMap(final List<List<Tile>> map) {
        for (List<Tile> tiles : map) {
            tiles.forEach(System.out::print);
            System.out.println();
        }
    }

    private static void printMap(final List<List<Tile>> map, final Pair<Integer, Integer> currentPosition) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (currentPosition.a() == i && currentPosition.b() == j) {
                    System.out.print("@");
                } else {
                    System.out.print(map.get(i).get(j));
                }
            }
            System.out.println();
        }
    }

    private static void printMap(List<List<Tile>> map, Set<Pair<Integer, Integer>> positions) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (positions.contains(Pair.of(i, j))) {
                    System.out.print("$");
                } else {
                    System.out.print(map.get(i).get(j));
                }
            }
            System.out.println();
        }
    }

    private static void printMap(List<List<Tile>> map, Pair<Integer, Integer> currentPosition, Map<Pair<Pair<Integer, Integer>, Direction>, Integer> visited) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (currentPosition.a() == i && currentPosition.b() == j) {
                    System.out.print("@");
                } else if (visited.containsKey(Pair.of(Pair.of(i, j), Direction.EAST)) || visited.containsKey(Pair.of(Pair.of(i, j), Direction.WEST)) || visited.containsKey(Pair.of(Pair.of(i, j), Direction.NORTH)) || visited.containsKey(Pair.of(Pair.of(i, j), Direction.SOUTH))) {
                    System.out.print("$");
                } else {
                    System.out.print(map.get(i).get(j));
                }
            }
            System.out.println();
        }
    }

    private enum Tile {
        START, END, WALL, EMPTY;

        private static Tile fromString(String tile) {
            return switch (tile) {
                case "S" -> START;
                case "E" -> END;
                case "#" -> WALL;
                case "." -> EMPTY;
                default -> throw new IllegalStateException("Unexpected tile: " + tile);
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case START -> "S";
                case END -> "E";
                case WALL -> "#";
                case EMPTY -> ".";
            };
        }
    }

    public enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    private record Position(Pair<Integer, Integer> position, Direction direction, int score,
                            Set<Pair<Integer, Integer>> previousPositions) {
        private static Position of(Pair<Integer, Integer> position, Direction direction, int score) {
            return new Position(position, direction, score, Set.of());
        }

        private static Position of(Pair<Integer, Integer> position, Direction direction, int score, Position previousPosition) {
            Set<Pair<Integer, Integer>> previousPositions = new HashSet<>(previousPosition.previousPositions());
            previousPositions.add(previousPosition.position());
            return new Position(position, direction, score, previousPositions);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Position position1 = (Position) o;
            return direction() == position1.direction() && Objects.equals(position(), position1.position());
        }

        @Override
        public int hashCode() {
            return Objects.hash(position(), direction());
        }
    }
}

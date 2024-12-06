package org.bohverkill.adventofcode2024;

import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 {
    public static void main(String[] args) {
//        final List<String> input = Utils.getInputList("/2024/Day6_example");
        final List<String> input = Utils.getInputList("/2024/Day6_input");
        task1(input);
        task2(input);
    }

    private static void task1(final List<String> input) {
        Position currentPosition = null;
        for (int i = 0; i < input.size(); i++) {
            final int j = input.get(i).indexOf('^');
            if (j != -1) {
                currentPosition = Position.of(i, j);
                break;
            }
        }
        if (currentPosition == null) {
            throw new IllegalArgumentException("No current position found");
        }

        Direction currentDirection = Direction.UP;
        Set<Position> visited = new HashSet<>();
        visited.add(currentPosition);
        while (true) {
//            printCurrent(input, currentPosition, currentDirection, visited);
            Position nextPosition = switch (currentDirection) {
                case UP -> Position.of(currentPosition.col() - 1, currentPosition.row());
                case RIGHT -> Position.of(currentPosition.col(), currentPosition.row() + 1);
                case DOWN -> Position.of(currentPosition.col() + 1, currentPosition.row());
                case LEFT -> Position.of(currentPosition.col(), currentPosition.row() - 1);
            };
            if (nextPosition.col() < 0 || nextPosition.col() >= input.size() || nextPosition.row() < 0 || nextPosition.row() >= input.getFirst().length()) {
                break;
            }
            final char c = input.get(nextPosition.col()).charAt(nextPosition.row());
            if (c != '.' && c != '^') {
                currentDirection = currentDirection.next();
            } else {
                visited.add(nextPosition);
                currentPosition = nextPosition;
            }
        }

        System.out.println("Part 1: " + visited.size());
    }

    private static void printCurrent(final List<String> input, final Position currentPosition, Direction currentDirection, final Set<Position> visited) {
        for (int i = 0; i < input.size(); i++) {
            String s = input.get(i);
            final int j = s.indexOf('^');
            if (j != -1) {
                s = StringUtils.replaceCharAt(s, j, '.');
            }
            for (Position position : visited) {
                if (i == position.col()) {
                    s = StringUtils.replaceCharAt(s, position.row(), 'X');
                }
            }
            if (i == currentPosition.col()) {
                s = StringUtils.replaceCharAt(s, currentPosition.row(), currentDirection.getChar());
            }
            System.out.println(s);
        }
        System.out.println("==========");
    }

    private static void task2(final List<String> input) {
        Position startPosition = null;
        for (int i = 0; i < input.size(); i++) {
            final int j = input.get(i).indexOf('^');
            if (j != -1) {
                startPosition = Position.of(i, j);
                break;
            }
        }
        if (startPosition == null) {
            throw new IllegalArgumentException("No current position found");
        }

        int sum = 0;
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.getFirst().length(); j++) {
                if (isLoop(new ArrayList<>(input), startPosition, Position.of(i, j))) {
                    sum++;
                }
            }
        }
        System.out.println("Part 2: " + sum);
    }

    private static boolean isLoop(final List<String> input, final Position startPosition, final Position obstruction) {
        input.set(obstruction.col(), StringUtils.replaceCharAt(input.get(obstruction.col()), obstruction.row(), 'O'));
        Position currentPosition = startPosition;
        Direction currentDirection = Direction.UP;
        Set<DirectionPosition> visited = new HashSet<>();
        while (true) {
//            printCurrent(input, currentPosition, currentDirection, visited);
            Position nextPosition = switch (currentDirection) {
                case UP -> Position.of(currentPosition.col() - 1, currentPosition.row());
                case RIGHT -> Position.of(currentPosition.col(), currentPosition.row() + 1);
                case DOWN -> Position.of(currentPosition.col() + 1, currentPosition.row());
                case LEFT -> Position.of(currentPosition.col(), currentPosition.row() - 1);
            };
            if (nextPosition.col() < 0 || nextPosition.col() >= input.size() || nextPosition.row() < 0 || nextPosition.row() >= input.getFirst().length()) {
                return false;
            }
            final char c = input.get(nextPosition.col()).charAt(nextPosition.row());
            if (c != '.' && c != '^') {
                currentDirection = currentDirection.next();
            } else {
                if (visited.contains(DirectionPosition.of(nextPosition, currentDirection))) {
                    return true;
                }
                visited.add(DirectionPosition.of(nextPosition, currentDirection));
                currentPosition = nextPosition;
            }
        }
    }

    private enum Direction {
        UP, RIGHT, DOWN, LEFT;

        private Direction next() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }

        private char getChar() {
            return switch (this) {
                case UP -> '^';
                case RIGHT -> '>';
                case DOWN -> 'v';
                case LEFT -> '<';
            };
        }
    }

    private record Position(int col, int row) {
        private static Position of(final int col, final int row) {
            return new Position(col, row);
        }
    }

    private record DirectionPosition(Position position, Direction direction) {
        private static DirectionPosition of(Position position, Direction direction) {
            return new DirectionPosition(position, direction);
        }
    }
}

package org.bohverkill.adventofcode2023;

import org.bohverkill.utils.Utils;

import java.util.*;

import static org.bohverkill.adventofcode2023.Day10Part1.Direction.*;

public class Day10Part1 {
    private static final String STARTING_POSITION_CHAR = "S";
//    private static final String GROUND_CHAR = ".";

    private static final Map<String, Type> PIPE_MAP = Map.of("|", Type.VERTICAL, "-", Type.HORIZONTAL, "L", Type.NINETY_DEGREE1, "J", Type.NINETY_DEGREE2, "7", Type.NINETY_DEGREE3, "F", Type.NINETY_DEGREE4);

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day10_1_1_example");
//        String input = Utils.getInput("/2023/Day10_1_2_example");
//        String input = Utils.getInput("/2023/Day10_1_3_example");
//        String input = Utils.getInput("/2023/Day10_1_4_example");
        String input = Utils.getInput("/2023/Day10_input");
        final String[] split = input.split("\n");
        final int lineLength = split[0].length();
        final Position start = findStart(input, lineLength + 1); // because of newlines
        final StartPipe startPipe = findStartPipes(split, lineLength, start);
        DirectionalPipe directionalPipe1 = startPipe.directionalPipe1;
        DirectionalPipe directionalPipe2 = startPipe.directionalPipe2;
        int steps = 1;
        while (!directionalPipe1.pipe.equals(directionalPipe2.pipe)) {
            directionalPipe1 = findNextPipe(split, directionalPipe1);
            directionalPipe2 = findNextPipe(split, directionalPipe2);
            steps++;
        }
        System.out.println("Day 10 Part 1: " + steps);
    }

    public static Position findStart(String input, int lineLength) {
        final int position = input.indexOf(STARTING_POSITION_CHAR);
        if (position == -1) {
            throw new IllegalStateException("No start: " + input);
        }
        return new Position(position / lineLength, position % lineLength);
    }

    public static StartPipe findStartPipes(String[] input, int lineLength, Position start) {
        List<DirectionalPipe> startPipes = new ArrayList<>();
        for (Type value : PIPE_MAP.values()) {
            final Pipe startPipe = new Pipe(value, start.row(), start.column());
            startPipes.clear();
            for (Direction direction : startPipe.type().connecting()) {
                final Position nextPosition = getNextPosition(direction, startPipe);
                if (nextPosition.isValid(input.length, lineLength)) {
                    final Optional<Pipe> nextPipe = Day10Part1.extractPipe(input, nextPosition);
                    if (nextPipe.isPresent()) {
                        final Optional<Direction> next = getNext(nextPipe.get().type().connecting(), direction);
                        if (next.isPresent()) {
                            startPipes.add(new DirectionalPipe(nextPipe.get(), direction));
                        }
                    }
                }
            }
            if (startPipes.size() == 2) {
                return new StartPipe(startPipe, startPipes.get(0), startPipes.get(1));
            }
        }
        throw new IllegalStateException("No next pipes found: " + Arrays.toString(input));
    }

    public static DirectionalPipe findNextPipe(String[] input, DirectionalPipe current) {
        final Optional<Direction> next = getNext(current.pipe.type.connecting, current.previousDirection);
        if (next.isEmpty()) {
            throw new IllegalStateException("No next direction: " + Arrays.toString(current.pipe.type.connecting));
        }
        final Position nextPosition = getNextPosition(next.get(), current.pipe);
        final Optional<Pipe> nextPipe = extractPipe(input, nextPosition);
        if (nextPipe.isEmpty()) {
            throw new IllegalStateException("Invalid next position: " + nextPosition);
        }
        return new DirectionalPipe(nextPipe.get(), next.get());
    }

    private static Optional<Direction> getNext(Direction[] directions, Direction previous) {
        final List<Direction> list = Arrays.stream(directions).filter(direction -> !direction.equals(previous.getNextDirection())).toList();
        if (list.size() == 1) {
            return Optional.of(list.getFirst());
        } else {
            return Optional.empty();
        }
    }

    private static Position getNextPosition(Direction direction, Pipe current) {
        return switch (direction) {
            case NORTH -> new Position(current.row - 1, current.column);
            case EAST -> new Position(current.row, current.column + 1);
            case SOUTH -> new Position(current.row + 1, current.column);
            case WEST -> new Position(current.row, current.column - 1);
        };
    }

    private static Optional<Pipe> extractPipe(String[] input, Position position) {
        final String type = String.valueOf(input[position.row].charAt(position.column));
        if (PIPE_MAP.containsKey(type)) {
            return Optional.of(new Pipe(PIPE_MAP.get(type), position.row, position.column));
        } else {
            return Optional.empty();
        }
    }

    public enum Type {
        VERTICAL(new Direction[]{NORTH, SOUTH}), HORIZONTAL(new Direction[]{EAST, WEST}), NINETY_DEGREE1(new Direction[]{NORTH, EAST}), NINETY_DEGREE2(new Direction[]{NORTH, WEST}), NINETY_DEGREE3(new Direction[]{SOUTH, WEST}), NINETY_DEGREE4(new Direction[]{SOUTH, EAST});

        private final Direction[] connecting;

        Type(Direction[] connecting) {
            this.connecting = connecting;
        }

        public Direction[] connecting() {
            return this.connecting;
        }
    }

    public enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public Direction getNextDirection() {
            return switch (this) {
                case NORTH -> SOUTH;
                case EAST -> WEST;
                case SOUTH -> NORTH;
                case WEST -> EAST;
            };
        }
    }

    public record DirectionalPipe(Pipe pipe, Direction previousDirection) {
    }

    public record StartPipe(Pipe pipe, DirectionalPipe directionalPipe1, DirectionalPipe directionalPipe2) {
    }

    public record Pipe(Type type, int row, int column) {
    }

    public record Position(int row, int column) {
        public boolean isValid(int maxRow, int maxColumn) {
            return row >= 0 && row < maxRow && column >= 0 && column < maxColumn;
        }
    }
}

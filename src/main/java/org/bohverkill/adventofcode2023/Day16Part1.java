package org.bohverkill.adventofcode2023;


import java.util.*;

public class Day16Part1 {
    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day16_example");
        String input = Utils.getInput("/2023/Day16_input");
        final String[] grid = Utils.getLines(input);
        final int count = solve(grid);
        System.out.println("Day 16 Part 1: " + count);
    }

    public static int solve(String[] grid) {
        Map<Tile, Set<Direction>> energizedGrid = new HashMap<>();
        followBeam(grid, energizedGrid, new Beam(0, 0, Direction.RIGHT));
        return energizedGrid.size();
    }

    public static void followBeam(String[] grid, Map<Tile, Set<Direction>> energizedGrid, Beam beam) {
        if (beam.isNotValid(grid.length, grid[0].length())) {
            return;
        }
        final Set<Direction> directions = energizedGrid.get(beam.tile());
        if (directions != null) {
            if (directions.contains(beam.direction())) {
                // we already have visited this direction on that tile
                return;
            } else {
                directions.add(beam.direction());
            }
        } else {
            energizedGrid.put(beam.tile(), new HashSet<>(List.of(beam.direction())));
        }
        final char currentChar = grid[beam.row()].charAt(beam.column());
        switch (currentChar) {
            case '.' -> followBeam(grid, energizedGrid, beam.next(beam.direction()));
            case '/' -> followBeam(grid, energizedGrid, beam.next(switch (beam.direction()) {
                case RIGHT -> Direction.UP;
                case LEFT -> Direction.DOWN;
                case DOWN -> Direction.LEFT;
                case UP -> Direction.RIGHT;
            }));
            case '\\' -> followBeam(grid, energizedGrid, beam.next(switch (beam.direction()) {
                case RIGHT -> Direction.DOWN;
                case LEFT -> Direction.UP;
                case DOWN -> Direction.RIGHT;
                case UP -> Direction.LEFT;
            }));
            case '|' -> {
                switch (beam.direction()) {
                    case RIGHT, LEFT -> {
                        followBeam(grid, energizedGrid, beam.next(Direction.UP));
                        followBeam(grid, energizedGrid, beam.next(Direction.DOWN));
                    }
                    case DOWN -> followBeam(grid, energizedGrid, beam.next(Direction.DOWN));
                    case UP -> followBeam(grid, energizedGrid, beam.next(Direction.UP));
                }
            }
            case '-' -> {
                switch (beam.direction()) {
                    case RIGHT -> followBeam(grid, energizedGrid, beam.next(Direction.RIGHT));
                    case LEFT -> followBeam(grid, energizedGrid, beam.next(Direction.LEFT));
                    case DOWN, UP -> {
                        followBeam(grid, energizedGrid, beam.next(Direction.RIGHT));
                        followBeam(grid, energizedGrid, beam.next(Direction.LEFT));
                    }
                }
            }
            default -> throw new IllegalStateException("Unrecognised character: " + currentChar);
        }
    }

    public enum Direction {
        RIGHT, LEFT, DOWN, UP
    }

    public record Beam(int row, int column, Direction direction) {
        public Beam next(Direction nextDirection) {
            return switch (nextDirection) {
                case RIGHT -> new Beam(row, column + 1, Direction.RIGHT);
                case LEFT -> new Beam(row, column - 1, Direction.LEFT);
                case DOWN -> new Beam(row + 1, column, Direction.DOWN);
                case UP -> new Beam(row - 1, column, Direction.UP);
            };
        }

        private boolean isNotValid(int rowSize, int columnSize) {
            return row() < 0 || row() >= rowSize || column() < 0 || column() >= columnSize;
        }

        public Tile tile() {
            return new Tile(row(), column());
        }
    }

    public record Tile(int row, int column) {
    }
}

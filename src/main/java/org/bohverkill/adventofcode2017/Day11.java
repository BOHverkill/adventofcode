package org.bohverkill.adventofcode2017;

import org.bohverkill.utils.Utils;

import java.util.List;

public class Day11 {
    public static void main(String[] args) {
        final List<Direction> directions = Utils.getSplit(Utils.getInput("/2017/Day11_input"), ",").map(Direction::parse).toList();
        task1(directions);
        task2(directions);
    }

    private static void task1(final List<Direction> directions) {
        final Hexagon start = Hexagon.of(0, 0);
        Hexagon current = start;
        for (Direction direction : directions) {
            current = step(current, direction);
        }

        int distance = distance(current, start);
        System.out.println("Part 1: " + distance);
    }

    // Source: https://stackoverflow.com/a/5085274
    private static Hexagon step(Hexagon current, Direction direction) {
        return switch (direction) {
            case NORTH -> Hexagon.of(current.x() - 1, current.y());
            case NORTHEAST -> Hexagon.of(current.x() - 1, current.y() + 1);
            case SOUTHEAST -> Hexagon.of(current.x(), current.y() + 1);
            case SOUTH -> Hexagon.of(current.x() + 1, current.y());
            case SOUTHWEST -> Hexagon.of(current.x() + 1, current.y() - 1);
            case NORTHWEST -> Hexagon.of(current.x(), current.y() - 1);
        };
    }

    private static int distance(final Hexagon h0, final Hexagon h1) {
        int dx = h1.x() - h0.x();
        int dy = h1.y() - h0.y();

        if (Math.signum(dx) == Math.signum(dy)) {
            return Math.abs(dx + dy);
        } else {
            return Math.max(Math.abs(dx), Math.abs(dy));
        }
    }

    private static void task2(final List<Direction> directions) {
        final Hexagon start = Hexagon.of(0, 0);
        Hexagon current = start;
        int maxDistance = Integer.MIN_VALUE;
        for (Direction direction : directions) {
            current = step(current, direction);
            maxDistance = Math.max(maxDistance, distance(current, start));
        }

        System.out.println("Part 2: " + maxDistance);
    }

    private enum Direction {
        NORTH, NORTHEAST, SOUTHEAST, SOUTH, SOUTHWEST, NORTHWEST;

        private static Direction parse(String direction) {
            return switch (direction.trim()) {
                case "n" -> NORTH;
                case "ne" -> NORTHEAST;
                case "se" -> SOUTHEAST;
                case "s" -> SOUTH;
                case "sw" -> SOUTHWEST;
                case "nw" -> NORTHWEST;
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            };
        }
    }

    private record Hexagon(int x, int y) {
        private static Hexagon of(final int x, final int y) {
            return new Hexagon(x, y);
        }
    }
}

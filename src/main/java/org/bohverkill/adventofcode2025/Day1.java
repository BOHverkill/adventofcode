package org.bohverkill.adventofcode2025;

import org.bohverkill.utils.MathUtils;
import org.bohverkill.utils.Utils;

import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 {
    private static final Pattern ROTATION_PATTERN = Pattern.compile("^([LR])(\\d+)$");
    private static final int DIAL = 50;
    private static final int MAX_NUMBERS = 100;

    static void main() {
//        final String name = "/2025/Day1_example";
        final String name = "/2025/Day1_input";
        final List<Rotation> directions = Utils.getInputLines(name).map(Day1::parseRotation).toList();
        task1(directions);
        task2(directions);
    }

    private static void task1(final List<Rotation> directions) {
        int dial = DIAL;
        int zeros = 0;
        for (Rotation direction : directions) {
            dial = Math.floorMod(direction.direction().op().applyAsInt(dial, direction.distance()), MAX_NUMBERS);
            if (dial == 0) {
                zeros++;
            }
        }
        System.out.println("Part 1: " + zeros);
    }

    private static void task2(final List<Rotation> directions) {
        int dial = DIAL;
        int zeros = 0;
        for (Rotation direction : directions) {
            int newDialPosition = direction.direction().op().applyAsInt(dial, direction.distance());
            int oldDialPosition = dial;
            dial = Math.floorMod(newDialPosition, MAX_NUMBERS);
            if (newDialPosition >= MAX_NUMBERS) {
                zeros += newDialPosition / MAX_NUMBERS;
            } else if (newDialPosition < 0) {
                if (oldDialPosition == 0) {
                    zeros -= 1;
                }
                zeros += Math.abs(newDialPosition / MAX_NUMBERS) + 1;
            } else if (dial == 0) {
                zeros++;
            }
        }
        System.out.println("Part 2: " + zeros);
    }

    private static Rotation parseRotation(String line) {
        Matcher rotationMatcher = Utils.getMatcher(ROTATION_PATTERN, line);

        final Direction direction = Direction.of(rotationMatcher.group(1));
        final int distance = Integer.parseInt(rotationMatcher.group(2));
        return Rotation.of(direction, distance);
    }

    private enum Direction {
        LEFT(MathUtils::minus), RIGHT(MathUtils::plus);

        private final IntBinaryOperator op;

        Direction(IntBinaryOperator op) {
            this.op = op;
        }

        public static Direction of(String direction) {
            return switch (direction) {
                case "L" -> LEFT;
                case "R" -> RIGHT;
                default -> throw new IllegalArgumentException("Unknown direction: " + direction);
            };
        }

        public IntBinaryOperator op() {
            return this.op;
        }
    }

    private record Rotation(Direction direction, int distance) {
        public static Rotation of(Direction direction, int distance) {
            return new Rotation(direction, distance);
        }
    }
}

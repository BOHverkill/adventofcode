package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.bohverkill.adventofcode2023.Day18Part1.Direction;
import static org.bohverkill.adventofcode2023.Day18Part1.Trench;

public class Day18Part2 {
    public static void main(String[] args) {
//        Stream<String> input = Utils.getInputLines("/2023/Day18_example");
        Stream<String> input = Utils.getInputLines("/2023/Day18_input");
//        final List<Trench> trenches = input.map(Day18Part1::mapToTrench).toList();
        final List<Trench> trenches = input.map(Day18Part1::mapToTrench).map(Day18Part2::swap).toList();
        final Point start = new Point(0, 0);
        List<Point> points = new ArrayList<>();
        long boundary = 0;
        Point currentPosition = start;
        for (Trench trench : trenches) {
            currentPosition = dig(currentPosition, trench);
            points.add(currentPosition);
            boundary += trench.number();
        }
        final long area = calculateArea(points);
        final long picks = calculatePicks(area, boundary);
        System.out.println("Day 18 Part 2: " + picks);
    }

    public static Trench swap(Trench trench) {
        final String color = trench.color();
        final int number = Integer.parseInt(color.substring(0, 5), 16);
        final Direction direction = switch (color.substring(5)) {
            case "0" -> Direction.RIGHT;
            case "1" -> Direction.DOWN;
            case "2" -> Direction.LEFT;
            case "3" -> Direction.UP;
            default -> throw new IllegalStateException("Unexpected value: " + color.substring(5));
        };

        return new Trench(direction, number, null);
    }

    // https://en.wikipedia.org/wiki/Shoelace_formula
    public static long calculateArea(List<Point> points) {
        int n = points.size();
        long area = IntStream.range(0, n).mapToLong(i -> points.get(i).x() * points.get((i + 1) % n).y() - points.get((i + 1) % n).x() * points.get(i).y()).sum();
        return Math.abs(area) / 2;
    }

    // https://en.wikipedia.org/wiki/Pick's_theorem that is rearranged to find i+b
    private static long calculatePicks(long area, long boundary) {
        return area + 1 + boundary / 2;
    }

    public static Point dig(Point point, Trench trench) {
        // this is upside down, but it does not matter for the area
        return switch (trench.direction()) {
            case UP -> new Point(point.x() - trench.number(), point.y());
            case DOWN -> new Point(point.x() + trench.number(), point.y());
            case LEFT -> new Point(point.x(), point.y() - trench.number());
            case RIGHT -> new Point(point.x(), point.y() + trench.number());
        };
    }

    public record Point(long x, long y) {
    }
}

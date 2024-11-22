package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day24Part1 {
    private static final Pattern HAILSTONE_PATTERN = Pattern.compile("^(-?\\d+), +(-?\\d+), +(-?\\d+) +@ +(-?\\d+), +(-?\\d+), +(-?\\d+)$");

//    private static final long WINDOW_FROM = 7L;
//    private static final long WINDOW_TO = 27L;
    private static final long WINDOW_FROM = 200000000000000L;
    private static final long WINDOW_TO = 400000000000000L;

    public static void main(String[] args) {
//        final Stream<String> lines = Utils.getInputLines("/2023/Day24_example");
        final Stream<String> lines = Utils.getInputLines("/2023/Day24_input");

        final List<Hailstone> hailstones = lines.map(Day24Part1::parse).toList();
        final List<Pair<Hailstone, Hailstone>> pairs = formPairs(hailstones);
        final long count = pairs.stream().filter(Day24Part1::solve).count();
        System.out.println("Day 24 Part 1: " + count);
    }

    public static boolean solve(Pair<Hailstone, Hailstone> pair) {
        final Vector2D a = pair.a().vector2D();
        final Vector2D b = pair.b().vector2D();

        final Optional<Point2D> intersection = intersection(a, b);
        if (intersection.isPresent()) {
            final Point2D p = intersection.get();

            return !isInPast(a, b, p) && min(p.x(), p.y()) >= WINDOW_FROM && max(p.x(), p.y()) <= WINDOW_TO;
        } else {
            return false;
        }
    }

    // source: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_line_equations
    public static Optional<Point2D> intersection(Vector2D v1, Vector2D v2) {
        final double a = v1.direction().y() / v1.direction().x(); // slope/gradient
        final double b = v2.direction().y() / v2.direction().x(); // slope/gradient
        final double c = v1.base().y() - a * v1.base().x(); // y-intercept
        final double d = v2.base().y() - b * v2.base().x(); // y-intercept

        if (a == b) {
            // parallel
            if (c != d) {
                return Optional.empty();
            } else {
                throw new IllegalStateException("Identical lines");
            }
        }

        final double x = (d - c) / (a - b);
        final double y = a * x + c;
        return Optional.of(new Point2D(x, y));
    }

    private static boolean isInPast(Vector2D a, Vector2D b, Point2D p) {
        return isInPast(a, p) || isInPast(b, p);
    }

    private static boolean isInPast(Vector2D v, Point2D p) {
        return v.direction().x() >= 0 && p.x() < v.base().x() || v.direction().x() < 0 && p.x() >= v.base().x();
    }

    public static double min(Double... doubles) {
        return calc(doubles, Math::min, Double.MAX_VALUE);
    }

    public static double max(Double... doubles) {
        return calc(doubles, Math::max, Double.MIN_VALUE);
    }

    private static <T> T calc(T[] values, BinaryOperator<T> op, T initialValue) {
        T value = initialValue;
        for (T i : values) {
            value = op.apply(value, i);
        }
        return value;
    }

    public static <V> List<Pair<V, V>> formPairs(List<V> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        final V first = list.getFirst();
        final List<V> subList = list.subList(1, list.size());

        return Stream.concat(subList.stream().map(second -> new Pair<>(first, second)), formPairs(subList).stream()).toList();
    }

    public static <V> List<Pair<V, V>> formPairs(List<V> firstList, List<V> secondList) {
        if (firstList.isEmpty() || secondList.isEmpty()) {
            return Collections.emptyList();
        }
        final V first = firstList.getFirst();
        final List<V> subFirstList = firstList.subList(1, firstList.size());

        return Stream.concat(secondList.stream().map(second -> new Pair<>(first, second)), formPairs(subFirstList, secondList).stream()).toList();
    }

    public static Hailstone parse(String line) {
        final Matcher hailstoneMatcher = Utils.getMatcher(HAILSTONE_PATTERN, line);

        final long px = Long.parseLong(hailstoneMatcher.group(1));
        final long py = Long.parseLong(hailstoneMatcher.group(2));
        final long pz = Long.parseLong(hailstoneMatcher.group(3));
        final long vx = Long.parseLong(hailstoneMatcher.group(4));
        final long vy = Long.parseLong(hailstoneMatcher.group(5));
        final long vz = Long.parseLong(hailstoneMatcher.group(6));

        return new Hailstone(px, py, pz, vx, vy, vz);
    }

    public record Point2D(double x, double y) {
    }

    public record Vector2D(Point2D base, Point2D direction) {

    }

    public record Hailstone(long px, long py, long pz, long vx, long vy, long vz) {
        public Vector2D vector2D() {
            return new Vector2D(new Point2D(px(), py()), new Point2D(vx(), vy()));
        }
    }

    public record Pair<A, B>(A a, B b) {

    }
}

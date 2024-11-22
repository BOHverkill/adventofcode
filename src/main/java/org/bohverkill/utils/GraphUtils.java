package org.bohverkill.utils;

import org.bohverkill.models.Cell;
import org.bohverkill.models.Point2D;
import org.bohverkill.models.Vector2D;

import java.util.Optional;

public final class GraphUtils {

    private GraphUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static float euclideanDistance(float x1, float x2, float y1, float y2) {
        return Math.round(Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2)));
    }

    public static double euclideanDistance(double x1, double x2, double y1, double y2) {
        return Math.hypot(Math.abs(y2 - y1), Math.abs(x2 - x1));
    }

    public static int euclideanDistance(int x1, int x2, int y1, int y2) {
        return (int) Math.round(Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2)));
    }

    public static long euclideanDistance(long x1, long x2, long y1, long y2) {
        return Math.round(Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2)));
    }

    public static int euclideanDistance(Cell cell1, Cell cell2) {
//        return euclideanDistance(cell1.row(), cell1.column(), cell2.row(), cell2.column());
        return (int) Math.round(Math.sqrt(Math.pow((cell2.column() - cell1.column()), 2) + Math.pow((cell2.row() - cell1.row()), 2)));
    }

    public static float manhattanDistance(float x1, float x2, float y1, float y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    public static double manhattanDistance(double x1, double x2, double y1, double y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    public static int manhattanDistance(int x1, int x2, int y1, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    public static long manhattanDistance(long x1, long x2, long y1, long y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    public static int manhattanDistance(Cell cell1, Cell cell2) {
//        return manhattanDistance(cell1.row(), cell1.column(), cell2.row(), cell2.column());
        return Math.abs(cell2.row() - cell1.row()) + Math.abs(cell2.column() - cell1.column());
    }

    // source: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_line_equations
    public static Optional<Point2D> lineIntersection(Vector2D v1, Vector2D v2) {
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
}

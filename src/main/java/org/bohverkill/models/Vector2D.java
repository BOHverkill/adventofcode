package org.bohverkill.models;

public record Vector2D(Point2D base, Point2D direction) {
    public static Vector2D of(Point2D base, Point2D direction) {
        return new Vector2D(base, direction);
    }

    public Vector2D fromCoordinates(double px, double py, double vx, double vy) {
        return new Vector2D(new Point2D(px, py), new Point2D(vx, vy));
    }
}

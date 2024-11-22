package org.bohverkill.models;

public record Vector3D(Point3D base, Point3D direction) {
    public static Vector3D of(Point3D base, Point3D direction) {
        return new Vector3D(base, direction);
    }

    public Vector3D fromCoordinates(double px, double py, double pz, double vx, double vy, double vz) {
        return new Vector3D(new Point3D(px, py, pz), new Point3D(vx, vy, vz));
    }
}

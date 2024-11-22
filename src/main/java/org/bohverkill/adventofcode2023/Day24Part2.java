package org.bohverkill.adventofcode2023;


import java.util.List;
import java.util.stream.Stream;

// Source: https://github.com/ash42/adventofcode/blob/fc475dfaea893c652a1d9aec0d765150bc5dcb5a/adventofcode2023/src/nl/michielgraat/adventofcode2023/day24/Day24.java
public class Day24Part2 {
    public static void main(String[] args) {
//        final Stream<String> lines = Utils.getInputLines("/2023/Day24_example");
        final Stream<String> lines = Utils.getInputLines("/2023/Day24_input");

        final List<Day24Part1.Hailstone> hailstones = lines.map(Day24Part1::parse).toList();
        final Day24Part1.Hailstone stone = solve(hailstones);
        System.out.println(stone.px() + stone.py() + stone.pz());
    }

    public static Day24Part1.Hailstone solve(List<Day24Part1.Hailstone> hailstones) {
        double[][] coefficientMatrix = new double[4][4];
        double[] rhs = new double[4];
        for (int i = 0; i < 4; i++) {
            final Day24Part1.Hailstone h1 = hailstones.get(i);
            final Day24Part1.Hailstone h2 = hailstones.get(i + 1);
            coefficientMatrix[i][0] = h2.vy() - (double) h1.vy(); // X
            coefficientMatrix[i][1] = h1.vx() - (double) h2.vx(); // Y
            coefficientMatrix[i][2] = h1.py() - (double) h2.py(); // VX
            coefficientMatrix[i][3] = h2.px() - (double) h1.px(); // VY
            rhs[i] = -h1.px() * h1.vy() + h1.py() * h1.vx() + h2.px() * h2.vy() - (double) h2.py() * h2.vx(); // rhs
        }
        gaussianElimination(coefficientMatrix, rhs);

        long x = Math.round(rhs[0]);
        long y = Math.round(rhs[1]);
        long vx = Math.round(rhs[2]);
        long vy = Math.round(rhs[3]);

        coefficientMatrix = new double[2][2];
        rhs = new double[2];
        for (int i = 0; i < 2; i++) {
            final Day24Part1.Hailstone h1 = hailstones.get(i);
            final Day24Part1.Hailstone h2 = hailstones.get(i + 1);
            coefficientMatrix[i][0] = h1.vx() - (double) h2.vx(); // Z
            coefficientMatrix[i][1] = h2.px() - (double) h1.px(); // VZ
            rhs[i] = -h1.px() * h1.vz() + h1.pz() * h1.vx() + h2.px() * h2.vz() - h2.pz() * h2.vx() - (h2.vz() - h1.vz()) * x - (double) (h1.pz() - h2.pz()) * vx; // rhs
        }
        gaussianElimination(coefficientMatrix, rhs);

        long z = Math.round(rhs[0]);
        long vz = Math.round(rhs[1]);

        return new Day24Part1.Hailstone(x, y, z, vx, vy, vz);
    }

    // Source: https://www.cs.emory.edu/~cheung/Courses/170/Syllabus/09/gaussian-elim.html
    public static void gaussianElimination(double[][] coefficientMatrix, double[] rhs) {
        int numberUnknowns = rhs.length;

        for (int i = 0; i < numberUnknowns; i++) {
            // Select pivot
            double pivot = coefficientMatrix[i][i];
            // Normalize row i
            for (int j = 0; j < numberUnknowns; j++) {
                coefficientMatrix[i][j] = coefficientMatrix[i][j] / pivot;
            }

            rhs[i] = rhs[i] / pivot;

            // Sweep using row i
            for (int k = 0; k < numberUnknowns; k++) {
                if (k != i) {
                    double factor = coefficientMatrix[k][i];
                    for (int j = 0; j < numberUnknowns; j++)
                        coefficientMatrix[k][j] = coefficientMatrix[k][j] - factor * coefficientMatrix[i][j];
                    rhs[k] = rhs[k] - factor * rhs[i];
                }
            }
        }
        // result in rhs
    }
}

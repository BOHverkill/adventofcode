package org.bohverkill.adventofcode2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bohverkill.adventofcode2023.Day10Part1.*;

public class Day10Part2 {
    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day10_2_1_example");
//        String input = Utils.getInput("/2023/Day10_2_2_example");
//        String input = Utils.getInput("/2023/Day10_2_3_example");
//        String input = Utils.getInput("/2023/Day10_2_4_example");
        String input = Utils.getInput("/2023/Day10_input");
        final String[] split = input.split("\n");
        final int lineLength = split[0].length();
        final Position start = Day10Part1.findStart(input, lineLength + 1); // because of newlines
        final StartPipe startPipe = Day10Part1.findStartPipes(split, lineLength, start);
        DirectionalPipe directionalPipe1 = startPipe.directionalPipe1();
        DirectionalPipe directionalPipe2 = startPipe.directionalPipe2();
        List<Pipe> loop = new ArrayList<>();
        loop.add(startPipe.pipe());
        loop.add(directionalPipe1.pipe());
        loop.add(directionalPipe2.pipe());
        while (!directionalPipe1.pipe().equals(directionalPipe2.pipe())) {
            directionalPipe1 = Day10Part1.findNextPipe(split, directionalPipe1);
            directionalPipe2 = Day10Part1.findNextPipe(split, directionalPipe2);
            loop.addFirst(directionalPipe1.pipe());
            loop.add(directionalPipe2.pipe());
        }
        loop.removeLast();
        final Set<Position> loopPositions = loop.stream().map(pipe -> new Position(pipe.row(), pipe.column())).collect(Collectors.toSet());
        final int[][] loopPolygon = loop.stream().filter(pipe -> !pipe.type().equals(Type.VERTICAL) && !pipe.type().equals(Type.HORIZONTAL)).map(pipe -> new int[]{pipe.row(), pipe.column()}).toArray(int[][]::new);
        final List<Position> inside = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            for (int j = 0; j < lineLength; j++) {
                final Position candidate = new Position(i, j);
                if (!loopPositions.contains(candidate) && contains(loopPolygon, new double[]{candidate.row(), candidate.column()})) {
                    inside.add(candidate);
                }
            }
        }
//        prettyPrint(split);
//        printInside(split, inside);
        System.out.println("Day 10 Part 2: " + inside.size());
    }

    // from https://rosettacode.org/wiki/Ray-casting_algorithm#Java
    private static boolean intersects(int[] A, int[] B, double[] P) {
        if (A[1] > B[1]) return intersects(B, A, P);

        if (P[1] == A[1] || P[1] == B[1]) P[1] += 0.0001;

        if (P[1] > B[1] || P[1] < A[1] || P[0] >= Math.max(A[0], B[0])) return false;

        if (P[0] < Math.min(A[0], B[0])) return true;

        double red = (P[1] - A[1]) / (P[0] - A[0]);
        double blue = (B[1] - A[1]) / (double) (B[0] - A[0]);
        return red >= blue;
    }

    private static boolean contains(int[][] shape, double[] pnt) {
        boolean inside = false;
        int len = shape.length;
        for (int i = 0; i < len; i++) {
            if (intersects(shape[i], shape[(i + 1) % len], pnt)) inside = !inside;
        }
        return inside;
    }

    private static void printInside(String[] input, List<Position> inside) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length(); j++) {
                if (inside.contains(new Position(i, j))) {
                    System.out.print("\033[0;1m" + "I" + "\033[0;0m");
                } else {
//                    System.out.print(input[i].charAt(j));
                    System.out.print(prettyChar(input[i].charAt(j)));
                }
            }
            System.out.println();
        }
    }

    private static char prettyChar(char c) {
        return switch (c) {
            case '|' -> '│';
            case '-' -> '─';
            case 'L' -> '└';
            case 'J' -> '┘';
            case '7' -> '┐';
            case 'F' -> '┌';
            case '.' -> ' ';
            case 'S' -> 'S';
            case 'I' -> 'I';
            case 'O' -> 'O';
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    private static void prettyPrint(String[] input) {
        for (String s : input) {
            for (int j = 0; j < s.length(); j++) {
                System.out.print(prettyChar(s.charAt(j)));
            }
            System.out.println();
        }
    }
}

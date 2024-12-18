package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.GraphUtils;
import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Stream;

public class Day18 {
    //        public static final int MEMORY_SPACE_SIZE = 7;
//    public static final int BYTES_COUNT = 12;
    public static final int MEMORY_SPACE_SIZE = 71;
    public static final int BYTES_COUNT = 1024;

    public static void main(String[] args) {
//        final String path = "/2024/Day18_example";
        final String path = "/2024/Day18_input";
        final List<Pair<Integer, Integer>> input = Utils.getInputLines(path).map(string -> string.split(",")).map(strings -> Pair.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]))).toList();
        task1(input);
        task2(input);
    }

    private static void task1(final List<Pair<Integer, Integer>> input) {
        final boolean[][] memorySpace = new boolean[MEMORY_SPACE_SIZE][MEMORY_SPACE_SIZE];

        for (int i = 0; i < BYTES_COUNT; i++) {
            final Pair<Integer, Integer> integerIntegerPair = input.get(i);
            memorySpace[integerIntegerPair.b()][integerIntegerPair.a()] = true;
        }

        int res = findPath(memorySpace);
        System.out.println("Part 1: " + res);
    }

    private static void task2(final List<Pair<Integer, Integer>> input) {
        final boolean[][] memorySpace = new boolean[MEMORY_SPACE_SIZE][MEMORY_SPACE_SIZE];

        int i;
        for (i = 0; i < BYTES_COUNT; i++) {
            final Pair<Integer, Integer> integerIntegerPair = input.get(i);
            memorySpace[integerIntegerPair.b()][integerIntegerPair.a()] = true;
        }

        Pair<Integer, Integer> integerIntegerPair = null;
        while (true) {
            int res = findPath(memorySpace);
            if (res == Integer.MAX_VALUE) {
                break;
            }
            integerIntegerPair = input.get(i);
            memorySpace[integerIntegerPair.b()][integerIntegerPair.a()] = true;
            i++;
        }
        System.out.println("Part 2: " + integerIntegerPair.a() + "," + integerIntegerPair.b());
    }

    private static int findPath(final boolean[][] memorySpace) {
        final Pair<Integer, Integer> startPosition = new Pair<>(0, 0);
        final Pair<Integer, Integer> endPosition = new Pair<>(MEMORY_SPACE_SIZE - 1, MEMORY_SPACE_SIZE - 1);
        int minimumSteps = Integer.MAX_VALUE;

        Map<Pair<Integer, Integer>, Integer> visited = new HashMap<>();
        visited.put(startPosition, 0);
        Queue<Pair<Pair<Integer, Integer>, Integer>> queue = new PriorityQueue<>(Comparator.comparingInt(o -> GraphUtils.manhattanDistance(o.a().a(), o.a().b(), endPosition.a(), endPosition.b())));
        queue.addAll(getNextPositions(Pair.of(startPosition, 0), memorySpace, visited));
        while (!queue.isEmpty()) {
            final Pair<Pair<Integer, Integer>, Integer> current = queue.remove();
            final boolean currentSpace = memorySpace[current.a().b()][current.a().a()];
            if (current.a().equals(endPosition)) {
                if (current.b() < minimumSteps) {
                    minimumSteps = current.b();
                }
            } else {
                if ((minimumSteps == Integer.MAX_VALUE || current.b() < minimumSteps) && !currentSpace && visited.getOrDefault(current.a(), Integer.MAX_VALUE) > current.b()) {
                    visited.put(current.a(), current.b());
                    queue.addAll(getNextPositions(current, memorySpace, visited));
                }
            }
        }
        return minimumSteps;
    }

    private static Collection<Pair<Pair<Integer, Integer>, Integer>> getNextPositions(Pair<Pair<Integer, Integer>, Integer> current, final boolean[][] memorySpace, Map<Pair<Integer, Integer>, Integer> visited) {
        return Stream.of(Pair.of(Pair.of(current.a().a() + 1, current.a().b()), current.b() + 1), Pair.of(Pair.of(current.a().a() - 1, current.a().b()), current.b() + 1), Pair.of(Pair.of(current.a().a(), current.a().b() + 1), current.b() + 1), Pair.of(Pair.of(current.a().a(), current.a().b() - 1), current.b() + 1)).filter(pair -> isValid(pair, memorySpace, visited)).toList();
    }

    private static boolean isValid(Pair<Pair<Integer, Integer>, Integer> pair, final boolean[][] memorySpace, Map<Pair<Integer, Integer>, Integer> visited) {
        return pair.a().a() >= 0 && pair.a().a() < MEMORY_SPACE_SIZE && pair.a().b() >= 0 && pair.a().b() < MEMORY_SPACE_SIZE && !memorySpace[pair.a().b()][pair.a().a()] && visited.getOrDefault(pair.a(), Integer.MAX_VALUE) > pair.b();
    }

    private static void printMemorySpace(boolean[][] memorySpace) {
        for (boolean[] booleans : memorySpace) {
            for (boolean aBoolean : booleans) {
                System.out.print(aBoolean ? "#" : ".");
            }
            System.out.println();
        }
        System.out.println("====");
    }

    private static void printMemorySpace(boolean[][] memorySpace, Pair<Integer, Integer> current) {
        for (int i = 0; i < memorySpace.length; i++) {
            for (int j = 0; j < memorySpace[i].length; j++) {
                boolean aBoolean = memorySpace[i][j];
                if (i == current.b() && j == current.a()) {
                    System.out.print("@");
                } else {
                    System.out.print(aBoolean ? "#" : ".");
                }
            }
            System.out.println();
        }
        System.out.println("====");
    }

    private static void printMemorySpace(boolean[][] memorySpace, Pair<Integer, Integer> current, Map<Pair<Integer, Integer>, Integer> visited) {
        for (int i = 0; i < memorySpace.length; i++) {
            for (int j = 0; j < memorySpace[i].length; j++) {
                boolean aBoolean = memorySpace[i][j];
                if (i == current.b() && j == current.a()) {
                    System.out.print("@");
                } else if (visited.containsKey(Pair.of(i, j))) {
                    System.out.print("$");
                } else {
                    System.out.print(aBoolean ? "#" : ".");
                }
            }
            System.out.println();
        }
        System.out.println("====");
    }
}

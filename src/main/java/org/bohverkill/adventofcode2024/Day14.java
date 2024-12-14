package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day14 {
    //    public static final int MAX_X = 11;
//    public static final int MAX_Y = 7;
    public static final int MAX_X = 101;
    public static final int MAX_Y = 103;
    public static final int MAX_SECONDS = 100;
    private static final Pattern ROBOT_PATTERN = Pattern.compile("^p=(\\d+),(\\d+) v=([0-9-]+),([0-9-]+)$");

    public static void main(String[] args) {
//        final String path = "/2024/Day14_example";
        final String path = "/2024/Day14_input";
        final List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots = Utils.getInputLines(path).map(Day14::parse).toList();
        task1(getSpace(robots));
        task2(getSpace(robots));
    }

    private static Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> parse(String string) {
        final Matcher robotMatcher = Utils.getMatcher(ROBOT_PATTERN, string);
        final int px = Integer.parseInt(robotMatcher.group(1));
        final int py = Integer.parseInt(robotMatcher.group(2));
        final int vx = Integer.parseInt(robotMatcher.group(3));
        final int vy = Integer.parseInt(robotMatcher.group(4));
        return Pair.of(new Pair<>(px, py), new Pair<>(vx, vy));
    }

    private static Tile[][] getSpace(List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots) {
        Tile[][] space = newSpace();
        for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> robot : robots) {
            space[robot.a().a()][robot.a().b()].add(robot.b());
        }
        return space;
    }

    private static void task1(Tile[][] space) {
        for (int i = 0; i < MAX_SECONDS; i++) {
            space = performSecond(space);
        }

        int[] quadrants = new int[4];
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                final Tile tile = space[i][j];
                if (!tile.isEmpty()) {
                    if (i < MAX_X / 2 && j < MAX_Y / 2) {
                        quadrants[0] += tile.size();
                    } else if (i >= (MAX_X / 2) + 1 && j < MAX_Y / 2) {
                        quadrants[1] += tile.size();
                    } else if (i < MAX_X / 2 && j >= (MAX_Y / 2) + 1) {
                        quadrants[2] += tile.size();
                    } else if (i >= (MAX_X / 2) + 1 && j >= (MAX_Y / 2) + 1) {
                        quadrants[3] += tile.size();
                    }
                }
            }
        }

        int res = Arrays.stream(quadrants).reduce(1, (a, b) -> a * b);
        System.out.println("Part 1: " + res);
    }

    private static Tile[][] performSecond(Tile[][] space) {
        final Tile[][] nextSpace = newSpace();
        for (int x = 0; x < space.length; x++) {
            for (int y = 0; y < space[x].length; y++) {
                final Tile tile = space[x][y];
                if (!tile.isEmpty()) {
                    for (Pair<Integer, Integer> robot : tile.robots()) {
                        nextSpace[Math.floorMod(x + robot.a(), MAX_X)][Math.floorMod(y + robot.b(), MAX_Y)].add(robot);
                    }
                }
            }
        }
        return nextSpace;
    }

    private static Tile[][] newSpace() {
        final Tile[][] space = new Tile[MAX_X][MAX_Y];
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                space[i][j] = Tile.empty();
            }
        }
        return space;
    }

    private static void task2(Tile[][] space) {
        int maxNeighbor = Integer.MIN_VALUE;
        int maxNeighborSeconds = 0;
        Tile[][] maxNeighborSpace = space;
        for (int seconds = 1; seconds <= MAX_X * MAX_Y; seconds++) {
            space = performSecond(space);
            final int neighbours = numberOfNeighbours(space);
            if (neighbours > maxNeighbor) {
                maxNeighbor = neighbours;
                maxNeighborSpace = space;
                maxNeighborSeconds = seconds;
            }
        }
        printSpace(maxNeighborSpace);
        System.out.println("Part 2: " + maxNeighborSeconds);
    }

    private static int numberOfNeighbours(Tile[][] space) {
        int count = 0;
        for (int x = -1; x <= space.length; x++) {
            for (int y = -1; y <= space[0].length; y++) {
                count += getCount(space, x - 1, y - 1);
                count += getCount(space, x - 1, y);
                count += getCount(space, x - 1, y + 1);
                count += getCount(space, x, y - 1);
                count += getCount(space, x, y);
                count += getCount(space, x, y + 1);
                count += getCount(space, x + 1, y - 1);
                count += getCount(space, x + 1, y);
                count += getCount(space, x + 1, y + 1);
            }
        }
        return count;
    }

    private static int getCount(Tile[][] space, int x, int y) {
        if (x >= 0 && x < space.length && y >= 0 && y < space[0].length && !space[x][y].isEmpty()) {
            return 1;
        } else {
            return 0;
        }
    }


    private static void printSpace(final Tile[][] space) {
        for (int i = 0; i < space[0].length; i++) {
            for (Tile[] tiles : space) {
                Tile tile = tiles[i];
                if (tile.isEmpty()) {
                    System.out.print(".");
                } else {
                    System.out.print(tile.size());
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private record Tile(List<Pair<Integer, Integer>> robots) {

        private static Tile empty() {
            return new Tile(new ArrayList<>(0));
        }

        private void add(Pair<Integer, Integer> robot) {
            this.robots().add(robot);
        }

        private boolean isEmpty() {
            return this.robots().isEmpty();
        }


        private int size() {
            return this.robots().size();
        }
    }
}

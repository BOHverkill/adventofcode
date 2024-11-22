package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day22Part1 {
    private static final Pattern BRICK_PATTERN = Pattern.compile("^(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)$");

    public static void main(String[] args) {
//        final Stream<String> lines = Utils.getInputLines("/2023/Day22_example");
        final Stream<String> lines = Utils.getInputLines("/2023/Day22_input");

        final List<Brick> bricks = lines.map(Day22Part1::parseBrick).toList();
//        printBricks(bricks);

        final List<Brick> fallen = fall(bricks);
//        printBricks(fallen);

        Set<Coordinates> occupied = fallen.stream().flatMap(b -> b.expand().stream()).collect(Collectors.toSet());
        final Map<Coordinates, Brick> coordinatesBrickMap = fallen.stream().flatMap(b -> b.expand().stream().map(c -> Map.entry(c, b))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final long count = fallen.stream().filter(brick -> canBeSafelyDisintegrated(brick, occupied, coordinatesBrickMap)).count();

        System.out.println("Day 22 Part 1: " + count);
    }

    public static boolean canBeSafelyDisintegrated(Brick brick, Set<Coordinates> occupied, Map<Coordinates, Brick> coordinatesBrickMap) {
        final List<Coordinates> aboveList = brick.zOffset(1).expand().stream().filter(b -> !brick.expand().contains(b)).filter(occupied::contains).toList();
        if (!aboveList.isEmpty()) {
            for (Coordinates coordinates : aboveList) {
                final Brick current = coordinatesBrickMap.get(coordinates);
                if (current.zOffset(-1).expand().stream().filter(b -> !current.expand().contains(b)).filter(b -> !brick.expand().contains(b)).noneMatch(occupied::contains)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Brick> fall(List<Brick> bricks) {
        List<Brick> fallen = new ArrayList<>(bricks);
        Set<Coordinates> occupied = bricks.stream().flatMap(brick -> brick.expand().stream()).collect(Collectors.toSet());

        final int maxZ = bricks.stream().flatMap(brick -> Stream.of(brick.start(), brick.end())).mapToInt(Coordinates::z).max().orElseThrow();

        final Map<Integer, List<Brick>> zMap = bricks.stream().collect(Collectors.groupingBy(Brick::minZ));

        for (int z = 2; z <= maxZ; z++) {
            final List<Brick> currentZBricks = zMap.getOrDefault(z, Collections.emptyList());
            for (Brick currentZBrick : currentZBricks) {
                if (currentZBrick.zOffset(-1).expand().stream().filter(b -> !currentZBrick.expand().contains(b)).noneMatch(occupied::contains)) {
                    Brick zOffsetBrick = currentZBrick.zOffset(-1);
                    for (int i = 2; i < currentZBrick.minZ(); i++) {
                        final Brick zOffsetBrickCandidate = currentZBrick.zOffset(-i);
                        if (zOffsetBrickCandidate.expand().stream().filter(b -> !currentZBrick.expand().contains(b)).noneMatch(occupied::contains)) {
                            zOffsetBrick = zOffsetBrickCandidate;
                        } else {
                            break;
                        }
                    }
                    currentZBrick.expand().forEach(occupied::remove);
                    fallen.remove(currentZBrick);
                    fallen.add(zOffsetBrick);
                    occupied.addAll(zOffsetBrick.expand());
                }
            }
        }

        return fallen;
    }

    public static void printBricks(List<Brick> bricks) {
        final int maxX = bricks.stream().flatMap(brick -> Stream.of(brick.start, brick.end)).mapToInt(Coordinates::x).max().orElseThrow();
        final int maxY = bricks.stream().flatMap(brick -> Stream.of(brick.start, brick.end)).mapToInt(Coordinates::y).max().orElseThrow();
        final int maxZ = bricks.stream().flatMap(brick -> Stream.of(brick.start, brick.end)).mapToInt(Coordinates::z).max().orElseThrow();
        Set<Coordinates> occupied = bricks.stream().flatMap(brick -> brick.expand().stream()).collect(Collectors.toSet());

        System.out.println(" ".repeat(maxX / 2) + "x");
        for (int x = 0; x <= maxX; x++) {
            System.out.print(x);
        }
        System.out.println();
        Set<Map.Entry<Integer, Integer>> zXOccupied = occupied.stream().map(coordinates -> Map.entry(coordinates.z(), coordinates.x())).collect(Collectors.toSet());
        for (int z = maxZ; z >= 1; z--) {
            for (int x = 0; x <= maxX; x++) {
                if (zXOccupied.contains(Map.entry(z, x))) {
                    System.out.print("O");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println(" " + z);
        }
        System.out.println("-".repeat(maxX + 1) + " 0\n");

        System.out.println(" ".repeat(maxY / 2) + "y");
        for (int y = 0; y <= maxY; y++) {
            System.out.print(y);
        }
        System.out.println();
        Set<Map.Entry<Integer, Integer>> zYOccupied = occupied.stream().map(coordinates -> Map.entry(coordinates.z(), coordinates.y())).collect(Collectors.toSet());
        for (int z = maxZ; z >= 1; z--) {
            for (int y = 0; y <= maxY; y++) {
                if (zYOccupied.contains(Map.entry(z, y))) {
                    System.out.print("O");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println(" " + z);
        }
        System.out.println("-".repeat(maxX + 1) + " 0\n");
    }

    public static Brick parseBrick(String line) {
        final Matcher ratingMatcher = Utils.getMatcher(BRICK_PATTERN, line);

        final int startX = Integer.parseInt(ratingMatcher.group(1));
        final int startY = Integer.parseInt(ratingMatcher.group(2));
        final int startZ = Integer.parseInt(ratingMatcher.group(3));
        final int endX = Integer.parseInt(ratingMatcher.group(4));
        final int endY = Integer.parseInt(ratingMatcher.group(5));
        final int endZ = Integer.parseInt(ratingMatcher.group(6));

        return new Brick(startX, startY, startZ, endX, endY, endZ);
    }

    public record Brick(Coordinates start, Coordinates end) {
        public Brick(int startX, int startY, int startZ, int endX, int endY, int endZ) {
            this(new Coordinates(startX, startY, startZ), new Coordinates(endX, endY, endZ));
        }

        public static List<Coordinates> expand(Brick brick) {
            final List<Coordinates> coords = new ArrayList<>();
            for (int x = brick.minX(); x <= brick.maxX(); x++) {
                for (int y = brick.minY(); y <= brick.maxY(); y++) {
                    for (int z = brick.minZ(); z <= brick.maxZ(); z++) {
                        coords.add(new Coordinates(x, y, z));
                    }
                }
            }
            return coords;
        }

        public Brick zOffset(int offset) {
            return new Brick(startX(), startY(), startZ() + offset, endX(), endY(), endZ() + offset);
        }

        public List<Coordinates> expand() {
            return Brick.expand(this);
        }

        public int startX() {
            return this.start.x();
        }

        public int startY() {
            return this.start.y();
        }

        public int startZ() {
            return this.start.z();
        }

        public int endX() {
            return this.end.x();
        }

        public int endY() {
            return this.end.y();
        }

        public int endZ() {
            return this.end.z();
        }

        public int maxX() {
            return Math.max(this.start.x(), this.end.x());
        }

        public int maxY() {
            return Math.max(this.start.y(), this.end.y());
        }

        public int maxZ() {
            return Math.max(this.start.z(), this.end.z());
        }

        public int minX() {
            return Math.min(this.start.x(), this.end.x());
        }

        public int minY() {
            return Math.min(this.start.y(), this.end.y());
        }

        public int minZ() {
            return Math.min(this.start.z(), this.end.z());
        }
    }

    public record Coordinates(int x, int y, int z) {
    }
}

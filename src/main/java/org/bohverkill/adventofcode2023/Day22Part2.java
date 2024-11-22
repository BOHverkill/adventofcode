package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bohverkill.adventofcode2023.Day22Part1.*;

public class Day22Part2 {
    public static void main(String[] args) {
//        final Stream<String> lines = Utils.getInputLines("/2023/Day22_example");
//        final Stream<String> lines = Utils.getInputLines("/2023/Day22_2_1_example");
//        final Stream<String> lines = Utils.getInputLines("/2023/Day22_2_2_example");
//        final Stream<String> lines = Utils.getInputLines("/2023/Day22_2_3_example");
//        final Stream<String> lines = Utils.getInputLines("/2023/Day22_2_4_example");
//        final Stream<String> lines = Utils.getInputLines("/2023/Day22_2_5_example");
        final Stream<String> lines = Utils.getInputLines("/2023/Day22_input");

        final List<Brick> bricks = lines.map(Day22Part1::parseBrick).toList();
//        printBricks(bricks);

        final List<Brick> fallen = fall(bricks);
//        printBricks(fallen);

        Set<Coordinates> occupied = fallen.stream().flatMap(b -> b.expand().stream()).collect(Collectors.toSet());
        final Map<Coordinates, Brick> coordinatesBrickMap = fallen.stream().flatMap(b -> b.expand().stream().map(c -> Map.entry(c, b))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        int sum = fallen.stream().mapToInt(brick -> disintegrateCount(brick, fallen, occupied, coordinatesBrickMap)).sum();
        System.out.println("Day 22 Part 2: " + sum);
    }

    public static int disintegrateCount(Brick brick, List<Brick> bricks, Set<Coordinates> occupied, Map<Coordinates, Brick> coordinatesBrickMap) {
        Set<Brick> fallen = new HashSet<>();
        fallen.add(brick);
        for (Brick current : bricks) {
            final Set<Brick> belowBricks = current.zOffset(-1).expand().stream().filter(b -> !current.expand().contains(b)).filter(occupied::contains).map(coordinatesBrickMap::get).collect(Collectors.toSet());
            Set<Brick> belowBricksWithoutFallen = new HashSet<>(belowBricks);
            belowBricksWithoutFallen.removeAll(fallen);
            if (!belowBricks.isEmpty() && belowBricksWithoutFallen.isEmpty()) {
                fallen.add(current);
            }
        }
        return fallen.size() - 1;
    }
}

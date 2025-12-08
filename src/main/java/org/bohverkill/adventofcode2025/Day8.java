package org.bohverkill.adventofcode2025;

import org.bohverkill.models.Pair;
import org.bohverkill.models.Point3D;
import org.bohverkill.utils.CollectionUtils;
import org.bohverkill.utils.GraphUtils;
import org.bohverkill.utils.MathUtils;
import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Gatherers;

public class Day8 {

    static void main() {
//        final String name = "/2025/Day8_example";
//        final int length = 10;
        final String name = "/2025/Day8_input";
        final int length = 1000;
        final List<Point3D<Integer>> input = Utils.getInputLines(name).map(s -> s.split(",")).map(s -> Point3D.of(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]))).toList();
        task1(input, length);
        task2(input);
    }

    private static void task1(final List<Point3D<Integer>> input, int length) {
        final List<Pair<Point3D<Integer>, Point3D<Integer>>> combinations = getCombinations(input);

        List<Set<Point3D<Integer>>> connections = new ArrayList<>();
        Map<Point3D<Integer>, Set<Point3D<Integer>>> connectionMap = new HashMap<>();
        for (Pair<Point3D<Integer>, Point3D<Integer>> combination : combinations.subList(0, length)) {
            connect(combination, connectionMap, connections);
        }
        System.out.println("Part 1: " + connections.stream().map(Set::size).sorted(Comparator.reverseOrder()).limit(3).gather(Gatherers.fold(() -> 1, MathUtils::times)).findFirst().orElseThrow());
    }

    private static void task2(final List<Point3D<Integer>> input) {
        final List<Pair<Point3D<Integer>, Point3D<Integer>>> combinations = getCombinations(input);

        List<Set<Point3D<Integer>>> connections = new ArrayList<>();
        Map<Point3D<Integer>, Set<Point3D<Integer>>> connectionMap = new HashMap<>();
        for (Pair<Point3D<Integer>, Point3D<Integer>> combination : combinations) {
            connect(combination, connectionMap, connections);
            if (connectionMap.size() == input.size()) {
                System.out.println("Part 2: " + combination.a().x() * combination.b().x());
                break;
            }
        }
    }

    private static List<Pair<Point3D<Integer>, Point3D<Integer>>> getCombinations(List<Point3D<Integer>> input) {
        final List<Pair<Point3D<Integer>, Point3D<Integer>>> combinations = CollectionUtils.combinations(input);
        combinations.sort(Comparator.comparingInt(p -> GraphUtils.euclideanDistance(p.a().x(), p.b().x(), p.a().y(), p.b().y(), p.a().z(), p.b().z())));
        return combinations;
    }

    private static void connect(Pair<Point3D<Integer>, Point3D<Integer>> combination, Map<Point3D<Integer>, Set<Point3D<Integer>>> connectionMap, List<Set<Point3D<Integer>>> connections) {
        if (connectionMap.containsKey(combination.a()) && connectionMap.containsKey(combination.b())) {
            final Set<Point3D<Integer>> set1 = connectionMap.get(combination.a());
            final Set<Point3D<Integer>> set2 = connectionMap.get(combination.b());
            if (!set1.equals(set2)) {
                set1.addAll(set2);
                connections.remove(set2);
                set2.forEach(integerPoint3D -> connectionMap.replace(integerPoint3D, set1));
            }
        } else if (connectionMap.containsKey(combination.a())) {
            final Set<Point3D<Integer>> set = connectionMap.get(combination.a());
            set.add(combination.b());
            connectionMap.put(combination.b(), set);
        } else if (connectionMap.containsKey(combination.b())) {
            final Set<Point3D<Integer>> set = connectionMap.get(combination.b());
            set.add(combination.a());
            connectionMap.put(combination.a(), set);
        } else {
            Set<Point3D<Integer>> set = new HashSet<>();
            set.add(combination.a());
            set.add(combination.b());
            connections.add(set);
            connectionMap.put(combination.a(), set);
            connectionMap.put(combination.b(), set);
        }
    }
}

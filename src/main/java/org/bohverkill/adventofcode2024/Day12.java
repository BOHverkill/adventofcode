package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.Utils;

import java.util.*;

public class Day12 {
    public static void main(String[] args) {
//        final String path = "/2024/Day12_example";
        final String path = "/2024/Day12_input";
        final List<String> input = Utils.getInputList(path);
        task1(input);
        task2(input);
    }

    private static void task1(final List<String> map) {
        final List<List<Pair<Integer, Integer>>> regions = findRegions(map);
        int sum = regions.stream().mapToInt(region -> region.size() * findNegative(region).size()).sum();
        System.out.println("Part 1: " + sum);
    }

    private static List<List<Pair<Integer, Integer>>> findRegions(final List<String> map) {
        final Set<Pair<Integer, Integer>> visited = new HashSet<>();
        final List<List<Pair<Integer, Integer>>> regions = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            final String s = map.get(i);
            for (int j = 0; j < s.length(); j++) {
                if (!visited.contains(new Pair<>(i, j))) {
                    final List<Pair<Integer, Integer>> region = findRegion(i, j, map, visited);
                    regions.add(region);
                }
            }
        }
        return regions;
    }

    private static List<Pair<Integer, Integer>> findRegion(final int i, final int j, final List<String> map, Set<Pair<Integer, Integer>> visited) {
        final char c = map.get(i).charAt(j);
        visited.add(new Pair<>(i, j));
        List<Pair<Integer, Integer>> region = new ArrayList<>();
        region.add(new Pair<>(i, j));
        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
        addToQueue(queue, i, j);
        while (!queue.isEmpty()) {
            final Pair<Integer, Integer> current = queue.poll();
            if (current.a() >= 0 && current.a() < map.size() && current.b() >= 0 && current.b() < map.getFirst().length() && !visited.contains(current)) {
                final char currentC = map.get(current.a()).charAt(current.b());
                if (currentC == c) {
                    visited.add(current);
                    region.add(current);
                    addToQueue(queue, current.a(), current.b());
                }
            }
        }
        return region;
    }

    private static List<Pair<Integer, Integer>> findNegative(final List<Pair<Integer, Integer>> region) {
        List<Pair<Integer, Integer>> negative = new ArrayList<>();
        region.forEach(current -> addToCollection(negative, current.a(), current.b()));
        region.forEach(o -> negative.removeAll(Collections.singleton(o)));
        return negative;
    }

    private static void addToQueue(Queue<Pair<Integer, Integer>> queue, int i, int j) {
        queue.offer(Pair.of(i + 1, j));
        queue.offer(Pair.of(i - 1, j));
        queue.offer(Pair.of(i, j + 1));
        queue.offer(Pair.of(i, j - 1));
    }

    private static void addToCollection(Collection<Pair<Integer, Integer>> list, int i, int j) {
        list.add(Pair.of(i + 1, j));
        list.add(Pair.of(i - 1, j));
        list.add(Pair.of(i, j + 1));
        list.add(Pair.of(i, j - 1));
    }

    private static void task2(final List<String> map) {
        final List<List<Pair<Integer, Integer>>> regions = findRegions(map);
        int sum = regions.stream().mapToInt(region -> region.size() * findSides(region)).sum();
        System.out.println("Part 2: " + sum);
    }

    private static int findSides(List<Pair<Integer, Integer>> region) {
        // find and count corners
        Set<Pair<Integer, Integer>> regionSet = new HashSet<>(region);
        int side = 0;
        for (Pair<Integer, Integer> current : region) {
            // outbound corner
            if (!regionSet.contains(Pair.of(current.a() - 1, current.b() - 1)) && !regionSet.contains(Pair.of(current.a() - 1, current.b())) && !regionSet.contains(Pair.of(current.a(), current.b() - 1))) {
                side++;
            }
            if (!regionSet.contains(Pair.of(current.a() - 1, current.b() + 1)) && !regionSet.contains(Pair.of(current.a() - 1, current.b())) && !regionSet.contains(Pair.of(current.a(), current.b() + 1))) {
                side++;
            }
            if (!regionSet.contains(Pair.of(current.a() + 1, current.b() + 1)) && !regionSet.contains(Pair.of(current.a() + 1, current.b())) && !regionSet.contains(Pair.of(current.a(), current.b() + 1))) {
                side++;
            }
            if (!regionSet.contains(Pair.of(current.a() + 1, current.b() - 1)) && !regionSet.contains(Pair.of(current.a() + 1, current.b())) && !regionSet.contains(Pair.of(current.a(), current.b() - 1))) {
                side++;
            }

            // inbound corner
            if (!regionSet.contains(Pair.of(current.a() - 1, current.b() - 1)) && regionSet.contains(Pair.of(current.a() - 1, current.b())) && regionSet.contains(Pair.of(current.a(), current.b() - 1))) {
                side++;
            }
            if (!regionSet.contains(Pair.of(current.a() - 1, current.b() + 1)) && regionSet.contains(Pair.of(current.a() - 1, current.b())) && regionSet.contains(Pair.of(current.a(), current.b() + 1))) {
                side++;
            }
            if (!regionSet.contains(Pair.of(current.a() + 1, current.b() + 1)) && regionSet.contains(Pair.of(current.a() + 1, current.b())) && regionSet.contains(Pair.of(current.a(), current.b() + 1))) {
                side++;
            }
            if (!regionSet.contains(Pair.of(current.a() + 1, current.b() - 1)) && regionSet.contains(Pair.of(current.a() + 1, current.b())) && regionSet.contains(Pair.of(current.a(), current.b() - 1))) {
                side++;
            }

            // edge case
            if (regionSet.contains(Pair.of(current.a() - 1, current.b() - 1)) && !regionSet.contains(Pair.of(current.a() - 1, current.b())) && !regionSet.contains(Pair.of(current.a(), current.b() - 1))) {
                side++;
            }
            if (regionSet.contains(Pair.of(current.a() - 1, current.b() + 1)) && !regionSet.contains(Pair.of(current.a() - 1, current.b())) && !regionSet.contains(Pair.of(current.a(), current.b() + 1))) {
                side++;
            }
            if (regionSet.contains(Pair.of(current.a() + 1, current.b() + 1)) && !regionSet.contains(Pair.of(current.a() + 1, current.b())) && !regionSet.contains(Pair.of(current.a(), current.b() + 1))) {
                side++;
            }
            if (regionSet.contains(Pair.of(current.a() + 1, current.b() - 1)) && !regionSet.contains(Pair.of(current.a() + 1, current.b())) && !regionSet.contains(Pair.of(current.a(), current.b() - 1))) {
                side++;
            }
        }
        return side;
    }
}

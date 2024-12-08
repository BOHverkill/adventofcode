package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.CollectionUtils;
import org.bohverkill.utils.Utils;

import java.util.*;

public class Day8 {
    public static void main(String[] args) {
//        final String path = "/2024/Day8_example";
        final String path = "/2024/Day8_input";
        final List<String> input = Utils.getInputList(path);
        final Pair<Integer, Integer> inputSize = Pair.of(input.size(), input.getFirst().length());
        final Map<Character, List<Pair<Integer, Integer>>> antennas = parse(input);
        task1(antennas, inputSize);
        task2(antennas, inputSize);
    }

    private static void task1(final Map<Character, List<Pair<Integer, Integer>>> antennas, Pair<Integer, Integer> inputSize) {
        Set<Pair<Integer, Integer>> antinodes = new HashSet<>();
        for (List<Pair<Integer, Integer>> value : antennas.values()) {
            for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> combination : CollectionUtils.combinations(value)) {
                final Pair<Integer, Integer> antenna1 = combination.a();
                final Pair<Integer, Integer> antenna2 = combination.b();

                final Pair<Integer, Integer> antinode1 = Pair.of(antenna1.a() + (antenna1.a() - antenna2.a()), antenna1.b() + (antenna1.b() - antenna2.b()));
                final Pair<Integer, Integer> antinode2 = Pair.of(antenna2.a() + (antenna2.a() - antenna1.a()), antenna2.b() + (antenna2.b() - antenna1.b()));

                if (antinode1.a() >= 0 && antinode1.a() < inputSize.a() && antinode1.b() >= 0 && antinode1.b() < inputSize.b()) {
                    antinodes.add(antinode1);
                }
                if (antinode2.a() >= 0 && antinode2.a() < inputSize.a() && antinode2.b() >= 0 && antinode2.b() < inputSize.b()) {
                    antinodes.add(antinode2);
                }
            }
        }

        System.out.println("Part 1: " + antinodes.size());
    }

    private static void task2(final Map<Character, List<Pair<Integer, Integer>>> antennas, Pair<Integer, Integer> inputSize) {
        Set<Pair<Integer, Integer>> antinodes = new HashSet<>();
        for (List<Pair<Integer, Integer>> value : antennas.values()) {
            for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> combination : CollectionUtils.combinations(value)) {
                final Pair<Integer, Integer> antenna1 = combination.a();
                final Pair<Integer, Integer> antenna2 = combination.b();

                antinodes.add(antenna1);
                antinodes.add(antenna2);

                addResonantHarmonics(inputSize, antinodes, antenna1, antenna2);

                addResonantHarmonics(inputSize, antinodes, antenna2, antenna1);
            }
        }

        System.out.println("Part 2: " + antinodes.size());
    }

    private static void addResonantHarmonics(Pair<Integer, Integer> inputSize, Set<Pair<Integer, Integer>> antinodes, Pair<Integer, Integer> antenna1, Pair<Integer, Integer> antenna2) {
        Pair<Integer, Integer> antinode = Pair.of(antenna1.a(), antenna1.b());
        while (true) {
            antinode = Pair.of(antinode.a() + (antenna1.a() - antenna2.a()), antinode.b() + (antenna1.b() - antenna2.b()));
            if (antinode.a() >= 0 && antinode.a() < inputSize.a() && antinode.b() >= 0 && antinode.b() < inputSize.b()) {
                antinodes.add(antinode);
            } else {
                break;
            }
        }
    }

    private static Map<Character, List<Pair<Integer, Integer>>> parse(final List<String> input) {
        Map<Character, List<Pair<Integer, Integer>>> antennas = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            final String s = input.get(i);
            for (int j = 0; j < s.length(); j++) {
                final char c = s.charAt(j);
                if (c != '.') {
                    antennas.computeIfAbsent(c, _ -> new ArrayList<>()).add(new Pair<>(i, j));
                }
            }
        }
        return antennas;
    }
}

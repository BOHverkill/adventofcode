package org.bohverkill.adventofcode2023;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Adapted from https://redd.it/18hbbxe
public class Day12Part2 {
    public static void main(String[] args) {
//        Stream<String> input = Utils.getInputLines("/2023/Day12_example");
        Stream<String> input = Utils.getInputLines("/2023/Day12_input");
        final Map<Row, Long> cache = new HashMap<>();
        final long sum = input.map(Day12Part2::mapToRow).map(Day12Part2::expand).mapToLong(row -> calc(cache, row)).sum();
        System.out.println("Day 12 Part 2: " + sum);
    }

    private static long calc(Map<Row, Long> cache, Row row) {
        // memoization
        if (cache.containsKey(row)) {
            return cache.get(row);
        }

        long out;
        if (row.groups().isEmpty()) {
            if (!row.springs().contains("#")) {
                out = 1;
            } else {
                out = 0;
            }
        } else {
            if (row.springs().isEmpty()) {
                out = 0;
            } else {
                out = switch (row.springs().charAt(0)) {
                    case '#' -> pound(cache, row);
                    case '.' -> dot(cache, row);
                    case '?' -> dot(cache, row) + pound(cache, row);
                    default -> throw new IllegalStateException("Unrecognised character: " + row.springs().charAt(0));
                };
            }
        }
        cache.put(row, out);
        return out;
    }

    private static long pound(Map<Row, Long> cache, Row row) {
        final int currentGroup = row.groups().getFirst();
        if (currentGroup > row.springs().length()) {
            return 0;
        }

        if (!row.springs().substring(0, currentGroup).replace("?", "#").equals("#".repeat(currentGroup))) {
            return 0;
        }

        if (row.springs().length() == currentGroup) {
            if (row.groups().size() == 1) {
                return 1;
            } else {
                return 0;
            }
        }

        if (row.springs().charAt(currentGroup) == '#') {
            return 0;
        }

        return calc(cache, new Row(row.springs().substring(currentGroup + 1), row.groups().subList(1, row.groups().size())));
    }

    private static long dot(Map<Row, Long> cache, Row row) {
        return calc(cache, new Row(row.springs().substring(1), row.groups()));
    }

    public static Row expand(Row row) {
        final String springs = row.springs();
        final List<Integer> groups = IntStream.range(0, row.groups().size() * 5).mapToObj(i -> row.groups().get(i % row.groups().size())).toList();
        return new Row(springs + "?" + springs + "?" + springs + "?" + springs + "?" + springs, groups);
    }

    public static Row mapToRow(String input) {
        final String[] split = input.split(" ");
        if (split.length != 2) {
            throw new IllegalStateException("Malformed input: " + input);
        }
        return new Row(split[0], Arrays.stream(split[1].split(",")).map(Integer::parseInt).toList());
    }

    public record Row(String springs, List<Integer> groups) {
    }
}

package org.bohverkill.adventofcode2024;

import org.bohverkill.utils.CollectionUtils;
import org.bohverkill.utils.MathUtils;
import org.bohverkill.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 {
    public static void main(String[] args) {
//        final String path = "/2024/Day11_example";
        final String path = "/2024/Day11_input";
        final List<Long> input = Utils.getInputSplit(path, " ").map(Long::parseLong).toList();
        task1(input);
        task2(input);
    }

    private static void task1(final List<Long> input) {
        final long size = getArrangementSizeAfterIterations(input, 25);
        System.out.println("Part 1: " + size);
    }

    private static void task2(final List<Long> input) {
        final long size = getArrangementSizeAfterIterations(input, 75);
        System.out.println("Part 2: " + size);
    }

    private static long getArrangementSizeAfterIterations(List<Long> input, int iterations) {
        Map<Long, Long> arrangement = convert(input);
        for (int i = 0; i < iterations; i++) {
            arrangement = blink(arrangement);
        }
        return arrangement.values().stream().mapToLong(value -> value).sum();
    }

    private static Map<Long, Long> blink(final Map<Long, Long> arrangement) {
        final Map<Long, Long> newArrangement = new HashMap<>();
        for (Map.Entry<Long, Long> entry : arrangement.entrySet()) {
            if (entry.getKey() == 0) {
                CollectionUtils.putOrApply(newArrangement, 1L, entry.getValue(), MathUtils::plus);
            } else {
                final String s = String.valueOf(entry.getKey());
                if (s.length() % 2 == 0) {
                    CollectionUtils.putOrApply(newArrangement, Long.parseLong(s.substring(0, s.length() / 2)), entry.getValue(), MathUtils::plus);
                    CollectionUtils.putOrApply(newArrangement, Long.parseLong(s.substring(s.length() / 2)), entry.getValue(), MathUtils::plus);
                } else {
                    CollectionUtils.putOrApply(newArrangement, entry.getKey() * 2024, entry.getValue(), MathUtils::plus);
                }
            }
        }
        return newArrangement;
    }

    private static Map<Long, Long> convert(List<Long> input) {
        Map<Long, Long> map = new HashMap<>();
        for (long l : input) {
            CollectionUtils.putOrApply(map, l, 1L, MathUtils::plus);
        }
        return map;
    }
}

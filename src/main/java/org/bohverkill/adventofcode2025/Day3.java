package org.bohverkill.adventofcode2025;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 {
    static void main() {
//        final String name = "/2025/Day3_example";
        final String name = "/2025/Day3_input";
        final List<List<Integer>> ranges = Utils.getInputLines(name).map(StringUtils::parseCharToIntegerList).toList();
        task1(ranges);
        task2(ranges);
    }

    private static void task1(final List<List<Integer>> ranges) {
        int sum = ranges.stream().map(range -> IntStream.range(0, range.size()).mapToObj(i -> Pair.of(range.get(i), i)).collect(Collectors.groupingBy(Pair::a))).mapToInt(Day3::getJoltage1).sum();
        System.out.println("Part 1: " + sum);
    }

    private static int getJoltage1(Map<Integer, List<Pair<Integer, Integer>>> map) {
        List<Map.Entry<Integer, List<Pair<Integer, Integer>>>> list = new ArrayList<>(map.entrySet()).reversed();
        for (Map.Entry<Integer, List<Pair<Integer, Integer>>> l1 : list) {
            List<Pair<Integer, Integer>> nums1 = l1.getValue();
            for (Map.Entry<Integer, List<Pair<Integer, Integer>>> l2 : list) {
                List<Pair<Integer, Integer>> nums2 = l2.getValue();
                for (Pair<Integer, Integer> integerIntegerPair : nums1) {
                    for (Pair<Integer, Integer> integerPair : nums2) {
                        if (integerIntegerPair.b() < integerPair.b()) {
                            return integerIntegerPair.a() * 10 + integerPair.a();
                        }
                    }
                }
            }
        }
        throw new IllegalStateException("No joltage");
    }

    private static void task2(final List<List<Integer>> ranges) {
        long sum = 0;
        for (List<Integer> range : ranges) {
            List<Integer> digits = new ArrayList<>(range.subList(range.size() - 12, range.size()));
            for (int i = range.size() - 13; i >= 0; i--) {
                int curr = range.get(i);
                for (int j = 0; j < digits.size(); j++) {
                    if (curr >= digits.get(j)) {
                        int newCurr = digits.get(j);
                        digits.set(j, curr);
                        curr = newCurr;
                    } else {
                        break;
                    }
                }
            }
            long number = 0;
            for (int digit : digits) {
                number *= 10;
                number += digit;
            }
            sum += number;
        }
        System.out.println("Part 2: " + sum);
    }
}

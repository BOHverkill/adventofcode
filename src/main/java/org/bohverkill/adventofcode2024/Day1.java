package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.MathUtils;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day1 {
    private static final Pattern LISTS_PATTERN = Pattern.compile("^(\\d+) {3}(\\d+)$");

    public static void main(String[] args) {
//        final List<Pair<Integer, Integer>> lists = Utils.getInputLines("/2024/Day1_example").map(Day1::parse).toList();
        final List<Pair<Integer, Integer>> lists = Utils.getInputLines("/2024/Day1_input").map(Day1::parse).toList();
        task1(lists);
        task2(lists);
    }

    private static void task1(final List<Pair<Integer, Integer>> lists) {
        final List<Integer> left = lists.stream().map(Pair::a).collect(Collectors.toCollection(ArrayList::new));
        final List<Integer> right = lists.stream().map(Pair::b).collect(Collectors.toCollection(ArrayList::new));

        left.sort(Integer::compareTo);
        right.sort(Integer::compareTo);

        int sum = IntStream.range(0, MathUtils.assertEquals(left.size(), right.size())).map(i -> Math.abs(left.get(i) - right.get(i))).sum();

        System.out.println("Part 1: " + sum);
    }

    private static void task2(final List<Pair<Integer, Integer>> lists) {
        final List<Integer> left = lists.stream().map(Pair::a).collect(Collectors.toCollection(ArrayList::new));
        final List<Integer> right = lists.stream().map(Pair::b).collect(Collectors.toCollection(ArrayList::new));

        final Map<Integer, Long> collect = right.stream().collect(Collectors.groupingBy(integer -> integer, Collectors.counting()));

        int sum = left.stream().mapToInt(i -> i).map(i -> i * Math.toIntExact(collect.getOrDefault(i, 0L))).sum();

        System.out.println("Part 2: " + sum);
    }

    private static Pair<Integer, Integer> parse(final String line) {
        final Matcher matcher = Utils.getMatcher(LISTS_PATTERN, line);
        final int left = Integer.parseInt(matcher.group(1));
        final int right = Integer.parseInt(matcher.group(2));
        return Pair.of(left, right);
    }
}

package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.models.Triple;
import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.*;

public class Day10 {
    public static void main(String[] args) {
//        final String path = "/2024/Day10_example";
        final String path = "/2024/Day10_input";
        final List<List<Integer>> input = Utils.getInputLines(path).map(StringUtils::parseCharToIntegerList).toList();
        task1(input);
        task2(input);
    }

    private static void task1(final List<List<Integer>> input) {
        final int sum = getTrailheadsSum(input, true);

        System.out.println("Part 1: " + sum);
    }

    private static void task2(final List<List<Integer>> input) {
        final int sum = getTrailheadsSum(input, false);

        System.out.println("Part 2: " + sum);
    }

    private static int getTrailheadsSum(List<List<Integer>> input, boolean uniq) {
        int sum = 0;
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).size(); j++) {
                if (input.get(i).get(j) == 0) {
                    sum += getTrailheadScore(i, j, input, uniq);
                }
            }
        }
        return sum;
    }

    private static int getTrailheadScore(final int i, final int j, final List<List<Integer>> input, final boolean uniq) {
        int score = 0;
        Set<Pair<Integer, Integer>> visitedPeak = new HashSet<>();
        Queue<Triple<Integer, Integer, Integer>> queue = new LinkedList<>();
        addToQueue(queue, i, j, 0);
        while (!queue.isEmpty()) {
            final Triple<Integer, Integer, Integer> current = queue.poll();
            if (current.a() >= 0 && current.a() < input.size() && current.b() >= 0 && current.b() < input.getFirst().size()) {
                final int height = input.get(current.a()).get(current.b());
                if (height == current.c() + 1) {
                    if (height == 9) {
                        if (uniq) {
                            if (!visitedPeak.contains(Pair.of(current.a(), current.b()))) {
                                score++;
                                visitedPeak.add(new Pair<>(current.a(), current.b()));
                            }
                        } else {
                            score++;
                        }
                    } else {
                        addToQueue(queue, current.a(), current.b(), height);
                    }
                }
            }
        }

        return score;
    }

    private static void addToQueue(Queue<Triple<Integer, Integer, Integer>> queue, int i, int j, int height) {
        queue.offer(Triple.of(i + 1, j, height));
        queue.offer(Triple.of(i - 1, j, height));
        queue.offer(Triple.of(i, j + 1, height));
        queue.offer(Triple.of(i, j - 1, height));
    }
}

package org.bohverkill.adventofcode2017;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 {
    public static void main(String[] args) {
//        final String path = "/2017/Day13_example";
        final String path = "/2017/Day13_input";
        final List<Pair<Integer, Integer>> input = Utils.getInputLines(path).map(Day13::parse).toList();
        task1(input);
        task2(input);
    }

    private static void task1(final List<Pair<Integer, Integer>> input) {
        final List<Pair<Integer, Integer>> caught = firewall(input, 0);
        int sum = caught.stream().mapToInt(p -> p.a() * p.b()).sum();
        System.out.println("Part 1: " + sum);
    }

    private static void task2(final List<Pair<Integer, Integer>> input) {
        int delay = 0;
        while (true) {
            List<Pair<Integer, Integer>> caught = firewall(input, delay);
            if (caught.isEmpty()) {
                break;
            }
            delay++;
        }

        System.out.println("Part 2: " + delay);
    }

    private static List<Pair<Integer, Integer>> firewall(List<Pair<Integer, Integer>> input, int delay) {
        final int[] layers = new int[input.getLast().a() + 1];
        Arrays.fill(layers, -1);
        input.forEach(integerIntegerPair -> layers[integerIntegerPair.a()] = integerIntegerPair.b());

        final List<Pair<Integer, Integer>> caught = new ArrayList<>();
        for (int depth = 0; depth < layers.length; depth++) {
            final int range = layers[depth];
            final int zero = (depth + delay) % ((range - 1) * 2);
            if (range != -1 && zero == 0) {
                caught.add(Pair.of(depth, range));
            }
        }
        return caught;
    }

    private static Pair<Integer, Integer> parse(final String line) {
        final String[] split = line.split(": ");
        final int depth = Integer.parseInt(split[0]);
        final int range = Integer.parseInt(split[1]);
        return Pair.of(depth, range);
    }
}

package org.bohverkill.adventofcode2025;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day5 {
    static void main() {
//        final String name = "/2025/Day5_example";
        final String name = "/2025/Day5_input";
        final Pair<List<Pair<Long, Long>>, List<Long>> ids = parse(Utils.getInput(name));
        task1(ids);
        task2(ids);
    }

    private static Pair<List<Pair<Long, Long>>, List<Long>> parse(final String input) {
        final String[] split = input.split("\n\n");
        final List<Pair<Long, Long>> freshRange = Arrays.stream(split[0].split("\n")).map(Day5::getSplit).toList();
        final List<Long> available = Arrays.stream(split[1].split("\n")).map(Long::parseLong).toList();
        return Pair.of(freshRange, available);
    }

    private static Pair<Long, Long> getSplit(String line) {
        final String[] split = line.split("-");
        return Pair.of(Long.parseLong(split[0]), Long.parseLong(split[1]));
    }

    private static void task1(final Pair<List<Pair<Long, Long>>, List<Long>> ids) {
        long count = ids.b().stream().filter(l -> ids.a().stream().anyMatch(longLongPair -> l >= longLongPair.a() && l <= longLongPair.b())).count();
        System.out.println("Part 1: " + count);
    }

    private static void task2(final Pair<List<Pair<Long, Long>>, List<Long>> ids) {
        long count = 0;
        List<Pair<Long, Long>> counted = new ArrayList<>();
        for (Pair<Long, Long> curr : ids.a().stream().sorted(Comparator.comparing(o -> o.b() - o.a(), Comparator.reverseOrder())).toList()) {
            for (Pair<Long, Long> c : counted) {
                if (curr.a() <= c.b() && curr.b() >= c.a()) {
                    if (curr.a() <= c.a()) {
                        curr = Pair.of(curr.a(), c.a() - 1);
                    } else if (curr.b() >= c.b()) {
                        curr = Pair.of(c.b() + 1, curr.b());
                    } else {
                        curr = null;
                        break;
                    }
                }
            }
            if (curr != null) {
                count += curr.b() - curr.a() + 1;
                counted.add(curr);
            }
        }
        System.out.println("Part 2: " + count);
    }
}

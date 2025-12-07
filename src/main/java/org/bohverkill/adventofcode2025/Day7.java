package org.bohverkill.adventofcode2025;

import org.bohverkill.utils.CollectionUtils;
import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.*;

public class Day7 {

    static void main() {
//        final String name = "/2025/Day7_example";
        final String name = "/2025/Day7_input";
        final List<List<String>> input = Utils.getInputLines(name).map(StringUtils::parseCharToStringList).toList();
        task1(input);
        task2(input);
    }

    private static void task1(final List<List<String>> input) {
        int currentRow = 0;
        Set<Integer> currentBeams = new HashSet<>();
        currentBeams.add(input.getFirst().indexOf("S"));
        int count = 0;
        while (currentRow != input.size()) {
            Set<Integer> newBeams = new HashSet<>();
            for (int currentBeam : currentBeams) {
                if (input.get(currentRow).get(currentBeam).equals("^")) {
                    newBeams.add(currentBeam - 1);
                    newBeams.add(currentBeam + 1);
                    count++;
                } else {
                    newBeams.add(currentBeam);
                }
            }
            currentBeams = newBeams;
            currentRow++;
        }
        System.out.println("Part 1: " + count);
    }

    private static void task2(final List<List<String>> input) {
        int currentRow = 0;
        Map<Integer, Long> currentBeams = new HashMap<>();
        currentBeams.put(input.getFirst().indexOf("S"), 1L);
        while (currentRow != input.size()) {
            Map<Integer, Long> newBeams = new HashMap<>();
            for (Map.Entry<Integer, Long> entry : currentBeams.entrySet()) {
                if (input.get(currentRow).get(entry.getKey()).equals("^")) {
                    CollectionUtils.putOrApply(newBeams, entry.getKey() - 1, entry.getValue(), Long::sum);
                    CollectionUtils.putOrApply(newBeams, entry.getKey() + 1, entry.getValue(), Long::sum);
                } else {
                    CollectionUtils.putOrApply(newBeams, entry.getKey(), entry.getValue(), Long::sum);
                }
            }
            currentBeams = newBeams;
            currentRow++;
        }
        System.out.println("Part 2: " + currentBeams.values().stream().mapToLong(Long::longValue).sum());
    }
}

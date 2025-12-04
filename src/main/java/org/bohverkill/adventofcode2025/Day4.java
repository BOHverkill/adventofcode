package org.bohverkill.adventofcode2025;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day4 {
    static void main() {
//        final String name = "/2025/Day4_example";
        final String name = "/2025/Day4_input";
        final List<List<String>> grid = Utils.getInputLines(name).map(StringUtils::parseCharToStringList).toList();
        task1(grid);
        task2(grid);
    }

    private static void task1(final List<List<String>> grid) {
        long sum = 0;
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (grid.get(i).get(j).equals("@") && getRolls(grid, i, j) < 4) {
                    sum++;
                }
            }
        }
        System.out.println("Part 1: " + sum);
    }

    private static long getRolls(final List<List<String>> grid, int i, int j) {
        return Stream.of(Pair.of(i - 1, j - 1), Pair.of(i - 1, j), Pair.of(i - 1, j + 1), Pair.of(i, j - 1), Pair.of(i, j + 1), Pair.of(i + 1, j - 1), Pair.of(i + 1, j), Pair.of(i + 1, j + 1)).filter(pair -> isValid(grid, pair)).filter(pair -> isRoll(grid, pair)).count();
    }

    private static boolean isValid(final List<List<String>> grid, Pair<Integer, Integer> pair) {
        return pair.a() >= 0 && pair.a() < grid.size() && pair.b() >= 0 && pair.b() < grid.get(pair.a()).size();
    }

    private static boolean isRoll(final List<List<String>> grid, Pair<Integer, Integer> pair) {
        return grid.get(pair.a()).get(pair.b()).equals("@");
    }

    private static void task2(final List<List<String>> origGrid) {
        List<List<String>> grid = origGrid;
        long sum = 0;
        boolean changed = true;
        while (changed) {
            changed = false;
            List<List<String>> newGrid = new ArrayList<>();
            for (int i = 0; i < grid.size(); i++) {
                List<String> newRow = new ArrayList<>();
                for (int j = 0; j < grid.get(i).size(); j++) {
                    if (grid.get(i).get(j).equals("@")) {
                        long rolls = getRolls(grid, i, j);
                        if (rolls < 4) {
                            newRow.add(".");
                            sum++;
                            changed = true;
                        } else {
                            newRow.add("@");
                        }
                    } else {
                        newRow.add(".");
                    }
                }
                newGrid.add(newRow);
            }
            grid = newGrid;
        }
        System.out.println("Part 2: " + sum);
    }
}

package org.bohverkill.adventofcode2023;


import java.util.ArrayList;
import java.util.List;

public class Day13Part2 {
    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day13_example");
        String input = Utils.getInput("/2023/Day13_input");
        final List<List<String>> patterns = Day13Part1.mapToPatterns(input);
        final int sum = patterns.stream().mapToInt(Day13Part2::solve).sum();
        System.out.println("Day 13 Part 2: " + sum);
    }

    public static int solve(List<String> pattern) {
        final int ignore = Day13Part1.solve(pattern);
        for (int i = 0; i < pattern.size(); i++) {
            for (int j = 0; j < pattern.get(i).length(); j++) {
                final List<String> tryPattern = new ArrayList<>(pattern);
                final char c = tryPattern.get(i).charAt(j);
                if (c == '#') {
                    tryPattern.set(i, Utils.replaceCharAt(tryPattern.get(i), j, '.'));
                } else {
                    tryPattern.set(i, Utils.replaceCharAt(tryPattern.get(i), j, '#'));
                }
                int t = solve(tryPattern, ignore);
                if (t != -1) {
                    return t;
                }
            }
        }
        throw new IllegalStateException("No mirror: " + pattern);
    }

    public static int solve(List<String> pattern, int ignore) {
        final int lineLength = pattern.getFirst().length();
        for (int i = 0; i < pattern.size() - 1; i++) {
            if (pattern.get(i).equals(pattern.get(i + 1))) {
                boolean matches = true;
                for (int j = 1; j < Math.min(i + 1, pattern.size() - (i + 1)); j++) {
                    if (!pattern.get(i - j).equals(pattern.get(i + 1 + j))) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    final int solution = (i + 1) * 100;
                    if (solution != ignore) {
                        return solution;
                    }
                }
            }
        }
        for (int i = 0; i < lineLength - 1; i++) {
            if (Day13Part1.getColumn(pattern, i).equals(Day13Part1.getColumn(pattern, i + 1))) {
                boolean matches = true;
                for (int j = 1; j < Math.min(i + 1, lineLength - (i + 1)); j++) {
                    if (!Day13Part1.getColumn(pattern, i - j).equals(Day13Part1.getColumn(pattern, i + 1 + j))) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    int solution = i+1;
                    if (solution != ignore) {
                        return solution;
                    }
                }
            }
        }
        return -1;
    }
}

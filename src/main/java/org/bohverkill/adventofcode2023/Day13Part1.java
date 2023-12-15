package org.bohverkill.adventofcode2023;


import java.util.Arrays;
import java.util.List;

public class Day13Part1 {
    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day13_example");
        String input = Utils.getInput("/2023/Day13_input");
        final List<List<String>> patterns = mapToPatterns(input);
        final int sum = patterns.stream().mapToInt(Day13Part1::solve).sum();
        System.out.println("Day 13 Part 1: " + sum);
    }

    public static List<List<String>> mapToPatterns(String input) {
        final String[] split = input.split("\n\n");
        return Arrays.stream(split).map(string -> List.of(string.split("\n"))).toList();
    }

    public static int solve(List<String> pattern) {
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
                    return (i + 1) * 100;
                }
            }
        }
        for (int i = 0; i < lineLength - 1; i++) {
            if (getColumn(pattern, i).equals(getColumn(pattern, i + 1))) {
                boolean matches = true;
                for (int j = 1; j < Math.min(i + 1, lineLength - (i + 1)); j++) {
                    if (!getColumn(pattern, i - j).equals(getColumn(pattern, i + 1 + j))) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    return i+1;
                }
            }
        }
        throw new IllegalStateException("No mirror");
    }

    public static String getColumn(List<String> lines, int column) {
        StringBuilder content = new StringBuilder();
        for (String line : lines) {
            content.append(line.charAt(column));
        }
        return content.toString();
    }
}

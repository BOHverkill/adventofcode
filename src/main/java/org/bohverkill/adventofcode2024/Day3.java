package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
    private static final Pattern MULTIPLY_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    private static final Pattern MULTIPLY_DO_DONT_PATTERN = Pattern.compile("do\\(\\)|don't\\(\\)|(mul\\(\\d{1,3},\\d{1,3}\\))");

    public static void main(String[] args) {
//        final String input = Utils.getInput("/2024/Day3_1_example");
//        final String input = Utils.getInput("/2024/Day3_2_example");
        final String input = Utils.getInput("/2024/Day3_input");
        task1(input);
        task2(input);
    }

    private static void task1(final String input) {
        final int sum = Utils.getAllMatches(MULTIPLY_PATTERN, input).map(Day3::parse).mapToInt(value -> value.a() * value.b()).sum();
        System.out.println("Part 1: " + sum);
    }

    private static Pair<Integer, Integer> parse(String mulString) {
        final Matcher mulMatcher = Utils.getMatcher(MULTIPLY_PATTERN, mulString);
        int x = Integer.parseInt(mulMatcher.group(1));
        int y = Integer.parseInt(mulMatcher.group(2));
        return Pair.of(x, y);
    }

    private static void task2(final String input) {
        final Matcher mulMatcher = MULTIPLY_DO_DONT_PATTERN.matcher(input);
        boolean enabled = true;
        int sum = 0;
        while (mulMatcher.find()) {
            final String group = mulMatcher.group();
            if (group.equals("do()")) {
                enabled = true;
            } else if (group.equals("don't()")) {
                enabled = false;
            } else {
                if (enabled) {
                    final Pair<Integer, Integer> parse = parse(group);
                    sum += parse.a() * parse.b();
                }
            }
        }

        System.out.println("Part 2: " + sum);
    }
}

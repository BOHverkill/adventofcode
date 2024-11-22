package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.Arrays;
import java.util.stream.Stream;

public class Day12Part1 {
    public static void main(String[] args) {
//        Stream<String> input = Utils.getInputLines("/2023/Day12_example");
        Stream<String> input = Utils.getInputLines("/2023/Day12_input");
        final long sum = input.map(Day12Part1::mapToRow).mapToLong(Day12Part1::solve).sum();
        System.out.println("Day 12 Part 1: " + sum);
    }

    public static Row mapToRow(String input) {
        final String[] split = input.split(" ");
        if (split.length != 2) {
            throw new IllegalStateException("Malformed input: " + input);
        }
        return new Row(split[0], Arrays.stream(split[1].split(",")).mapToInt(Integer::parseInt).toArray());
    }

    public static long solve(Row row) {
        return generatePermutation(row.springs(), row.groups());
    }

    public static boolean check(String springs, int[] groups) {
        final int[] springGroups = Arrays.stream(springs.split("\\.")).filter(s -> !s.isEmpty()).mapToInt(String::length).toArray();
        return Arrays.equals(springGroups, groups);
    }

    public static boolean partialCheck(String springs, int[] groups, int index) {
        final int[] springGroups = Arrays.stream(springs.substring(0, index).split("\\.")).filter(s -> !s.isEmpty()).mapToInt(String::length).toArray();
        if (springGroups.length > groups.length) {
            return false;
        }
        for (int i = 0; i < springGroups.length; i++) {
            if (springGroups[i] > groups[i]) {
                return false;
            }
        }
        return true;
    }

    public static long generatePermutation(String springs, int[] groups) {
        final int i = springs.indexOf('?');
        if (i == -1) {
            if (check(springs, groups)) {
                return 1;
            }
        } else if (partialCheck(springs, groups, i)) {
            long out = generatePermutation(Utils.replaceCharAt(springs, i, '#'), groups);
            out += generatePermutation(Utils.replaceCharAt(springs, i, '.'), groups);
            return out;
        }
        return 0;
    }

//    unoptimized version
//    public static long solve(Row row) {
//        final ArrayList<String> permutations = new ArrayList<>();
//        generatePermutation(row.springs, 0, permutations);
//        return permutations.stream().filter(permutation -> check(permutation, row.groups())).count();
//    }
//
//    public static void generatePermutation(String springs, int position, List<String> permutations) {
//        final int i = springs.indexOf('?', position);
//        if (i == -1) {
//            permutations.add(springs);
//        } else {
//            generatePermutation(replaceCharAt(springs, i, '#'), i, permutations);
//            generatePermutation(replaceCharAt(springs, i, '.'), i, permutations);
//        }
//    }

    public record Row(String springs, int[] groups) {
    }
}

package org.bohverkill.adventofcode2024;

import org.bohverkill.utils.Utils;

public class Day4 {
    public static void main(String[] args) {
//        final String[] input = Utils.getInput("/2024/Day4_example").split("\n");
        final String[] input = Utils.getInput("/2024/Day4_input").split("\n");
        task1(input);
        task2(input);
    }

    private static void task1(final String[] input) {
        int sum = 0;
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length(); j++) {
                if (input[i].charAt(j) == 'X') {
                    // horizontal
                    if (stringArraySaveCharAt(input, i, j + 1) == 'M' && stringArraySaveCharAt(input, i, j + 2) == 'A' && stringArraySaveCharAt(input, i, j + 3) == 'S') {
                        sum++;
                    }
                    // reverse
                    if (stringArraySaveCharAt(input, i, j - 1) == 'M' && stringArraySaveCharAt(input, i, j - 2) == 'A' && stringArraySaveCharAt(input, i, j - 3) == 'S') {
                        sum++;
                    }
                    // vertical
                    if (stringArraySaveCharAt(input, i + 1, j) == 'M' && stringArraySaveCharAt(input, i + 2, j) == 'A' && stringArraySaveCharAt(input, i + 3, j) == 'S') {
                        sum++;
                    }
                    // reverse
                    if (stringArraySaveCharAt(input, i - 1, j) == 'M' && stringArraySaveCharAt(input, i - 2, j) == 'A' && stringArraySaveCharAt(input, i - 3, j) == 'S') {
                        sum++;
                    }
                    // diagonal
                    if (stringArraySaveCharAt(input, i + 1, j + 1) == 'M' && stringArraySaveCharAt(input, i + 2, j + 2) == 'A' && stringArraySaveCharAt(input, i + 3, j + 3) == 'S') {
                        sum++;
                    }
                    // reverse
                    if (stringArraySaveCharAt(input, i - 1, j - 1) == 'M' && stringArraySaveCharAt(input, i - 2, j - 2) == 'A' && stringArraySaveCharAt(input, i - 3, j - 3) == 'S') {
                        sum++;
                    }
                    // diagonal
                    if (stringArraySaveCharAt(input, i + 1, j - 1) == 'M' && stringArraySaveCharAt(input, i + 2, j - 2) == 'A' && stringArraySaveCharAt(input, i + 3, j - 3) == 'S') {
                        sum++;
                    }
                    // reverse
                    if (stringArraySaveCharAt(input, i - 1, j + 1) == 'M' && stringArraySaveCharAt(input, i - 2, j + 2) == 'A' && stringArraySaveCharAt(input, i - 3, j + 3) == 'S') {
                        sum++;
                    }
                }
            }
        }
        System.out.println("Part 1: " + sum);
    }

    private static void task2(final String[] input) {
        int sum = 0;
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length(); j++) {
                if (input[i].charAt(j) == 'A') {
                    if (stringArraySaveCharAt(input, i - 1, j - 1) == 'M' && stringArraySaveCharAt(input, i + 1, j + 1) == 'S' && stringArraySaveCharAt(input, i + 1, j - 1) == 'M' && stringArraySaveCharAt(input, i - 1, j + 1) == 'S') {
                        sum++;
                    }
                    if (stringArraySaveCharAt(input, i - 1, j - 1) == 'M' && stringArraySaveCharAt(input, i + 1, j + 1) == 'S' && stringArraySaveCharAt(input, i - 1, j + 1) == 'M' && stringArraySaveCharAt(input, i + 1, j - 1) == 'S') {
                        sum++;
                    }
                    if (stringArraySaveCharAt(input, i + 1, j + 1) == 'M' && stringArraySaveCharAt(input, i - 1, j - 1) == 'S' && stringArraySaveCharAt(input, i + 1, j - 1) == 'M' && stringArraySaveCharAt(input, i - 1, j + 1) == 'S') {
                        sum++;
                    }
                    if (stringArraySaveCharAt(input, i + 1, j + 1) == 'M' && stringArraySaveCharAt(input, i - 1, j - 1) == 'S' && stringArraySaveCharAt(input, i - 1, j + 1) == 'M' && stringArraySaveCharAt(input, i + 1, j - 1) == 'S') {
                        sum++;
                    }
                }
            }
        }
        System.out.println("Part 2: " + sum);
    }

    private static char stringArraySaveCharAt(String[] strings, int i, int j) {
        if (i >= 0 && i < strings.length && j >= 0 && j < strings[i].length()) {
            return strings[i].charAt(j);
        } else {
            return '.';
        }
    }
}

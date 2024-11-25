package org.bohverkill.adventofcode2017;

import org.bohverkill.utils.Utils;

public class Day5 {
    public static void main(String[] args) {
//        final int[] input = Utils.getInputLines("/2017/Day5_example").mapToInt(Integer::parseInt).toArray();
        final int[] input = Utils.getInputLines("/2017/Day5_input").mapToInt(Integer::parseInt).toArray();
        task1(input.clone());
        task2(input.clone());
    }

    private static void task1(final int[] input) {
        int steps = 0;
        int curr = 0;
        while (curr >= 0 && curr < input.length) {
//            print(input, curr);
            int next = curr + input[curr];
            input[curr] = input[curr] + 1;
            curr = next;
            steps++;
        }
        System.out.println("Part 1: " + steps);
    }

    private static void task2(final int[] input) {
        int steps = 0;
        int curr = 0;
        while (curr >= 0 && curr < input.length) {
//            print(input, curr);
            int next = curr + input[curr];
            if (input[curr] >= 3) {
                input[curr] = input[curr] - 1;
            } else {
                input[curr] = input[curr] + 1;
            }
            curr = next;
            steps++;
        }
        System.out.println("Part 2: " + steps);
    }

    private static void print(final int[] input, int current) {
        for (int i = 0; i < input.length; i++) {
            if (i == current) {
                System.out.print("(" + input[i] + ") ");
            } else {
                System.out.print(input[i] + " ");
            }
        }
        System.out.println();
    }
}

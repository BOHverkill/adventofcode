package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

public class Day15Part1 {

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day15_example");
        String input = Utils.getInput("/2023/Day15_input");
        final int sum = Utils.getSplit(input, ",").mapToInt(Day15Part1::hash).sum();
        System.out.println("Day 15 Part 1: " + sum);
    }

    public static char hash(String input) {
        char currentValue = 0;
        for (int i = 0; i < input.length(); i++) {
            currentValue += input.charAt(i);
            currentValue *= 17;
            currentValue %= 256;
        }
        return currentValue;
    }
}

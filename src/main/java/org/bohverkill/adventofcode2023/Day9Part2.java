package org.bohverkill.adventofcode2023;

import org.bohverkill.utils.Utils;

import java.util.List;
import java.util.stream.Stream;

public class Day9Part2 {
    public static void main(String[] args) {
//        Stream<String> lines = Utils.getInputLines("/2023/Day9_example");
        Stream<String> lines = Utils.getInputLines("/2023/Day9_input");
        System.out.println("Day9 Part 2: " + lines.map(Day9Part1::parseLine).mapToInt(Day9Part2::predictPreviousValue).sum());
    }

    private static int predictPreviousValue(List<Integer> history) {
        return Day9Part1.predictValue(history, List::addFirst, List::getFirst, Day9Part2::minus);
    }

//    private static void predictPrevious(int i, List<List<Integer>> differences) {
//        differences.get(i - 1).addFirst(differences.get(i - 1).getFirst() - differences.get(i).getFirst());
//    }

    public static int minus(int a, int b) {
        return a - b;
    }
}

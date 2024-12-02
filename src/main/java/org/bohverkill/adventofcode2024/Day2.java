package org.bohverkill.adventofcode2024;

import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.IntStream;

public class Day2 {
    public static void main(String[] args) {
//        final List<String> input = Utils.getInputList("/2024/Day2_example");
         final List<String> input = Utils.getInputList("/2024/Day2_input");
        task1(input);
        task2(input);
    }

    private static void task1(final List<String> input) {
        final long res = input.stream().map(s -> Arrays.stream(s.split(" ")).map(Integer::parseInt).toList()).filter(Day2::checkReport).count();
        System.out.println("Part 1: " + res);
    }

    private static boolean checkReport(List<Integer> report) {
        final List<Integer> c1 = new ArrayList<>(report);
        c1.sort(Integer::compareTo);
        final List<Integer> c2 = new ArrayList<>(report);
        c2.sort(Collections.reverseOrder());
        if (!c1.equals(report) && !c2.equals(report)) {
            return false;
        } else {
            return IntStream.range(0, report.size() - 1).map(i -> Math.abs(report.get(i) - report.get(i + 1))).noneMatch(distance -> distance < 1 || distance > 3);
        }
    }

    private static void task2(final List<String> input) {
        final long res = input.stream().map(s -> Arrays.stream(s.split(" ")).map(Integer::parseInt).toList()).filter(Day2::checkDampenerReport).count();
        System.out.println("Part 2: " + res);
    }

    private static boolean checkDampenerReport(List<Integer> report) {
        if (checkReport(report)) {
            return true;
        }
        for (int i = 0; i < report.size(); i++) {
            List<Integer> singleIgnoredReport = new ArrayList<>(report);
            singleIgnoredReport.remove(i);
            if (checkReport(singleIgnoredReport)) {
                return true;
            }
        }
        return false;
    }
}

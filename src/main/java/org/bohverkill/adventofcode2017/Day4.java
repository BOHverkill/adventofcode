package org.bohverkill.adventofcode2017;

import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Day4 {
    public static void main(String[] args) {
        final String[] input = Utils.getInputArray("/2017/Day4_input");
        task1(input);
        task2(input);
    }

    private static void task1(String[] input) {
        int res = (int) Arrays.stream(input).map(l -> l.split(" ")).filter(pwList -> new HashSet<>(List.of(pwList)).size() == pwList.length).count();
        System.out.println(res);
    }

    private static void task2(String[] input) {
        int res = 0;
        for (String l : input) {
            final List<String> pwList = List.of(l.split(" "));
            final HashSet<String> sorted = pwList.stream().map(String::toCharArray).map(Day4::sorted).map(String::new).collect(Collectors.toCollection(HashSet::new));
            if (pwList.size() == sorted.size()) {
                res++;
            }
        }
        System.out.println(res);
    }

    private static char[] sorted(char[] input) {
        Arrays.sort(input);
        return input;
    }
}

package org.bohverkill.adventofcode2023;

import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day9Part1 {
    private static final Pattern LINE_PATTERN = Pattern.compile("([0-9-]+)");

    public static void main(String[] args) {
//        Stream<String> lines = Utils.getInputLines("/2023/Day9_example");
        Stream<String> lines = Utils.getInputLines("/2023/Day9_input");
        System.out.println("Day9 Part 1: " + lines.map(Day9Part1::parseLine).mapToInt(Day9Part1::predictNextValue).sum());
    }

    public static List<Integer> parseLine(String line) {
        return Utils.getAllMatches(LINE_PATTERN, line).map(Integer::parseInt).toList();
    }

//    private static int predictNextValue(List<Integer> history) {
//        final List<List<Integer>> differences = calculateDifferences(history);
//        differences.getLast().add(0);
//        IntStream.iterate(differences.size() - 1, i -> i > 0, i -> i - 1).forEach(i -> differences.get(i - 1).add(differences.get(i - 1).getLast() + differences.get(i).getLast());
//        return differences.getFirst().getLast();
//    }

    private static int predictNextValue(List<Integer> history) {
        return predictValue(history, List::add, List::getLast, Integer::sum);
    }

    public static int predictValue(List<Integer> history, ObjIntConsumer<List<Integer>> addFunction, ToIntFunction<List<Integer>> getFunction, IntBinaryOperator combineOperator) {
        final List<List<Integer>> differences = calculateDifferences(history);
        addFunction.accept(differences.getLast(), 0);
        IntStream.iterate(differences.size() - 1, i -> i > 0, i -> i - 1).forEach(i -> addFunction.accept(differences.get(i - 1), combineOperator.applyAsInt(getFunction.applyAsInt(differences.get(i - 1)), getFunction.applyAsInt(differences.get(i)))));
        return getFunction.applyAsInt(differences.getFirst());
    }

    public static List<List<Integer>> calculateDifferences(List<Integer> history) {
        List<List<Integer>> differences = new ArrayList<>();
        differences.add(new ArrayList<>(history));
        while (!differences.getLast().stream().allMatch(i -> i.equals(0))) {
            differences.add(new ArrayList<>(IntStream.range(0, differences.getLast().size() - 1).mapToObj(i -> differences.getLast().get(i + 1) - differences.getLast().get(i)).toList()));
        }
        return differences;
    }
}

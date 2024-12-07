package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.MathUtils;
import org.bohverkill.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.LongBinaryOperator;

public class Day7 {

    private static final LongBinaryOperator[] OPERATORS_TASK1 = new LongBinaryOperator[]{MathUtils::plus, MathUtils::times};
    private static final LongBinaryOperator[] OPERATORS_TASK2 = new LongBinaryOperator[]{MathUtils::plus, MathUtils::times, Day7::concatenation};

    public static void main(String[] args) {
//        final String path = "/2024/Day7_example";
        final String path = "/2024/Day7_input";
        final List<Pair<Long, List<Long>>> input = Utils.getInputLines(path).map(Day7::parse).toList();
        task1(input);
        task2(input);
    }

    private static void task1(final List<Pair<Long, List<Long>>> input) {
        long sum = input.stream().filter(equation -> equationCanBeTrue(equation, OPERATORS_TASK1)).mapToLong(Pair::a).sum();
        System.out.println("Part 1: " + sum);
    }

    private static boolean equationCanBeTrue(Pair<Long, List<Long>> equation, LongBinaryOperator[] operators) {
        final Long testValue = equation.a();
        final List<Long> numbers = equation.b();

        return combination(numbers.getFirst(), numbers.subList(1, numbers.size()), testValue, operators);
    }

    private static boolean combination(long intermediateResult, List<Long> numbers, long testValue, LongBinaryOperator[] operators) {
        if (numbers.isEmpty()) {
            return intermediateResult == testValue;
        }
        for (LongBinaryOperator operator : operators) {
            long newResult = operator.applyAsLong(intermediateResult, numbers.getFirst());
            if (combination(newResult, numbers.subList(1, numbers.size()), testValue, operators)) {
                return true;
            }
        }
        return false;
    }

    private static void task2(final List<Pair<Long, List<Long>>> input) {
        long sum = input.stream().filter(equation -> equationCanBeTrue(equation, OPERATORS_TASK2)).mapToLong(Pair::a).sum();
        System.out.println("Part 2: " + sum);
    }

    public static long concatenation(long a, long b) {
        return Long.parseLong(Long.toString(a) + b);
    }

    private static Pair<Long, List<Long>> parse(final String line) {
        final String[] split = line.split(":");
        final long testValue = Long.parseLong(split[0]);
        final List<Long> numbers = Arrays.stream(split[1].trim().split(" ")).map(Long::parseLong).toList();
        return Pair.of(testValue, numbers);
    }
}

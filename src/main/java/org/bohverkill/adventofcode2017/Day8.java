package org.bohverkill.adventofcode2017;

import org.bohverkill.utils.MathUtils;
import org.bohverkill.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.IntBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 {
    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("^([a-z]+) (inc|dec) ([0-9-]+) if ([a-z]+) (>|<|>=|<=|==|!=) ([0-9-]+)$");

    public static void main(String[] args) {
//        final List<Instruction> instructions = Utils.getInputLines("/2017/Day8_example").map(Day8::parse).toList();
        final List<Instruction> instructions = Utils.getInputLines("/2017/Day8_input").map(Day8::parse).toList();
        task1(instructions);
        task2(instructions);
    }

    private static void task1(final List<Instruction> input) {
        final Map<String, Integer> registers = new HashMap<>();

        input.stream().filter(instruction -> instruction.conditionComparisonOperator().action().test(registers.getOrDefault(instruction.conditionRegister(), 0), instruction.conditionComparisonAmount())).forEach(instruction -> registers.put(instruction.modifyRegister(), instruction.modifyOperation().action().applyAsInt(registers.getOrDefault(instruction.modifyRegister(), 0), instruction.modifyAmount())));

        System.out.println("Part 1: " + registers.values().stream().mapToInt(Integer::intValue).max().orElseThrow());
    }

    private static void task2(final List<Instruction> input) {
        final Map<String, Integer> registers = new HashMap<>();

        int max = Integer.MIN_VALUE;
        for (Instruction instruction : input) {
            if (instruction.conditionComparisonOperator().action().test(registers.getOrDefault(instruction.conditionRegister(), 0), instruction.conditionComparisonAmount())) {
                final int value = instruction.modifyOperation().action().applyAsInt(registers.getOrDefault(instruction.modifyRegister(), 0), instruction.modifyAmount());
                registers.put(instruction.modifyRegister(), value);
                if (value > max) {
                    max = value;
                }
            }
        }

        System.out.println("Part 2: " + max);
    }

    private static Instruction parse(String line) {
        final Matcher instructionMatcher = Utils.getMatcher(INSTRUCTION_PATTERN, line);
        final String modifyRegister = instructionMatcher.group(1);
        final ModifyOperation modifyOperation = ModifyOperation.parse(instructionMatcher.group(2));
        final int modifyAmount = Integer.parseInt(instructionMatcher.group(3));
        final String conditionRegister = instructionMatcher.group(4);
        final ConditionComparisonOperator conditionComparisonOperator = ConditionComparisonOperator.parse(instructionMatcher.group(5));
        final int conditionComparisonAmount = Integer.parseInt(instructionMatcher.group(6));
        return Instruction.of(modifyRegister, modifyOperation, modifyAmount, conditionRegister, conditionComparisonOperator, conditionComparisonAmount);
    }

    private enum ModifyOperation {
        INCREASE(Integer::sum), DECREASE(MathUtils::minus);

        private final IntBinaryOperator action;

        ModifyOperation(IntBinaryOperator action) {
            this.action = action;
        }

        public static ModifyOperation parse(String operation) {
            return switch (operation) {
                case "inc" -> INCREASE;
                case "dec" -> DECREASE;
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            };
        }

        public IntBinaryOperator action() {
            return this.action;
        }
    }

    private enum ConditionComparisonOperator {
        GREATER_THAN_TO((a, b) -> a > b), LESS_THAN_TO((a, b) -> a < b), GREATER_THAN_OR_EQUAL_TO((a, b) -> a >= b), LESS_THAN_OR_EQUAL_TO((a, b) -> a <= b), EQUAL_TO(Integer::equals), NOT_EQUAL_TO((a, b) -> !a.equals(b));

        private final BiPredicate<Integer, Integer> action;

        ConditionComparisonOperator(BiPredicate<Integer, Integer> action) {
            this.action = action;
        }

        public static ConditionComparisonOperator parse(String operator) {
            return switch (operator) {
                case ">" -> GREATER_THAN_TO;
                case "<" -> LESS_THAN_TO;
                case ">=" -> GREATER_THAN_OR_EQUAL_TO;
                case "<=" -> LESS_THAN_OR_EQUAL_TO;
                case "==" -> EQUAL_TO;
                case "!=" -> NOT_EQUAL_TO;
                default -> throw new IllegalStateException("Unexpected value: " + operator);
            };
        }

        public BiPredicate<Integer, Integer> action() {
            return this.action;
        }
    }

    private record Instruction(String modifyRegister, ModifyOperation modifyOperation, int modifyAmount,
                               String conditionRegister, ConditionComparisonOperator conditionComparisonOperator,
                               int conditionComparisonAmount) {
        private static Instruction of(String modifyRegister, ModifyOperation modifyOperation, int modifyAmount, String conditionRegister, ConditionComparisonOperator conditionComparisonOperator, int conditionComparisonAmount) {
            return new Instruction(modifyRegister, modifyOperation, modifyAmount, conditionRegister, conditionComparisonOperator, conditionComparisonAmount);
        }
    }
}

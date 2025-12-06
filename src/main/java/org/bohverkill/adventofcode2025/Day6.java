package org.bohverkill.adventofcode2025;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.MathUtils;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Gatherers;

import static org.bohverkill.utils.AssertionUtils.assertEquals;

public class Day6 {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");
    private static final Pattern SYMBOL_PATTERN = Pattern.compile("([+*])");

    static void main() {
//        final String name = "/2025/Day6_example";
        final String name = "/2025/Day6_input";
        final String[] input = Utils.getInputArray(name);
        task1(input);
        task2(input);
    }

    private static void task1(final String[] input) {
        final List<Pair<List<Long>, Symbol>> l = parseTask1(input);
        long sum = l.stream().mapToLong(p -> p.a().stream().gather(Gatherers.fold(p.b()::getNeutralElement, p.b().getOp()::applyAsLong)).findFirst().orElseThrow()).sum();
        System.out.println("Part 1: " + sum);
    }

    private static void task2(final String[] input) {
        final List<Pair<List<Long>, Symbol>> l = parseTask2(input);
        long sum = l.stream().mapToLong(p -> p.a().stream().gather(Gatherers.fold(p.b()::getNeutralElement, p.b().getOp()::applyAsLong)).findFirst().orElseThrow()).sum();
        System.out.println("Part 2: " + sum);
    }

    private static List<Pair<List<Long>, Symbol>> parseTask1(String[] lines) {
        List<List<Long>> numbers = new ArrayList<>();
        for (int i = 0; i < lines.length - 1; i++) {
            final Matcher numberMatcher = NUMBER_PATTERN.matcher(lines[i]);
            List<Long> n = new ArrayList<>();
            while (numberMatcher.find()) {
                final String number = numberMatcher.group();
                n.add(Long.parseLong(number));
            }
            numbers.add(n);
        }
        final Matcher symbolMatcher = SYMBOL_PATTERN.matcher(lines[lines.length - 1]);
        List<Symbol> symbols = new ArrayList<>();
        while (symbolMatcher.find()) {
            final String symbol = symbolMatcher.group();
            symbols.add(Symbol.parse(symbol));
        }

        assertEquals(numbers.getFirst().size(), symbols.size());
        List<Pair<List<Long>, Symbol>> pairs = new ArrayList<>();
        for (int i = 0; i < numbers.getFirst().size(); i++) {
            List<Long> number = new ArrayList<>();
            for (List<Long> longs : numbers) {
                number.add(longs.get(i));
            }
            pairs.add(Pair.of(number, symbols.get(i)));
        }

        return pairs;
    }

    private static List<Pair<List<Long>, Symbol>> parseTask2(String[] lines) {
        final Matcher symbolMatcher = SYMBOL_PATTERN.matcher(lines[lines.length - 1]);
        List<Symbol> symbols = new ArrayList<>();
        List<Integer> starts = new ArrayList<>();
        while (symbolMatcher.find()) {
            final String symbol = symbolMatcher.group();
            symbols.add(Symbol.parse(symbol));
            starts.add(symbolMatcher.start());
        }

        List<Pair<List<Long>, Symbol>> pairs = new ArrayList<>();
        int last = lines[0].length() - 1;
        for (int i = starts.size() - 1; i >= 0; i--) {
            final int curr = starts.get(i);

            List<Long> numbers = new ArrayList<>();
            for (int j = last; j >= curr; j--) {
                long number = 0;
                for (int k = 0; k < lines.length - 1; k++) {
                    final char c = lines[k].charAt(j);
                    if (c != ' ') {
                        number *= 10;
                        number += Character.getNumericValue(c);
                    }
                }
                numbers.add(number);
            }

            pairs.add(Pair.of(numbers, symbols.get(i)));
            last = curr - 2;
        }

        return pairs;
    }

    public enum Symbol {
        PLUS(MathUtils::plus, 0), ASTERISK(MathUtils::times, 1);

        private final LongBinaryOperator op;
        private final long neutralElement;

        Symbol(LongBinaryOperator op, long neutralElement) {
            this.op = op;
            this.neutralElement = neutralElement;
        }

        public static Symbol parse(String symbol) {
            return switch (symbol) {
                case "+" -> PLUS;
                case "*" -> ASTERISK;
                default -> throw new IllegalStateException("Unexpected value: " + symbol);
            };
        }

        public LongBinaryOperator getOp() {
            return this.op;
        }

        public long getNeutralElement() {
            return this.neutralElement;
        }
    }
}

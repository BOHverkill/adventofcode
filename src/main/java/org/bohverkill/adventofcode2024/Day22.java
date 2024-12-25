package org.bohverkill.adventofcode2024;

import org.bohverkill.utils.Utils;

import java.util.*;

public class Day22 {
    public static void main(String[] args) {
//        final String path = "/2024/Day22_example";
        final String path = "/2024/Day22_input";
        final List<Long> initialSecretNumbers = Utils.getInputLines(path).map(Long::parseLong).toList();
        task1(initialSecretNumbers);
        task2(initialSecretNumbers);
    }

    private static void task1(final List<Long> initialSecretNumbers) {
        long sum = 0;
        for (long secretNumber : initialSecretNumbers) {
            for (int i = 0; i < 2000; i++) {
                secretNumber = secretNumberProcess(secretNumber);
            }
            sum += secretNumber;
        }
        System.out.println("Part 1: " + sum);
    }

    private static long secretNumberProcess(long secretNumber) {
        secretNumber = ((secretNumber * 64) ^ secretNumber) % 16777216;
        secretNumber = ((secretNumber / 32) ^ secretNumber) % 16777216;
        secretNumber = ((secretNumber * 2048) ^ secretNumber) % 16777216;
        return secretNumber;
    }

    private static void task2(final List<Long> initialSecretNumbers) {
        final Map<Sequence, Long> sequenceMap = new HashMap<>();
        for (long initialSecretNumber : initialSecretNumbers) {
            long secretNumber = initialSecretNumber;
            long lastNumber = initialSecretNumber % 10;
            List<Long> secretNumbers = new ArrayList<>();
            List<Long> diff = new ArrayList<>();
            for (int i = 0; i < 2000; i++) {
                secretNumber = secretNumberProcess(secretNumber);
                long secretNumberLastDigit = secretNumber % 10;
                secretNumbers.add(secretNumberLastDigit);
                diff.add(lastNumber - secretNumberLastDigit);
                lastNumber = secretNumberLastDigit;
            }
            final List<Sequence> sequences = new ArrayList<>();
            for (int i = 0; i < diff.size() - 3; i++) {
                sequences.add(Sequence.of(diff.get(i), diff.get(i + 1), diff.get(i + 2), diff.get(i + 3)));
            }
            final Set<Sequence> visited = new HashSet<>();
            for (int i = 0; i < sequences.size(); i++) {
                final Sequence sequence = sequences.get(i);
                if (!visited.contains(sequence)) {
                    sequenceMap.put(sequence, sequenceMap.getOrDefault(sequence, 0L) + secretNumbers.get(i + 3));
                    visited.add(sequence);
                }
            }
        }
        long maxBanana = sequenceMap.values().stream().mapToLong(l -> l).max().orElseThrow();
        System.out.println("Part 2: " + maxBanana);

    }

    private record Sequence(long number1, long number2, long number3, long number4) {
        private static Sequence of(long number1, long number2, long number3, long number4) {
            return new Sequence(number1, number2, number3, number4);
        }

        @Override
        public String toString() {
            return "[" + number1 + ", " + number2 + ", " + number3 + ", " + number4 + ']';
        }
    }
}

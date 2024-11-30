package org.bohverkill.adventofcode2017;

import org.bohverkill.utils.ArrayUtils;
import org.bohverkill.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 {
    //    private static final int LIST_SIZE = 5;
    private static final int LIST_SIZE = 256;
    private static final int ROUNDS = 64;
    private static final int BLOCKS = 16;
    private static final List<Character> STANDARD_LENGTH_SUFFIX = List.of((char) 17, (char) 31, (char) 73, (char) 47, (char) 23);

    public static void main(String[] args) {
//        final String input = Utils.getInput("/2017/Day10_example");
        final String input = Utils.getInput("/2017/Day10_input").trim();
        final List<Character> lengths = Utils.getSplit(input, ",").map(String::trim).map(Integer::parseInt).map(integer -> (char) integer.intValue()).toList();
        task1(lengths);
        task2(input);
    }

    private static void task1(final List<Character> input) {
        final char[] list = round(input, 0, 0, createList()).list();
        System.out.println("Part 1: " + list[0] * list[1]);
    }

    private static char[] createList() {
        char[] list = new char[LIST_SIZE];
        for (char i = 0; i < list.length; i++) {
            list[i] = i;
        }
        return list;
    }

    private static Round round(final List<Character> input, int currentPosition, int skipSize, char[] list) {
        for (int i : input) {
            ArrayUtils.subCircularReverse(list, currentPosition, currentPosition + i);
            currentPosition = (currentPosition + i + skipSize) % list.length;
            skipSize++;
        }

        return Round.of(list, currentPosition, skipSize);
    }


    private static void task2(final String input) {
        List<Character> lengths = Stream.concat(input.chars().mapToObj(c -> (char) c), STANDARD_LENGTH_SUFFIX.stream()).toList();
//        System.out.println("lengths = " + lengths.stream().map(v -> String.valueOf((int) v)).collect(Collectors.joining(",")));

        Round round = Round.of(createList(), 0, 0);
        for (int i = 0; i < ROUNDS; i++) {
            round = round(lengths, round.currentPosition(), round.skipSize(), round.list());
//            System.out.println("round(" + i + ") = " + round);
        }

        final String hash = reduce(round.list());

        System.out.println("Part 2: " + hash);
    }

    public static String reduce(final char[] sparseHash) {
        StringBuilder denseHash = new StringBuilder();

        for (int i = 0; i < BLOCKS; i++) {
            char num = 0;
            for (int j = 0; j < sparseHash.length / BLOCKS; j++) {
                num = (char) (num ^ sparseHash[i * BLOCKS + j]);
            }
            denseHash.append(String.format("%02x", (int) num));
        }

        return denseHash.toString();
    }

    private record Round(char[] list, int currentPosition, int skipSize) {
        private static Round of(char[] list, int currentPosition, int skipSize) {
            return new Round(list, currentPosition, skipSize);
        }

        @Override
        public String toString() {
            return "Round{" + "list=" + ArrayUtils.stream(list).map(v -> String.valueOf((int) v)).collect(Collectors.joining(",")) + ", currentPosition=" + currentPosition + ", skipSize=" + skipSize + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Round round = (Round) o;
            return skipSize() == round.skipSize() && currentPosition() == round.currentPosition() && Objects.deepEquals(list(), round.list());
        }

        @Override
        public int hashCode() {
            return Objects.hash(Arrays.hashCode(list()), currentPosition(), skipSize());
        }
    }
}

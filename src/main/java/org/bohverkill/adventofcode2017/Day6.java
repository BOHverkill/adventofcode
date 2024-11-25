package org.bohverkill.adventofcode2017;

import org.bohverkill.utils.Utils;

import java.util.*;

public class Day6 {
    public static void main(String[] args) {
//        final String input = Utils.getInput("/2017/Day6_example");
        final String input = Utils.getInput("/2017/Day6_input");
        final int[] banks = Arrays.stream(input.trim().split("\t")).mapToInt(Integer::parseInt).toArray();
        task1(banks.clone());
        task2(banks.clone());
    }

    private static void task1(final int[] banks) {
        Set<SetArrayWrapper> set = new HashSet<>();
        int i;
        for (i = 0; set.add(new SetArrayWrapper(banks.clone())); i++) {
//            System.out.println("banks = " + Arrays.toString(banks));
            int k = maxIndex(banks);
            int blocks = banks[k];
            banks[k] = 0;
            k++;
            for (; blocks != 0; blocks--) {
                if (k >= banks.length) {
                    k = 0;
                }
                banks[k] = banks[k] + 1;
                k++;
            }
        }
        System.out.println("Part 1: " + i);
    }

    private static void task2(final int[] banks) {
        Set<SetArrayWrapper> set = new HashSet<>();
        List<SetArrayWrapper> list = new ArrayList<>();
        while (set.add(new SetArrayWrapper(banks.clone()))) {
            list.add(new SetArrayWrapper(banks.clone()));
            int k = maxIndex(banks);
            int blocks = banks[k];
            banks[k] = 0;
            k++;
            for (; blocks != 0; blocks--) {
                if (k >= banks.length) {
                    k = 0;
                }
                banks[k] = banks[k] + 1;
                k++;
            }
        }
        int res = 0;
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).equals(new SetArrayWrapper(banks.clone()))) {
                res = j;
            }
        }
        res = list.size() - res;
        System.out.println("Part 2: " + res);
    }

    private static int maxIndex(int[] banks) {
        int max = Integer.MIN_VALUE;
        int maxI = Integer.MIN_VALUE;
        for (int k = 0; k < banks.length; k++) {
            if (banks[k] > max) {
                max = banks[k];
                maxI = k;
            }
        }
        return maxI;
    }

    public record SetArrayWrapper(int[] entry) {
        @Override
        public String toString() {
            return Arrays.toString(entry);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            SetArrayWrapper that = (SetArrayWrapper) o;
            return Objects.deepEquals(entry(), that.entry());
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(entry());
        }
    }
}

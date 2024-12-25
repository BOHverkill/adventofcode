package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Day25 {
    public static void main(String[] args) {
//        final String path = "/2024/Day25_example";
        final String path = "/2024/Day25_input";
        final String input = Utils.getInput(path);
        final Pair<List<int[]>, List<int[]>> parse = parse(input);
        task1(parse.a(), parse.b());
    }

    private static Pair<List<int[]>, List<int[]>> parse(final String input) {
        final String[] split = input.split("\n\n");
        final List<int[]> locks = new ArrayList<>();
        final List<int[]> keys = new ArrayList<>();
        for (String s : split) {
            final String[] lines = s.split("\n");
            if (lines.length == 7 && lines[0].equals("#####")) {
                int[] lock = new int[5];
                for (int i = 0; i < 5; i++) {
                    int j;
                    for (j = 1; j <= 5; j++) {
                        if (lines[j].charAt(i) == '.') {
                            break;
                        } else if (lines[j].charAt(i) != '#') {
                            throw new IllegalArgumentException("Invalid input: " + s);
                        }
                    }
                    lock[i] = j - 1;
                }
                locks.add(lock);
            } else if (lines.length == 7 && lines[6].equals("#####")) {
                int[] key = new int[5];
                for (int i = 0; i < 5; i++) {
                    int j;
                    for (j = 5; j >= 1; j--) {
                        if (lines[j].charAt(i) == '.') {
                            break;
                        } else if (lines[j].charAt(i) != '#') {
                            throw new IllegalArgumentException("Invalid input: " + s);
                        }
                    }
                    key[i] = 5 - j;
                }
                keys.add(key);
            } else {
                throw new IllegalArgumentException("Invalid input: " + s);
            }
        }
        return Pair.of(locks, keys);
    }

    private static void task1(final List<int[]> locks, final List<int[]> keys) {
        int sum = 0;
        for (int[] lock : locks) {
            for (int[] key : keys) {
                boolean fit = true;
                for (int i = 0; i < 5; i++) {
                    if (lock[i] + key[i] > 5) {
                        fit = false;
                        break;
                    }
                }
                if (fit) {
                    sum++;
                }
            }
        }
        System.out.println("Part 1: " + sum);
    }
}

package org.bohverkill.adventofcode2017;

import org.bohverkill.utils.Utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Day9 {
    public static void main(String[] args) {
        final String input = Utils.getInput("/2017/Day9_input");
        task1(input);
        task2(input);
    }

    private static void task1(final String input) {
        final Group group = parseGroup(input);
        System.out.println("Part 1: " + calculateScore(group));
    }

    private static Group parseGroup(final String input) {
        Deque<Group> current = new ArrayDeque<>();
        current.addFirst(Group.of(0));
        int i = 0;
        while (i < input.length()) {
            final char c = input.charAt(i);
            switch (c) {
                case '{' -> {
                    final Group g = Group.of(current.getFirst().score() + 1);
                    current.getFirst().contained().add(g);
                    current.addFirst(g);
                }
                case '!' -> i++;
                case '<' -> {
                    i++;
                    while (true) {
                        final char d = input.charAt(i);
                        if (d == '!') {
                            i++;
                        } else if (d == '>') {
                            break;
                        }
                        i++;
                    }
                }
                case '}' -> current.removeFirst();
            }
            i++;
        }
        return current.getFirst();
    }

    private static int calculateScore(final Group group) {
        Deque<Group> current = new ArrayDeque<>();
        current.add(group);
        int score = 0;
        while (!current.isEmpty()) {
            final Group g = current.removeFirst();
            current.addAll(g.contained());
            score += g.score();
        }
        return score;
    }

    private static void task2(final String input) {
        System.out.println("Part 2: " + countGarbage(input));
    }

    private static int countGarbage(final String input) {
        int count = 0;
        int i = 0;
        while (i < input.length()) {
            final char c = input.charAt(i);
            if (c == '!') {
                i++;
            } else if (c == '<') {
                i++;
                while (true) {
                    final char d = input.charAt(i);
                    if (d == '!') {
                        count--;
                        i++;
                    } else if (d == '>') {
                        break;
                    }
                    count++;
                    i++;
                }
            }
            i++;
        }
        return count;
    }

    private record Group(int score, List<Group> contained) {
        private static Group of(int score) {
            return new Group(score, new ArrayList<>());
        }
    }
}

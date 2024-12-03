package org.bohverkill.adventofcode2017;

import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 {
    private static final Pattern PIPE_PATTERN = Pattern.compile("^(\\d+) <-> ([0-9, ]+)");

    public static void main(String[] args) {
//        final List<Pipe> input = Utils.getInputLines("/2017/Day12_example").map(Day12::parse).toList();
        final List<Pipe> input = Utils.getInputLines("/2017/Day12_input").map(Day12::parse).toList();
        task1(input);
        task2(input);
    }

    private static void task1(List<Pipe> input) {
        Set<Integer> reaching = getReaching(input, 0);

        System.out.println("Part 1: " + reaching.size());
    }

    private static Set<Integer> getReaching(List<Pipe> input, int start) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            int i = queue.poll();
            final List<Integer> to = new ArrayList<>(input.get(i).to());
            to.removeAll(visited);
            queue.addAll(to);
            visited.add(i);
        }

        return visited;
    }

    private static void task2(List<Pipe> input) {
        Set<Set<Integer>> uniqReaching = new HashSet<>();
        for (int i = 0; i < input.size(); i++) {
            uniqReaching.add(getReaching(input, i));
        }

        System.out.println("Part 2: " + uniqReaching.size());
    }

    private static Pipe parse(final String input) {
        final Matcher pipeMatcher = Utils.getMatcher(PIPE_PATTERN, input);
        final int from = Integer.parseInt(pipeMatcher.group(1));
        final List<Integer> to = Arrays.stream(pipeMatcher.group(2).split(", ")).map(Integer::parseInt).toList();
        return Pipe.of(from, to);
    }

    private record Pipe(int from, List<Integer> to) {
        private static Pipe of(int from, List<Integer> to) {
            return new Pipe(from, to);
        }
    }
}

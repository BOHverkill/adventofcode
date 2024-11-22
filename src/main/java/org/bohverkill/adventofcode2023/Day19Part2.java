package org.bohverkill.adventofcode2023;


import org.bohverkill.models.Pair;
import org.bohverkill.utils.Utils;

import java.util.*;

import static org.bohverkill.adventofcode2023.Day19Part1.*;

public class Day19Part2 {
    private static final int RATING_MIN = 1;
    private static final int RATING_MAX = 4000;

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day19_example");
        String input = Utils.getInput("/2023/Day19_input");

        final String[] split = input.split("\n\n");
        final List<Workflow> workflows = Arrays.stream(split[0].split("\n")).map(Day19Part1::parseWorkflow).toList();
        final Workflow in = workflows.stream().filter(workflow -> workflow.name().equals("in")).findFirst().orElseThrow();

        long count = solve(workflows, in);

        System.out.println("Day 19 Part 2: " + count);
    }

    public static long solve(List<Workflow> workflows, Workflow in) {
        long count = 0;

        Queue<Pair<Workflow, RangeRating>> queue = new ArrayDeque<>();
        queue.add(Pair.of(in, RangeRating.start()));

        while (!queue.isEmpty()) {
            final Pair<Workflow, RangeRating> current = queue.remove();
            final Workflow currentWorkflow = current.a();
            final RangeRating currentRangeRating = current.b();

            final List<Pair<String, RangeRating>> nextPairs = processWorkflow(currentRangeRating, currentWorkflow);

            for (Pair<String, RangeRating> nextPair : nextPairs) {
                final String nextWorkflow = nextPair.a();
                final RangeRating nextRangeRating = nextPair.b();
                if (nextWorkflow.equals("A")) {
                    count += nextRangeRating.sum();
                } else if (!nextWorkflow.equals("R")) {
                    queue.add(Pair.of(workflows.stream().filter(w -> w.name().equals(nextWorkflow)).findFirst().orElseThrow(), nextRangeRating));
                }
            }
        }
        return count;
    }

    public static List<Pair<String, RangeRating>> processWorkflow(RangeRating rangeRating, Workflow workflow) {
        RangeRating currentRangeRating = rangeRating;
        final ArrayList<Pair<String, RangeRating>> pairs = new ArrayList<>();
        for (Rule rule : workflow.rules()) {
            final RangeRating newRange;
            if (rule.isEndRule()) {
                newRange = currentRangeRating.copy();
            } else {
                final Category category = rule.category();
                final Operator operator = rule.operator();
                final long rating = rule.rating();
                final long min = currentRangeRating.categoryMin(category);
                final long max = currentRangeRating.categoryMax(category);

                long newMin = min;
                long newMax = max;
                long newOtherMin = min;
                long newOtherMax = max;
                if (operator == Operator.GREATER_THAN) {
                    newMin = Math.max(min, rating + 1);
                    newOtherMax = Math.min(max, rating);
                } else if (operator == Operator.LESS_THAN) {
                    newMax = Math.min(max, rating - 1);
                    newOtherMin = Math.max(min, rating);
                }

                newRange = currentRangeRating.copyOf(category, newMin, newMax);
                currentRangeRating = currentRangeRating.copyOf(category, newOtherMin, newOtherMax);
            }
            pairs.add(Pair.of(rule.outWorkflow(), newRange));
        }
        return pairs;
    }

    public record RangeRating(long minX, long maxX, long minM, long maxM, long minA, long maxA, long minS, long maxS) {

        public RangeRating(RangeRating rating) {
            this(rating.minX(), rating.maxX(), rating.minM(), rating.maxM(), rating.minA(), rating.maxA(), rating.minS(), rating.maxS());
        }

        public static RangeRating start() {
            return new RangeRating(RATING_MIN, RATING_MAX, RATING_MIN, RATING_MAX, RATING_MIN, RATING_MAX, RATING_MIN, RATING_MAX);
        }

        public long categoryMin(Category category) {
            return switch (category) {
                case X -> minX();
                case M -> minM();
                case A -> minA();
                case S -> minS();
            };
        }

        public long categoryMax(Category category) {
            return switch (category) {
                case X -> maxX();
                case M -> maxM();
                case A -> maxA();
                case S -> maxS();
            };
        }

        public long sum() {
            return (maxX() + 1 - minX()) * (maxM() + 1 - minM()) * (maxA() + 1 - minA()) * (maxS() + 1 - minS());
        }

        public RangeRating copy() {
            return new RangeRating(this);
        }

        public RangeRating copyOf(Category category, long min, long max) {
            return switch (category) {
                case X -> new RangeRating(min, max, minM(), maxM(), minA(), maxA(), minS(), maxS());
                case M -> new RangeRating(minX(), maxX(), min, max, minA(), maxA(), minS(), maxS());
                case A -> new RangeRating(minX(), maxX(), minM(), maxM(), min, max, minS(), maxS());
                case S -> new RangeRating(minX(), maxX(), minM(), maxM(), minA(), maxA(), min, max);
            };
        }
    }
}

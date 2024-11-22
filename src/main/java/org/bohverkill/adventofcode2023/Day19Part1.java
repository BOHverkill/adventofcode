package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19Part1 {
    private static final Pattern WORKFLOW_PATTERN = Pattern.compile("^([a-z]+)\\{(.*)}$");
    private static final Pattern RULE_PATTERN = Pattern.compile("^([xmas])([><])(\\d+):([a-zAR]+)$");
    private static final Pattern RATING_PATTERN = Pattern.compile("^\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}$");

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day19_example");
        String input = Utils.getInput("/2023/Day19_input");

        final String[] split = input.split("\n\n");
        final List<Workflow> workflows = Arrays.stream(split[0].split("\n")).map(Day19Part1::parseWorkflow).toList();
        final List<Rating> ratings = Arrays.stream(split[1].split("\n")).map(Day19Part1::parseRating).toList();
        final Workflow in = workflows.stream().filter(workflow -> workflow.name().equals("in")).findFirst().orElseThrow();

        final int sum = ratings.stream().filter(rating -> followWorkflows(rating, workflows, in)).mapToInt(Rating::sum).sum();

        System.out.println("Day 19 Part 1: " + sum);
    }

    /**
     * @return true if it is accepted, else false
     */
    //
    public static boolean followWorkflows(Rating rating, List<Workflow> workflows, Workflow in) {
        Workflow current = in;

        while (true) {
            final String nextWorkflow = processWorkflow(rating, current);
            if (nextWorkflow.equals("A")) {
                return true;
            }
            if (nextWorkflow.equals("R")) {
                return false;
            }
            current = workflows.stream().filter(w -> w.name().equals(nextWorkflow)).findFirst().orElseThrow(); // could be improved by switch to map
        }
    }

    public static String processWorkflow(Rating rating, Workflow workflow) {
        for (Rule rule : workflow.rules()) {
            if (rule.isEndRule() || rule.matches(rating)) {
                return rule.outWorkflow();
            }
        }
        throw new IllegalStateException("No matching workflow");
    }

    public static Rating parseRating(String input) {
        final Matcher ratingMatcher = Utils.getMatcher(RATING_PATTERN, input);
        final int x = Integer.parseInt(ratingMatcher.group(1));
        final int m = Integer.parseInt(ratingMatcher.group(2));
        final int a = Integer.parseInt(ratingMatcher.group(3));
        final int s = Integer.parseInt(ratingMatcher.group(4));

        return new Rating(x, m, a, s);
    }

    public static Workflow parseWorkflow(String input) {
        final Matcher workflowMatcher = Utils.getMatcher(WORKFLOW_PATTERN, input);
        final String name = workflowMatcher.group(1);
        final String[] stepsSplit = workflowMatcher.group(2).split(",");
        List<Rule> rules = Arrays.stream(stepsSplit).map(Day19Part1::parseRule).toList();

        return new Workflow(name, rules);
    }

    public static Rule parseRule(String input) {
        final Matcher matcher = RULE_PATTERN.matcher(input);
        if (!matcher.find()) {
            return new Rule(input);
        }

        Category category = Category.parse(matcher.group(1));
        Operator operator = Operator.parse(matcher.group(2));
        int rating = Integer.parseInt(matcher.group(3));
        String workflow = matcher.group(4);

        return new Rule(category, operator, rating, workflow);
    }

    public enum Category {
        X, M, A, S;

        public static Category parse(String category) {
            return switch (category) {
                case "x" -> X;
                case "m" -> M;
                case "a" -> A;
                case "s" -> S;
                default -> throw new IllegalStateException("Unexpected value: " + category);
            };
        }
    }

    public enum Operator {
        GREATER_THAN, LESS_THAN;

        public static Operator parse(String operator) {
            return switch (operator) {
                case ">" -> GREATER_THAN;
                case "<" -> LESS_THAN;
                default -> throw new IllegalStateException("Unexpected value: " + operator);
            };
        }
    }

    public record Workflow(String name, List<Rule> rules) {
    }

    public record Rule(Category category, Operator operator, int rating, String outWorkflow) {

        public Rule(String outWorkflow) {
            this(null, null, -1, outWorkflow);
        }

        public boolean isEndRule() {
            return category() == null || operator() == null || rating() == -1;
        }

        public boolean matches(Rating rating) {
            final int r = rating.category(category());
            return switch (operator()) {
                case GREATER_THAN -> r > rating();
                case LESS_THAN -> r < rating();
            };
        }
    }

    public record Rating(int x, int m, int a, int s) {
        public int category(Category category) {
            return switch (category) {
                case X -> x();
                case M -> m();
                case A -> a();
                case S -> s();
            };
        }

        public int sum() {
            return x() + m() + a() + s();
        }
    }
}

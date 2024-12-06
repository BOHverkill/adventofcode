package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5 {
    private static final Pattern PAGE_ORDERING_RULES_PATTERN = Pattern.compile("^(\\d+)\\|(\\d+)$");

    public static void main(String[] args) {
//        final String path = "/2024/Day5_example";
        final String path = "/2024/Day5_input";
        final String[] input = Utils.getInput(path).split("\n\n");

        final List<Pair<Integer, Integer>> pageOrderingRules = Arrays.stream(input[0].split("\n")).map(Day5::parsePageOrderingRule).toList();
        final Map<Integer, List<Integer>> beforePageOrderingRules = pageOrderingRules.stream().collect(Collectors.groupingBy(Pair::a, Collectors.mapping(Pair::b, Collectors.toList())));
        final Map<Integer, List<Integer>> afterPageOrderingRules = pageOrderingRules.stream().collect(Collectors.groupingBy(Pair::b, Collectors.mapping(Pair::a, Collectors.toList())));
        final List<List<Integer>> pagesToProduce = Arrays.stream(input[1].split("\n")).map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).toList()).toList();

        final List<List<Integer>> toPrint = new ArrayList<>();
        final List<List<Integer>> notToPrint = new ArrayList<>();

        for (List<Integer> pages : pagesToProduce) {
            if (isCorrectOrdering(pages, beforePageOrderingRules, afterPageOrderingRules)) {
                toPrint.add(pages);
            } else {
                notToPrint.add(pages);
            }
        }

        task1(toPrint);
        task2(notToPrint, beforePageOrderingRules, afterPageOrderingRules);
    }

    private static boolean isCorrectOrdering(List<Integer> pages, final Map<Integer, List<Integer>> beforePageOrderingRules, final Map<Integer, List<Integer>> afterPageOrderingRules) {
        for (int i = 0; i < pages.size(); i++) {
            final Integer page = pages.get(i);
            if (i != pages.size() - 1) {
                final List<Integer> afterRules = afterPageOrderingRules.get(page);
                final List<Integer> subList = pages.subList(i + 1, pages.size());
                if (afterRules != null) {
                    for (Integer after : afterRules) {
                        if (subList.contains(after)) {
                            return false;
                        }
                    }
                }
            }
            if (i != 0) {
                final List<Integer> beforeRules = beforePageOrderingRules.get(page);
                final List<Integer> subList = pages.subList(0, i - 1);
                if (beforeRules != null) {
                    for (Integer before : beforeRules) {
                        if (subList.contains(before)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static List<Integer> fixPrint(List<Integer> pages, final Map<Integer, List<Integer>> beforePageOrderingRules, final Map<Integer, List<Integer>> afterPageOrderingRules) {
        List<Integer> fixedPages = new LinkedList<>(pages);
        for (int i = 0; i < fixedPages.size(); i++) {
            final Integer page = fixedPages.get(i);
            if (i != fixedPages.size() - 1) {
                final List<Integer> afterRules = afterPageOrderingRules.get(page);
                final List<Integer> subList = fixedPages.subList(i + 1, fixedPages.size());
                if (afterRules != null) {
                    for (Integer after : afterRules) {
                        if (subList.contains(after)) {
                            fixedPages.remove(after);
                            fixedPages.add(i, after);
                            return fixPrint(fixedPages, beforePageOrderingRules, afterPageOrderingRules);
                        }
                    }
                }
            }
            if (i != 0) {
                final List<Integer> beforeRules = beforePageOrderingRules.get(page);
                final List<Integer> subList = fixedPages.subList(0, i - 1);
                if (beforeRules != null) {
                    for (Integer before : beforeRules) {
                        if (subList.contains(before)) {
                            fixedPages.remove(before);
                            fixedPages.add(i, before);
                            return fixPrint(fixedPages, beforePageOrderingRules, afterPageOrderingRules);
                        }
                    }
                }
            }
        }
        return fixedPages;
    }

    private static void task1(final List<List<Integer>> toPrint) {
        int sum = toPrint.stream().mapToInt(i -> i.get(i.size() / 2)).sum();

        System.out.println("Part 1: " + sum);
    }

    private static void task2(final List<List<Integer>> notToPrint, final Map<Integer, List<Integer>> beforePageOrderingRules, final Map<Integer, List<Integer>> afterPageOrderingRules) {
        int sum = notToPrint.stream().map(pages -> fixPrint(pages, beforePageOrderingRules, afterPageOrderingRules)).mapToInt(i -> i.get(i.size() / 2)).sum();

        System.out.println("Part 2: " + sum);
    }

    private static Pair<Integer, Integer> parsePageOrderingRule(final String line) {
        final Matcher pageOrderingRuleMatcher = Utils.getMatcher(PAGE_ORDERING_RULES_PATTERN, line);
        int i = Integer.parseInt(pageOrderingRuleMatcher.group(1));
        int j = Integer.parseInt(pageOrderingRuleMatcher.group(2));
        return new Pair<>(i, j);
    }
}

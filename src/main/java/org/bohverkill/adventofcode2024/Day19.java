package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {
    public static void main(String[] args) {
//        final String path = "/2024/Day19_example";
        final String path = "/2024/Day19_input";
        final Pair<List<List<Stripe>>, List<List<Stripe>>> input = parse(Utils.getInput(path));
        task1(input);
        task2(input);
    }

    private static Pair<List<List<Stripe>>, List<List<Stripe>>> parse(final String input) {
        final String[] split = input.split("\n\n");
        final List<List<Stripe>> availableTowelPatterns = Arrays.stream(split[0].split(", ")).map(Day19::parsePattern).toList();
        final List<List<Stripe>> designs = Arrays.stream(split[1].split("\n")).map(Day19::parsePattern).toList();
        return Pair.of(availableTowelPatterns, designs);
    }

    private static void task1(final Pair<List<List<Stripe>>, List<List<Stripe>>> input) {
        int res = 0;

        for (List<Stripe> design : input.b()) {
            if (applyStripe(design, input.a(), new HashMap<>())) {
                res++;
            }
        }

        System.out.println("Part 1: " + res);
    }

    private static boolean applyStripe(List<Stripe> design, List<List<Stripe>> stripes, Map<Pair<List<Stripe>, List<Stripe>>, Boolean> cache) {
        if (design.isEmpty()) {
            return true;
        }
        for (List<Stripe> stripe : stripes) {
            if (!cache.containsKey(Pair.of(stripe, design))) {
                if (startsWith(design, stripe)) {
                    final boolean res = applyStripe(design.subList(stripe.size(), design.size()), stripes, cache);
                    cache.put(Pair.of(stripe, design), res);
                    if (res) {
                        return true;
                    }
                }
            } else {
                if (Boolean.TRUE.equals(cache.get(Pair.of(stripe, design)))) {
                    return true;
                }
            }
        }
        return false;
    }

//    private static boolean applyStripe(List<Stripe> design, List<List<Stripe>> stripes) {
//        Deque<Pair<List<Stripe>, List<Stripe>>> queue = new LinkedList<>();
//        for (List<Stripe> stripe : stripes) {
//            queue.add(Pair.of(design, stripe));
//        }
//        while (!queue.isEmpty()) {
//            Pair<List<Stripe>, List<Stripe>> current = queue.pollLast();
//            if (current.a().isEmpty()) {
//                return true;
//            }
//            if (startsWith(current.a(), current.b())) {
//                for (List<Stripe> stripe : stripes) {
//                    queue.add(Pair.of(current.a().subList(current.b().size(), current.a().size()), stripe));
//                }
//            }
//        }
//        return false;
//    }

    private static <T> boolean startsWith(List<T> list, List<T> begin) {
        if (begin.size() > list.size()) {
            return false;
//            throw new IllegalArgumentException("Begin must be less or equal to list size");
        }
        for (int i = 0; i < begin.size(); i++) {
            if (!list.get(i).equals(begin.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static void task2(final Pair<List<List<Stripe>>, List<List<Stripe>>> input) {
        long res = 0;

        for (List<Stripe> design : input.b()) {
            final HashMap<Pair<List<Stripe>, List<Stripe>>, Long> cache = new HashMap<>();
            res += applyStripeCount(design, input.a(), cache);
        }

        System.out.println("Part 2: " + res);
        // 923272966 too low
    }

    private static long applyStripeCount(List<Stripe> design, List<List<Stripe>> stripes, Map<Pair<List<Stripe>, List<Stripe>>, Long> cache) {
        if (design.isEmpty()) {
            return 1;
        }
        long count = 0;
        for (List<Stripe> stripe : stripes) {
            if (!cache.containsKey(Pair.of(stripe, design))) {
                if (startsWith(design, stripe)) {
                    final long res = applyStripeCount(design.subList(stripe.size(), design.size()), stripes, cache);
                    cache.put(Pair.of(stripe, design), res);
                    count += res;
                }
            } else {
                count += cache.get(Pair.of(stripe, design));
            }
        }
        return count;
    }

    private static List<Stripe> parsePattern(final String input) {
        return StringUtils.parseCharToStringStream(input).map(Stripe::fromString).toList();
    }

    private enum Stripe {
        WHITE, BLUE, BLACK, RED, GREEN;

        private static Stripe fromString(final String stripe) {
            return switch (stripe) {
                case "w" -> WHITE;
                case "u" -> BLUE;
                case "b" -> BLACK;
                case "r" -> RED;
                case "g" -> GREEN;
                default -> throw new IllegalStateException("Unexpected stripe: " + stripe);
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case WHITE -> "w";
                case BLUE -> "u";
                case BLACK -> "b";
                case RED -> "r";
                case GREEN -> "g";
            };
        }
    }
}

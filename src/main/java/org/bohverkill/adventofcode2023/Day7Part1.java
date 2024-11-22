package org.bohverkill.adventofcode2023;

import org.bohverkill.utils.Utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7Part1 {
    private static final String CARD_ORDER = "AKQJT98765432";
    private static final Pattern CAMEL_CARD_PATTERN = Pattern.compile("^([AKQJT2-9]+) (\\d+)$");

    public static void main(String[] args) {
//        Stream<String> lines = Utils.getInputLines("/2023/Day7_example");
        Stream<String> lines = Utils.getInputLines("/2023/Day7_input");
        final List<CamelCard> list = lines.map(Day7Part1::parseCamelCard).sorted().toList();
        System.out.println("Part 1: " + IntStream.range(0, list.size()).map(i -> list.get(i).bid * (i + 1)).sum());
    }

    public static CamelCard parseCamelCard(String line) {
        final Matcher camelCardMatcher = Utils.getMatcher(CAMEL_CARD_PATTERN, line);
        String hand = camelCardMatcher.group(1);
        if (hand.length() != 5) {
            throw new IllegalStateException("Malformed hand: " + hand);
        }
        int bid = Integer.parseInt(camelCardMatcher.group(2));
        return new CamelCard(hand, bid);
    }

    public record CamelCard(String hand, int bid) implements Comparable<CamelCard> {
        public static int getType(String hand) {
            final Map<Character, Integer> occurrences = getOccurrences(hand);
            final List<Integer> values = occurrences.values().stream().sorted(Comparator.reverseOrder()).toList();

            if (values.equals(List.of(5))) {
                // Five of a kind
                return 6;
            } else if (values.equals(List.of(4, 1))) {
                // Four of a kind
                return 5;
            } else if (values.equals(List.of(3, 2))) {
                // Full house
                return 4;
            } else if (values.equals(List.of(3, 1, 1))) {
                // Three of a kind
                return 3;
            } else if (values.equals(List.of(2, 2, 1))) {
                // Two pair
                return 2;
            } else if (values.equals(List.of(2, 1, 1, 1))) {
                // One pair
                return 1;
            } else if (values.equals(List.of(1, 1, 1, 1, 1))) {
                // One pair
                return 0;
            } else {
                throw new IllegalStateException("Impossible");
            }
        }

        public static int compareHands(String thisHand, String oHand) {
            for (int i = 0; i < 5; i++) {
                final char thisChar = thisHand.charAt(i);
                final char oChar = oHand.charAt(i);
                if (thisChar != oChar) {
                    int thisCharIndex = CARD_ORDER.indexOf(thisChar);
                    int oCharIndex = CARD_ORDER.indexOf(oChar);
                    return thisCharIndex < oCharIndex ? 1 : -1;
                }
            }
            return 0;
        }

        public static Map<Character, Integer> getOccurrences(String hand) {
//            IntStream.range(0, hand.length()).forEach(i -> out.compute(hand.charAt(i), (key, value) -> value != null ? value + 1 : 1));
            return hand.chars().mapToObj(i -> (char) i).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
        }

        @Override
        public int compareTo(CamelCard o) {
            int typeThis = getType(this.hand);
            int typeO = getType(o.hand);
            if (typeThis < typeO) {
                return -1;
            } else if (typeThis > typeO) {
                return 1;
            } else {
                return compareHands(this.hand, o.hand);
            }
        }
    }
}

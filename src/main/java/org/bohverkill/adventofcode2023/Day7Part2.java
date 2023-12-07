package org.bohverkill.adventofcode2023;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7Part2 {
    private static final String CARD_ORDER = "AKQT98765432J";
    private static final Pattern CAMEL_CARD_PATTERN = Pattern.compile("^([AKQT2-9J]+) (\\d+)$");

    public static void main(String[] args) {
//        Stream<String> lines = Utils.getInputLines("/2023/Day7_example");
        Stream<String> lines = Utils.getInputLines("/2023/Day7_input");
        final List<CamelCard> list = lines.map(Day7Part2::parseCamelCard).sorted().toList();
        System.out.println("Part 2: " + IntStream.range(0, list.size()).map(i -> list.get(i).bid * (i + 1)).sum());
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

        public static int getBestTypeWithJoker(String hand) {
            if (!hand.contains("J")) {
                return getType(hand);
            } else {
                int bestType = Integer.MIN_VALUE;
                for (int i = 0; i < CARD_ORDER.length(); i++) {
                    final char card = CARD_ORDER.charAt(i);
                    final int type = getType(hand.replace('J', card));
                    if (type > bestType) {
                        bestType = type;
                    }
                }
                return bestType;
            }
        }

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
            return hand.chars().mapToObj(i -> (char) i).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
        }

        @Override
        public int compareTo(CamelCard o) {
            int typeThis = getBestTypeWithJoker(this.hand);
            int typeO = getBestTypeWithJoker(o.hand);
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

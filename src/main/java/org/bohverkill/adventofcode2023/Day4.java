package org.bohverkill.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day4 {
    private static final Pattern CARD_PATTERN = Pattern.compile("^Card +(\\d+): ?(.*)$");

    public static void main(String[] args) {
//        try (Stream<String> stream = Files.lines(Paths.get(Objects.requireNonNull(Day4.class.getResource("/2023/Day4_example")).getPath()))) {
        try (Stream<String> stream = Files.lines(Paths.get(Objects.requireNonNull(Day4.class.getResource("/2023/Day4_input")).getPath()))) {
//            System.out.println(stream.map(Day4::convertCart).mapToInt(Day4::calculatePoints).sum());
            final List<Card> cards = stream.map(Day4::convertCart).toList();
            System.out.println(calculateScratchcards(cards).values().stream().mapToInt(value -> value).sum());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Card convertCart(String line) {
        final Matcher gameMatcher = CARD_PATTERN.matcher(line);
        if (!gameMatcher.find()) {
            throw new IllegalStateException("Malformed input: " + line);
        }
        final int cardId = Integer.parseInt(gameMatcher.group(1));
        final String[] split = gameMatcher.group(2).split("\\|");
        if (split.length != 2) {
            throw new IllegalStateException("Malformed input: " + line);
        }
        final List<Integer> winningNumbers = getNumbers(split[0]);
        final List<Integer> numbersYouHave = getNumbers(split[1]);

        return new Card(cardId, winningNumbers, numbersYouHave);
    }

    private static List<Integer> getNumbers(String numbers) {
        return Arrays.stream(numbers.split(" ")).filter(s -> !s.isEmpty()).map(Integer::parseInt).toList();
    }

    private static int calculatePoints(Card card) {
        final long count = card.numbersYouHave.stream().filter(card.winningNumbers::contains).count();
        return (int) Math.pow(2.0, count - 1.0);
    }

    private static Map<Integer, Integer> calculateScratchcards(List<Card> card) {
        // init map with 1 card for each id
        final Map<Integer, Integer> scratchcards = IntStream.rangeClosed(1, card.size()).boxed().collect(Collectors.toMap(i -> i, i -> 1, (a, b) -> b));
        for (Card currentCard : card) {
            for (Integer wonCardId : calculateScratchcard(currentCard)) {
                scratchcards.computeIfPresent(wonCardId, (key, oldValue) -> oldValue + scratchcards.get(currentCard.id));
            }
        }
        return scratchcards;
    }

    private static List<Integer> calculateScratchcard(Card card) {
        final int count = Math.toIntExact(card.numbersYouHave.stream().filter(card.winningNumbers::contains).count());
        return IntStream.rangeClosed(1, count).mapToObj(i -> card.id + i).toList();
    }

    public record Card(int id, List<Integer> winningNumbers, List<Integer> numbersYouHave) {
    }
}

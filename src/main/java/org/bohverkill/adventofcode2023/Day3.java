package org.bohverkill.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day3 {
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[^.\\d\\n]");
    private static final Pattern GEAR_PATTERN = Pattern.compile("(\\*)");

    public static void main(String[] args) {
        try {
//            String inputString = Files.readString(Paths.get(Objects.requireNonNull(Day3.class.getResource("/2023/Day3_example")).getPath()));
            String inputString = Files.readString(Paths.get(Objects.requireNonNull(Day3.class.getResource("/2023/Day3_input")).getPath()));
            final String[] input = inputString.split("\n");
            final int lineLength = input[0].length();
            // lineLength + 1 because of the newlines
            final int resultPart1 = find(SPECIAL_CHARS_PATTERN, inputString).stream().map(i -> mapToSpecialChar(i, lineLength + 1)).map(s -> getWindow(input, lineLength, s)).flatMap(Collection::stream).flatMapToInt(p -> extractPartNumber(input, lineLength, p)).sum();
            System.out.println("Part 1: " + resultPart1);
            final int resultPart2 = find(GEAR_PATTERN, inputString).stream().map(i -> mapToSpecialChar(i, lineLength + 1)).map(s -> getWindow(input, lineLength, s)).mapToInt(p -> extractGearRatio(input, lineLength, p)).sum();
            System.out.println("Part 2: " + resultPart2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Integer> find(final Pattern pattern, String input) {
        final Matcher gameMatcher = pattern.matcher(input);
        List<Integer> specialChars = new ArrayList<>();
        while (gameMatcher.find()) {
            specialChars.add(gameMatcher.start());
        }
        return specialChars;
    }

    public static SpecialChar mapToSpecialChar(int position, int lineLength) {
        return new SpecialChar(position / lineLength, position % lineLength);
    }

    public static List<Position> getWindow(String[] input, int lineLength, SpecialChar specialChar) {
        List<Position> window = new ArrayList<>();
        int columnStart = specialChar.column - 1;
        if (columnStart < 0) {
            columnStart = 0;
        }
        int columnEnd = specialChar.column + 1;
        if (columnEnd >= lineLength) {
            columnEnd = lineLength - 1;
        }
        if (specialChar.row > 0) {
            window.add(new Position(specialChar.row - 1, columnStart, columnEnd));
        }
        window.add(new Position(specialChar.row, columnStart, columnEnd));
        if (specialChar.row < input.length - 1) {
            window.add(new Position(specialChar.row + 1, columnStart, columnEnd));
        }
        return window;
    }

    public static IntStream extractPartNumber(String[] input, int lineLength, Position position) {
        List<Integer> numbers = new ArrayList<>();
        extract(input, lineLength, position, numbers);
        return numbers.stream().mapToInt(value -> value);
    }

    public static int extractGearRatio(String[] input, int lineLength, List<Position> positions) {
        List<Integer> numbers = new ArrayList<>();
        for (Position position : positions) {
            extract(input, lineLength, position, numbers);
        }
        if (numbers.size() == 2) {
            return numbers.get(0) * numbers.get(1);
        } else {
            return 0;
        }
    }

    private static void extract(String[] input, int lineLength, Position position, List<Integer> numbers) {
        int column = position.columnStart;
        while (column <= position.columnEnd) {
            if (Character.isDigit(input[position.row].charAt(column))) {
                final Number number = findNumber(input, lineLength, position.row, column);
                if (number.columnEnd > column) {
                    // skip already processed numbers that are after the current one
                    column = number.columnEnd;
                }
                numbers.add(number.number);
            }
            column++;
        }
    }

    public static Number findNumber(String[] input, int lineLength, int row, int originalColumn) {
        StringBuilder out = new StringBuilder();
        int columnStart = originalColumn;
        int columnEnd = originalColumn;
        for (int column = originalColumn; column >= 0 && Character.isDigit(input[row].charAt(column)); column--) {
            out.insert(0, input[row].charAt(column));
            columnStart--;
        }
        // don't count numbers double, use the next one as start
        for (int column = originalColumn + 1; column < lineLength && Character.isDigit(input[row].charAt(column)); column++) {
            out.append(input[row].charAt(column));
            columnEnd++;
        }
        return new Number(Integer.parseInt(out.toString()), columnStart, columnEnd);
    }

    public record SpecialChar(int row, int column) {
    }

    public record Position(int row, int columnStart, int columnEnd) {
    }

    public record Number(int number, int columnStart, int columnEnd) {
    }
}

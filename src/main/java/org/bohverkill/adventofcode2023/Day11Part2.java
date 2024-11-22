package org.bohverkill.adventofcode2023;

import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day11Part2 {
    private static final Pattern EMPTY_LINE = Pattern.compile("^\\.+$");
    private static final Pattern GALEXY_PATTERN = Pattern.compile("#");

    private static final int EXPAND_FACTOR = 1000000;
//    private static final int EXPAND_FACTOR = 100;
//    private static final int EXPAND_FACTOR = 10;
//    private static final int EXPAND_FACTOR = 2;

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day11_example");
        String input = Utils.getInput("/2023/Day11_input");
        final List<String> lines = new ArrayList<>(Arrays.stream(input.split("\n")).toList());
        List<Integer> expandedRows = new ArrayList<>();
        List<Integer> expandedColumns = new ArrayList<>();
        expand(lines, expandedRows, expandedColumns);
        final int lineLength = lines.getFirst().length();
        final List<Galaxy> positions = Utils.findPositions(GALEXY_PATTERN, String.join("", lines)).stream().map(i -> mapToGalaxy(i, lineLength)).toList();
        long out = 0;
        for (int i = 0; i < positions.size(); i++) {
            final Galaxy current = positions.get(i);
            out += IntStream.range(i + 1, positions.size()).mapToObj(positions::get).mapToLong(galaxy -> manhattanDistance(expand(current, expandedRows, expandedColumns), expand(galaxy, expandedRows, expandedColumns))).sum();
        }
        System.out.println("Day 11 Part 2: " + out);
    }

    private static Galaxy expand(Galaxy galaxy, List<Integer> expandedRows, List<Integer> expandedColumns) {
        int rows = (int) IntStream.range(0, galaxy.row).filter(expandedRows::contains).count();
        int columns = (int) IntStream.range(0, galaxy.column).filter(expandedColumns::contains).count();
        return new Galaxy(galaxy.row + rows * (EXPAND_FACTOR - 1), galaxy.column + columns * (EXPAND_FACTOR - 1));
    }

    public static void expand(List<String> input, List<Integer> expandedRows, List<Integer> expandedColumns) {
        int i = 0;
        while (i < input.size()) {
            final String line = input.get(i);
            if (EMPTY_LINE.matcher(line).matches()) {
                expandedRows.add(i);
            }
            i++;
        }
        i = 0;
        while (i < input.getFirst().length()) {
            boolean empty = true;
            for (String s : input) {
                if (s.charAt(i) == '#') {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                expandedColumns.add(i);
            }
            i++;
        }
    }

    public static Galaxy mapToGalaxy(int position, int lineLength) {
        return new Galaxy(position / lineLength, position % lineLength);
    }

    public static int manhattanDistance(Galaxy g0, Galaxy g1) {
        return Math.abs(g1.row() - g0.row()) + Math.abs(g1.column() - g0.column());
    }

    public record Galaxy(int row, int column) {
    }
}

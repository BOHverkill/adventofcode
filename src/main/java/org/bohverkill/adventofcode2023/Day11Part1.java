package org.bohverkill.adventofcode2023;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day11Part1 {
        private static final Pattern EMPTY_LINE = Pattern.compile("^\\.+$");
    private static final Pattern GALEXY_PATTERN = Pattern.compile("#");

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day11_example");
        String input = Utils.getInput("/2023/Day11_input");
        final List<String> lines = new ArrayList<>(Arrays.stream(input.split("\n")).toList());
        expand(lines);
        final int lineLength = lines.getFirst().length();
//        System.out.println(String.join("\n", lines));
        final List<Galaxy> positions = Utils.findPositions(GALEXY_PATTERN, String.join("", lines)).stream().map(i -> mapToGalaxy(i, lineLength)).toList();
//        System.out.println(positions);
        int out = 0;
        for (int i = 0; i < positions.size(); i++) {
            final int[][] grid = floodFill(positions.get(i), lines.size(), lineLength);
            out += IntStream.range(i + 1, positions.size()).mapToObj(positions::get).mapToInt(galaxy -> grid[galaxy.row()][galaxy.column()] - 1).sum();
        }
        System.out.println("Day 11 Part 1: " + out);
    }

    public static void expand(List<String> input) {
        int i = 0;
        while (i < input.getFirst().length()) {
            boolean empty = true;
            for (String s : input) {
                if (s.charAt(i) == '#') {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                for (int j = 0; j < input.size(); j++) {
                    input.set(j, new StringBuilder(input.get(j)).insert(i, ".").toString());
                }
                i++;
            }
            i++;
        }
        i = 0;
        while (i < input.size()) {
            final String line = input.get(i);
            if (EMPTY_LINE.matcher(line).matches()) {
                input.add(i, line);
                i++;
            }
            i++;
        }
    }

    public static Galaxy mapToGalaxy(int position, int lineLength) {
        return new Galaxy(position / lineLength, position % lineLength);
    }

    public static int[][] floodFill(Galaxy start, int maxRow, int maxColumn) {
        int[][] grid = new int[maxRow][maxColumn];
        Queue<Flood> queue = new LinkedList<>();
        queue.add(new Flood(start.row(), start.column(), 1));
        while (!queue.isEmpty()) {
            final Flood current = queue.remove();
            if (current.row() < 0 || current.row() >= maxRow || current.column() < 0 || current.column() >= maxColumn || grid[current.row()][current.column()] != 0) {
                continue;
            }
            grid[current.row()][current.column()] = current.iteration();
            queue.add(new Flood(current.row() - 1, current.column(), current.iteration() + 1));
            queue.add(new Flood(current.row() + 1, current.column(), current.iteration() + 1));
            queue.add(new Flood(current.row(), current.column() - 1, current.iteration() + 1));
            queue.add(new Flood(current.row(), current.column() + 1, current.iteration() + 1));
        }
//        System.out.println(Arrays.deepToString(grid).replace("],", "]\n"));
        return grid;
    }

    public record Galaxy(int row, int column) {
    }

    public record Flood(int row, int column, int iteration) {
    }
}

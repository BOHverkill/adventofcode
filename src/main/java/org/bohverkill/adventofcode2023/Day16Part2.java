package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.bohverkill.adventofcode2023.Day16Part1.*;

public class Day16Part2 {
    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day16_example");
        String input = Utils.getInput("/2023/Day16_input");
        final String[] grid = Utils.getLines(input);
        final int count = solve(grid);
        System.out.println("Day 16 Part 2: " + count);
    }

    public static int solve(String[] grid) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < grid.length; i++) {
            max = getMax(max, grid, i, 0, Direction.RIGHT);
        }
        for (int i = 0; i < grid[0].length(); i++) {
            max = getMax(max, grid, 0, i, Direction.DOWN);
        }
        for (int i = 0; i < grid.length; i++) {
            max = getMax(max, grid, i, grid[i].length() - 1, Direction.LEFT);
        }
        for (int i = 0; i < grid[grid.length - 1].length(); i++) {
            max = getMax(max, grid, grid.length - 1, i, Direction.UP);
        }
        return max;
    }

    private static int getMax(int max, String[] grid, int row, int column, Direction direction) {
        Map<Tile, Set<Direction>> energizedGrid = new HashMap<>();
        followBeam(grid, energizedGrid, new Beam(row, column, direction));
        return Math.max(max, energizedGrid.size());
    }
}

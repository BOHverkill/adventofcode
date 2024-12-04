package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.*;

public class Day14Part2 {
    private static final int CYCLES = 1_000_000_000;

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day14_example");
        String input = Utils.getInput("/2023/Day14_input");
        final List<String> platform = Arrays.stream(input.split("\n")).toList();
        Map<List<String>, List<String>> tiltedPlatforms = new HashMap<>();
        List<String> tiltedPlatform = platform;
        for (int i = 0; i < CYCLES; i++) {
            final List<String> platformBefore = tiltedPlatform;
            if (tiltedPlatforms.containsKey(platformBefore)) {
                tiltedPlatform = tiltedPlatforms.get(platformBefore);
            } else {
                for (int j = 0; j < 4; j++) {
                    tiltedPlatform = rotatePlatform(tiltNorth(tiltedPlatform));
                }
                tiltedPlatforms.put(platformBefore, tiltedPlatform);
            }
//            System.out.println("After " + (i + 1) + " cycles:");
//            System.out.println(String.join("\n", tiltedPlatform));
//            if (i % 100_000 == 0) {
//                System.out.printf("%.9f\n", ((double) i) / CYCLES);
//            }
        }
        int load = Day14Part1.calculateLoad(tiltedPlatform);
        System.out.println("Day 14 Part 2: " + load);
    }

    public static List<String> rotatePlatform(List<String> platform) {
        final List<String> rotatedPlatform = new ArrayList<>(platform.size());
        for (int i = 0; i < platform.getFirst().length(); i++) {
            StringBuilder newRow = new StringBuilder();
            for (int j = platform.size() - 1; j >= 0; j--) {
                newRow.append(platform.get(j).charAt(i));
            }
            rotatedPlatform.add(newRow.toString());
        }
        return rotatedPlatform;
    }

    // more optimized version from part 1
    public static List<String> tiltNorth(List<String> platform) {
        final List<String> tiltedPlatform = new ArrayList<>(platform);
        for (int i = 1; i < tiltedPlatform.size(); i++) {
            for (int j = 0; j < tiltedPlatform.get(i).length(); j++) {
                if (tiltedPlatform.get(i).charAt(j) == 'O' && tiltedPlatform.get(i - 1).charAt(j) == '.') {
                    int newI = i - 1;
                    for (int k = i - 2; k >= 0; k--) {
                        if (tiltedPlatform.get(k).charAt(j) == '.') {
                            newI = k;
                        } else {
                            break;
                        }
                    }
                    tiltedPlatform.set(i, StringUtils.replaceCharAt(tiltedPlatform.get(i), j, '.'));
                    tiltedPlatform.set(newI, StringUtils.replaceCharAt(tiltedPlatform.get(newI), j, 'O'));
                }
            }
        }
        return tiltedPlatform;
    }
}

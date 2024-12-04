package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14Part1 {
    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day14_example");
        String input = Utils.getInput("/2023/Day14_input");
        final List<String> platform = Arrays.stream(input.split("\n")).toList();
        final List<String> tiltedPlatform = tiltNorth(platform);
//        System.out.println(String.join("\n", tiltedPlatform));
        int load = calculateLoad(tiltedPlatform);
        System.out.println("Day 14 Part 1: " + load);
    }

    public static List<String> tiltNorth(List<String> platform) {
        final List<String> tiltedPlatform = new ArrayList<>(platform);
        boolean changed;
        do {
            changed = false;
            for (int i = 1; i < tiltedPlatform.size(); i++) {
                for (int j = 0; j < tiltedPlatform.get(i).length(); j++) {
                    if (tiltedPlatform.get(i).charAt(j) == 'O' && tiltedPlatform.get(i - 1).charAt(j) == '.') {
                        tiltedPlatform.set(i, StringUtils.replaceCharAt(tiltedPlatform.get(i), j, '.'));
                        tiltedPlatform.set(i - 1, StringUtils.replaceCharAt(tiltedPlatform.get(i - 1), j, 'O'));
                        changed = true;
                    }
                }
            }
        } while (changed);
        return tiltedPlatform;
    }

    public static int calculateLoad(List<String> platform) {
        int load = 0;
        for (int i = 0; i < platform.size(); i++) {
            for (int j = 0; j < platform.get(i).length(); j++) {
                if (platform.get(i).charAt(j) == 'O') {
                    load += platform.size() - i;
                }
            }
        }
        return load;
    }
}

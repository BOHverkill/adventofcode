package org.bohverkill.adventofcode2024;

import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day9 {
    public static void main(String[] args) {
//        final String path = "/2024/Day9_example";
        final String path = "/2024/Day9_input";
        final String input = Utils.getInput(path).trim();
        final List<Integer> diskMap = getDiskMap(input);
        task1(new ArrayList<>(diskMap));
        task2(new ArrayList<>(diskMap));
    }

    private static List<Integer> getDiskMap(String input) {
        final List<Integer> diskMap = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            final int num = Integer.parseInt(String.valueOf(input.charAt(i)));
            if (i % 2 == 0) {
                for (int j = 0; j < num; j++) {
                    diskMap.add(i / 2);
                }
            } else {
                for (int j = 0; j < num; j++) {
                    diskMap.add(-1);
                }
            }
        }
        return diskMap;
    }

    private static void task1(final List<Integer> diskMap) {
        int index = diskMap.size() - 1;
        while (true) {
            int firstFreeSpace = IntStream.range(0, diskMap.size()).filter(i -> diskMap.get(i) == -1).findFirst().orElseThrow();
            if (firstFreeSpace >= index) {
                break;
            }
            final int id = diskMap.get(index);
            if (id != -1) {
                diskMap.set(firstFreeSpace, id);
                diskMap.set(index, -1);
            }
            index--;
        }

        final long sum = getFilesystemChecksum(diskMap);

        System.out.println("Part 1: " + sum);
    }

    private static void task2(final List<Integer> diskMap) {
        int index = diskMap.size() - 1;
        while (index >= 0) {
            final int id = diskMap.get(index);
            int endIndex = index - 1;
            while (endIndex >= 0 && id == diskMap.get(endIndex)) {
                endIndex--;
            }
            if (id != -1) {
                int size = 0;
                for (int i = 0; i <= endIndex + 1; i++) {
                    if (diskMap.get(i) == -1) {
                        size++;
                    } else {
                        if (size != 0) {
                            if (size >= index - endIndex) {
                                for (int j = 0; j < index - endIndex; j++) {
                                    diskMap.set((i - size) + j, id);
                                    diskMap.set(endIndex + j + 1, -1);
                                }
                                break;
                            } else {
                                size = 0;
                            }
                        }
                    }
                }
            }
            index = endIndex;
        }

        final long sum = getFilesystemChecksum(diskMap);

        System.out.println("Part 2: " + sum);
    }

    private static long getFilesystemChecksum(List<Integer> diskMap) {
        return IntStream.range(0, diskMap.size()).filter(i -> diskMap.get(i) != -1).mapToLong(i -> (long) i * diskMap.get(i)).sum();
    }

//    private static void printDiskMap(final List<Integer> diskMap) {
//        StringBuilder sb = new StringBuilder();
//        for (Integer i : diskMap) {
//            if (i != -1) {
//                sb.append(i);
//            } else {
//                sb.append(".");
//            }
//        }
//        System.out.println(sb);
//    }
}

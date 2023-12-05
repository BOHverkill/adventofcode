package org.bohverkill.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {
    private static final Pattern SEED_PATTERN = Pattern.compile("^seeds: ([\\d ]+)\n(.*)$", Pattern.DOTALL);
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^(\\w+)-to-(\\w+) map:$");

    public static void main(String[] args) {
        try {
//            String inputString = Files.readString(Paths.get(Objects.requireNonNull(Day5.class.getResource("/2023/Day5_example")).getPath()));
            String inputString = Files.readString(Paths.get(Objects.requireNonNull(Day5.class.getResource("/2023/Day5_input")).getPath()));
            final Almanac almanac = parseAlmanac(inputString);
            System.out.println(almanac.seeds.stream().mapToLong(seed -> getLocation(seed, almanac.categories)).min().getAsLong());
            final AlmanacWithSeed almanacWithSeed = parseAlmanacWithSeed(inputString);
            System.out.println(almanacWithSeed.seeds.stream().mapToLong(seed -> getMinLocation(seed, almanacWithSeed.categories)).min().getAsLong());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getLocation(long seed, List<Category> categories) {
        long current = seed;
        for (Category category : categories) {
            current = getCategory(category, current);
        }
        return current;
    }

    public static long getMinLocation(Seed seed, List<Category> categories) {
        long min = Long.MAX_VALUE;
        for (int i = 0; i < seed.length; i++) {
            long current = seed.start + i;
            for (Category category : categories) {
                current = getCategory(category, current);
            }
            if (min > current) {
                min = current;
            }
        }
        return min;
    }

    public static long getCategory(Category category, long input) {
        for (Range map : category.ranges) {
            if (input >= map.sourceRangeStart && input < map.sourceRangeStart + map.rangeLength) {
                return (input - map.sourceRangeStart) + map.destinationRangeStart;
            }
        }
        return input;
    }

    public static Almanac parseAlmanac(String input) {
        final Matcher seedsMatcher = SEED_PATTERN.matcher(input);
        if (!seedsMatcher.find()) {
            throw new IllegalStateException("Malformed input: " + input);
        }
        final List<Long> seeds = Arrays.stream(seedsMatcher.group(1).split(" ")).map(Long::parseLong).toList();

        final String[] split = seedsMatcher.group(2).split("\n\n");
        final List<Category> categories = Arrays.stream(split).map(Day5::parseCategory).toList();
        return new Almanac(seeds, categories);
    }

    public static AlmanacWithSeed parseAlmanacWithSeed(String input) {
        final Matcher seedsMatcher = SEED_PATTERN.matcher(input);
        if (!seedsMatcher.find()) {
            throw new IllegalStateException("Malformed input: " + input);
        }
        final List<Seed> seeds = parseSeeds(Arrays.stream(seedsMatcher.group(1).split(" ")).map(Long::parseLong).toList());

        final String[] split = seedsMatcher.group(2).split("\n\n");
        final List<Category> categories = Arrays.stream(split).map(Day5::parseCategory).toList();
        return new AlmanacWithSeed(seeds, categories);
    }

    public static List<Seed> parseSeeds(List<Long> seeds) {
        List<Seed> expanded = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i += 2) {
            final Long start = seeds.get(i);
            final Long length = seeds.get(i + 1);
            expanded.add(new Seed(start, length));
        }
        return expanded;
    }

    public static Category parseCategory(String categoryString) {
        final List<String> split = Arrays.stream(categoryString.trim().split("\n")).toList();
        if (split.size() < 2) {
            throw new IllegalStateException("Malformed input: " + categoryString);
        }
        final String headerString = split.get(0);
        final Matcher headerMatcher = CATEGORY_PATTERN.matcher(headerString);
        if (!headerMatcher.find()) {
            throw new IllegalStateException("Malformed input: " + headerString);
        }
        String sourceCategory = headerMatcher.group(1);
        String destinationCategory = headerMatcher.group(2);
        final List<Range> map = split.subList(1, split.size()).stream().map(Day5::parseMapping).toList();
        return new Category(sourceCategory, destinationCategory, map);
    }

    public static Range parseMapping(String line) {
        final String[] split = line.split(" ");
        if (split.length != 3) {
            throw new IllegalStateException("Malformed input: " + line);
        }
        final long destinationRangeStart = Long.parseLong(split[0]);
        final long sourceRangeStart = Long.parseLong(split[1]);
        final int rangeLength = Integer.parseInt(split[2]);

        return new Range(sourceRangeStart, destinationRangeStart, rangeLength);
    }

    public record Almanac(List<Long> seeds, List<Category> categories) {
    }

    public record Category(String sourceCategory, String destinationCategory, List<Range> ranges) {
    }

    public record Range(long sourceRangeStart, long destinationRangeStart, int rangeLength) {
    }

    public record AlmanacWithSeed(List<Seed> seeds, List<Category> categories) {
    }

    public record Seed(long start, long length) {
    }
}

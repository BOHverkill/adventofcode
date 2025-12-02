package org.bohverkill.adventofcode2025;

import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day2 {
    static void main() {
//        final String name = "/2025/Day2_example";
        final String name = "/2025/Day2_input";
        final List<Range> ranges = Utils.getInputSplit(name, ",").map(Day2::parseRange).toList();
        task1(ranges);
        task2(ranges);
    }

    private static Range parseRange(String input) {
        String[] split = input.split("-");
        return Range.of(Long.parseLong(split[0]), Long.parseLong(split[1]));
    }

    private static void task1(final List<Range> ranges) {
        long count = ranges.stream().flatMap(Day2::expandRange).mapToLong(value -> value).filter(Day2::isInvalid1).sum();
        System.out.println("Part 1: " + count);
    }

    private static Stream<Long> expandRange(Range range) {
        return LongStream.range(range.first(), range.last() + 1).boxed();
    }

    private static boolean isInvalid1(long id) {
        String[] strings = StringUtils.splitMiddle(String.valueOf(id));
        return strings[0].equals(strings[1]);
    }

    private static void task2(final List<Range> ranges) {
        long count = ranges.stream().flatMap(Day2::expandRange).mapToLong(value -> value).filter(Day2::isInvalid2).sum();
        System.out.println("Part 2: " + count);
    }

    private static boolean isInvalid2(long id) {
        String s = String.valueOf(id);
        for (int i = s.length() / 2; i > 0; i--) {
            long count = Pattern.compile(s.substring(0, i), Pattern.LITERAL).matcher(s.substring(i)).results().count();
            if ((s.length() - i) / (double) i == count) {
                return true;
            }
        }
        return false;
    }

    private record Range(long first, long last) {
        public static Range of(long first, long last) {
            return new Range(first, last);
        }
    }
}

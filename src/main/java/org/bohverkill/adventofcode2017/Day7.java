package org.bohverkill.adventofcode2017;

import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7 {
    private static final Pattern PROGRAM_PATTERN = Pattern.compile("^([a-z]+) \\((\\d+)\\)( -> ([a-z, ]+))?$");

    public static void main(String[] args) {
//        final String[] input = Utils.getInputArray("/2017/Day7_example");
        final String[] input = Utils.getInputArray("/2017/Day7_input");
        final Program root = task1(input);
        task2(root);
    }

    private static Program task1(final String[] input) {
        Set<Program> programs = new HashSet<>();

        for (String line : input) {
            final Matcher programMatcher = Utils.getMatcher(PROGRAM_PATTERN, line);

            String name = programMatcher.group(1);
            int weight = Integer.parseInt(programMatcher.group(2));
            final String aboveGroup = programMatcher.group(4);
            final Program newProgram = Program.of(name, weight);
            if (aboveGroup != null) {
                Arrays.stream(aboveGroup.split(",")).map(String::trim).forEach(s -> add(programs, newProgram, s));
            }
            if (programs.contains(newProgram)) {
                final Program oldProgram = programs.stream().filter(p -> p.equals(newProgram)).findFirst().orElseThrow();
                oldProgram.setWeight(newProgram.weight());
                newProgram.above().stream().filter(p -> !oldProgram.above().contains(p)).forEach(p -> oldProgram.above().add(p));
            } else {
                programs.add(newProgram);
            }
        }

        final Program root = programs.stream().filter(p -> p.below() == null).findFirst().orElseThrow();
        System.out.println("Part 1: " + root.name());
        return root;
    }

    private static void add(Set<Program> programs, Program below, String name) {
        Program above = Program.of(name);
        if (programs.contains(above)) {
            final Program finalAbove = above;
            above = programs.stream().filter(p -> p.equals(finalAbove)).findFirst().orElseThrow();
        } else {
            programs.add(above);
        }
        above.setBelow(below);
        below.above().add(above);
    }

    private static void task2(final Program root) {
        Program current = root;
        while (true) {
            Map<Integer, List<Program>> map = current.above().stream().collect(Collectors.groupingBy(Day7::calculateWeight));
            final Optional<Map.Entry<Integer, List<Program>>> first = map.entrySet().stream().filter(integerListEntry -> integerListEntry.getValue().size() == 1).findFirst();
            if (first.isEmpty()) {
                break;
            }
            current = first.get().getValue().getFirst();
        }

        Map<Integer, List<Program>> map = current.below().above().stream().collect(Collectors.groupingBy(Day7::calculateWeight));
        Program finalCurrent = current;
        final Map.Entry<Integer, List<Program>> neighborWeightEntry = map.entrySet().stream().filter(e -> !e.getValue().contains(finalCurrent)).findAny().orElseThrow();
        System.out.println("Part 2: " + (neighborWeightEntry.getKey() - calculateAboveWeight(current)));
    }

    private static int calculateWeight(Program root) {
        return root.weight() + calculateAboveWeight(root);
    }

    private static int calculateAboveWeight(Program root) {
        return root.above().stream().mapToInt(Day7::calculateWeight).sum();
    }


    private static final class Program {
        private final String name;
        private final List<Program> above;
        private int weight;
        private Program below;

        private Program(String name, int weight) {
            this.name = name;
            this.weight = weight;
            this.below = null;
            this.above = new ArrayList<>();
        }

        private static Program of(String name, int weight) {
            return new Program(name, weight);
        }

        private static Program of(String name) {
            return new Program(name, 0);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Program program = (Program) o;
            return Objects.equals(name(), program.name());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name());
        }

        @Override
        public String toString() {
            return "Program[" + "name=" + name + ", " + "weight=" + weight + ", " + "below=" + below + ']';
        }

        public String name() {
            return name;
        }

        public int weight() {
            return weight;
        }

        public Program below() {
            return below;
        }

        public List<Program> above() {
            return above;
        }

        public void setBelow(Program below) {
            this.below = below;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}

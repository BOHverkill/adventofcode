package org.bohverkill.adventofcode2023;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day15Part2 {
    private static final int BOX_COUNT = 256;

    private static final Pattern STEP_PATTERN = Pattern.compile("^([a-z]+)([=-])(\\d*)$");

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day15_example");
        final String input = Utils.getInput("/2023/Day15_input");
        final List<List<Lens>> boxes = new ArrayList<>(BOX_COUNT);
        IntStream.range(0, BOX_COUNT).forEach(i -> boxes.add(new LinkedList<>()));
        Utils.getSplit(input, ",").map(Day15Part2::parseStep).forEach(step -> perform(step, boxes));
        int sum = 0;
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = 0; j < boxes.get(i).size(); j++) {
                final int focusingPower = getFocusingPower(i, j + 1, boxes.get(i).get(j).focalLength());
                sum += focusingPower;
            }
        }
        System.out.println("Day 15 Part 2: " + sum);
    }

    public static Step parseStep(String step) {
        final Matcher matcher = Utils.getMatcher(STEP_PATTERN, step);
        final String label = matcher.group(1);
        final Operation operation = Operation.parse(matcher.group(2));
        final String group = matcher.group(3);
        int focalLength = group.isEmpty() ? -1 : Integer.parseInt(group);
        return new Step(label, operation, focalLength);
    }

    public static void perform(Step step, List<List<Lens>> boxes) {
        final List<Lens> box = boxes.get(step.hash());
        switch (step.operation()) {
            case DASH -> box.remove(step.lens());
            case EQUALS_SIGN -> {
                for (int i = 0; i < box.size(); i++) {
                    if (box.get(i).label().equals(step.label())) {
                        box.set(i, step.lens());
                        return;
                    }
                }
                box.addLast(step.lens());
            }
            default -> throw new IllegalStateException("Unexpected value: " + step.operation());
        }
    }

    public static int getFocusingPower(int boxNumber, int slotNumber, int focalLength) {
        return (1 + boxNumber) * slotNumber * focalLength;
    }

    public enum Operation {
        EQUALS_SIGN, DASH;

        public static Operation parse(String operation) {
            return switch (operation) {
                case "=" -> EQUALS_SIGN;
                case "-" -> DASH;
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            };
        }
    }

    public record Step(String label, Operation operation, int focalLength) {
        public char hash() {
            return Day15Part1.hash(label);
        }

        public Lens lens() {
            return new Lens(label(), focalLength());
        }
    }

    public record Lens(String label, int focalLength) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lens lens = (Lens) o;
            return Objects.equals(label, lens.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }
    }
}

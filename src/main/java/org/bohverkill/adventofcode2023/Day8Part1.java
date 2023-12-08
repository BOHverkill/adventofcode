package org.bohverkill.adventofcode2023;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8Part1 {
    private static final String START_NODE = "AAA";
    private static final String END_NODE = "ZZZ";

    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("^([RL]+)\n\n(.*)$", Pattern.DOTALL);
    private static final Pattern NODE_PATTERN = Pattern.compile("^([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)$");
//    private static final Pattern NODE_PATTERN = Pattern.compile("^([A-Z12]{3}) = \\(([A-Z12]{3}), ([A-Z12]{3})\\)$");  // needed for example of part 2

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day8_1_example");
//        String input = Utils.getInput("/2023/Day8_1_1_example");
        String input = Utils.getInput("/2023/Day8_input");
        System.out.println("Day8 Part 1: " + countSteps(parseNetwork(input)));
    }

    public static Network parseNetwork(String input) {
        final Matcher instructionMatcher = Utils.getMatcher(INSTRUCTION_PATTERN, input);
        String instructionLine = instructionMatcher.group(1);
        String nodesLines = instructionMatcher.group(2);

        final List<String> instructions = Arrays.stream(instructionLine.split("")).toList();
        final Map<String, Node> nodes = Arrays.stream(nodesLines.split("\n")).map(Day8Part1::parseNode).collect(Collectors.toMap(node -> node.name, node -> node));

        return new Network(instructions, nodes);
    }

    public static Node parseNode(String line) {
        final Matcher nodeMatcher = Utils.getMatcher(NODE_PATTERN, line);

        final String name = nodeMatcher.group(1);
        final String left = nodeMatcher.group(2);
        final String right = nodeMatcher.group(3);

        return new Node(name, left, right);
    }

    public static int countSteps(Network network) {
        Node current = network.nodes.get(START_NODE);
        int step = 0;

        while (!current.name.equals(END_NODE)) {
            final String instruction = network.instructions.get(step % network.instructions.size());
            current = switch (instruction) {
                case "R" -> network.nodes.get(current.right);
                case "L" -> network.nodes.get(current.left);
                default -> throw new IllegalStateException("Impossible");
            };
            step++;
        }
        return step;
    }

    public record Network(List<String> instructions, Map<String, Node> nodes) {
    }

    public record Node(String name, String left, String right) {
    }
}

package org.bohverkill.adventofcode2023;

import static org.bohverkill.adventofcode2023.Day8Part1.*;

public class Day8Part2 {
    private static final String START_NODE_END = "A";
    private static final String END_NODE_END = "Z";

    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day8_2_example");
        String input = Utils.getInput("/2023/Day8_input");
        System.out.println("Day8 Part 2: " + countStepsSimultaneously(parseNetwork(input)));
    }

    public static long countStepsSimultaneously(Network network) {
        return network.nodes().values().stream().filter(v -> v.name().endsWith(START_NODE_END)).map(node -> countSteps(node, network)).mapToLong(value -> value).reduce(1, Day8Part2::lcm);
    }

    public static long countSteps(Node node, Network network) {
        Node current = node;
        long step = 0;

        while (!current.name().endsWith(END_NODE_END)) {
            final String instruction = network.instructions().get(Math.toIntExact(step % network.instructions().size()));
            current = switch (instruction) {
                case "R" -> network.nodes().get(current.right());
                case "L" -> network.nodes().get(current.left());
                default -> throw new IllegalStateException("Impossible");
            };
            step++;
        }
        return step;
    }

    // from https://stackoverflow.com/a/4202114
    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    // Brute force method does not work :/
//    public static long countStepsSimultaneously(Network network) {
//        List<Node> currentNodes = new ArrayList<>(network.nodes().values().stream().filter(v -> v.name().endsWith(START_NODE_END)).toList());
//        List<Node> currentNewNodes = new ArrayList<>();
//        long step = 0;
//
//        while (!currentNodes.stream().allMatch(node -> node.name().endsWith(END_NODE_END))) {
//            currentNewNodes.clear();
//            final String instruction = network.instructions().get(Math.toIntExact(step % network.instructions().size()));
//            for (Node currentNode : currentNodes) {
//                currentNewNodes.add(switch (instruction) {
//                    case "R" -> network.nodes().get(currentNode.right());
//                    case "L" -> network.nodes().get(currentNode.left());
//                    default -> throw new IllegalStateException("Impossible");
//                });
//            }
//            currentNodes.clear();
//            currentNodes.addAll(currentNewNodes);
//            step++;
//        }
//
//        return step;
//    }
}

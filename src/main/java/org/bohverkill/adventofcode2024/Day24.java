package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day24 {
    private static final Pattern GATE_CONNECTION_PATTERN = Pattern.compile("^([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)$");
    private static final int MAX_INPUT_NUMBER = 45;

    public static void main(String[] args) {
//        final String path = "/2024/Day24_example";
        final String path = "/2024/Day24_input";
        final String input = Utils.getInput(path);
        final Pair<List<Pair<String, Boolean>>, List<GateConnection>> parse = parse(input);
        task1(parse.a(), parse.b());
        task2(parse.b());
    }

    private static Pair<List<Pair<String, Boolean>>, List<GateConnection>> parse(final String input) {
        final String[] split = input.split("\n\n");
        final List<Pair<String, Boolean>> initialWireValues = Arrays.stream(split[0].split("\n")).map(string -> string.split(": ")).map(strings -> Pair.of(strings[0], convertToBoolean(strings[1]))).toList();
        final List<GateConnection> gateConnections = Arrays.stream(split[1].split("\n")).map(Day24::parseGateConnection).toList();
        return Pair.of(initialWireValues, gateConnections);
    }

    private static GateConnection parseGateConnection(final String line) {
        final Matcher gateConnectionMatcher = Utils.getMatcher(GATE_CONNECTION_PATTERN, line);
        final String input1 = gateConnectionMatcher.group(1);
        final LogicGate logicGate = LogicGate.fromString(gateConnectionMatcher.group(2));
        final String input2 = gateConnectionMatcher.group(3);
        final String output = gateConnectionMatcher.group(4);
        return GateConnection.of(input1, input2, logicGate, output);
    }

    private static boolean convertToBoolean(final String input) {
        if (input.equals("1")) {
            return true;
        } else if (input.equals("0")) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid input: " + input);
        }
    }

    private static void task1(final List<Pair<String, Boolean>> initialWireValues, final List<GateConnection> gateConnections) {
        Map<String, Boolean> states = initialWireValues.stream().collect(Collectors.toMap(Pair::a, Pair::b));
        final long res = performCalculation(states, gateConnections);

        System.out.println("Part 1: " + res);
    }

    private static long performCalculation(Map<String, Boolean> states, List<GateConnection> gateConnections) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (GateConnection gateConnection : gateConnections) {
                if (!states.containsKey(gateConnection.output()) && states.containsKey(gateConnection.input1()) && states.containsKey(gateConnection.input2())) {
                    boolean connected = gateConnection.gate().perform(states.get(gateConnection.input1()), states.get(gateConnection.input2()));
                    states.put(gateConnection.output(), connected);
                    changed = true;
                }
            }
        }

        final List<Map.Entry<String, Boolean>> z = new ArrayList<>(states.entrySet().stream().filter(e -> e.getKey().startsWith("z")).toList());
        z.sort(Map.Entry.comparingByKey());
        return booleanListToLong(z.stream().map(Map.Entry::getValue).toList().reversed());
    }

    /**
     * Converts a list of booleans to a long, starting from the most significant bit
     *
     * @param booleans the booleans values to convert, starting with the most significant bits
     * @return a number representing the boolean list
     */
    private static long booleanListToLong(List<Boolean> booleans) {
        long out = 0;
        for (boolean b : booleans) {
            out = (out << 1) | (b ? 1 : 0);
        }
        return out;
    }

    private static void task2(final List<GateConnection> gateConnections) {
        Map<String, Boolean> states = new HashMap<>();
        long input1 = 0;
        long input2 = 0;
        for (int i = 0; i < MAX_INPUT_NUMBER; i++) {
            states.put(String.format("x%02d", i), true);
            input1 = (input1 << 1) | 1;
            states.put(String.format("y%02d", i), true);
            input2 = (input2 << 1) | 1;
        }

        long res = performCalculation(new HashMap<>(states), gateConnections);
        System.out.println("x = " + input1 + ", y = " + input2 + ": x + y = " + res + " (" + Long.toBinaryString(res) + ")" + (res == (input1 + input2) ? " == " : " != ") + (input1 + input2) + " (" + Long.toBinaryString(input1 + input2) + ")");

        printGraph(gateConnections);

        System.out.println("wrongInputGates = " + gateConnections.stream().filter(gateConnection2 -> gateConnection2.input1().startsWith("x") || gateConnection2.input2().startsWith("x") || gateConnection2.input1().startsWith("y") || gateConnection2.input2().startsWith("y")).filter(gateConnection2 -> gateConnection2.gate() == LogicGate.OR).toList());
        System.out.println("wrongOutputGates = " + gateConnections.stream().filter(gateConnection2 -> gateConnection2.output().startsWith("z")).filter(gateConnection2 -> gateConnection2.gate() != LogicGate.XOR).toList());
        // the last output value is correct, because it just contains the carry

        List<GateConnection> corrected = new ArrayList<>(gateConnections);

        final List<Pair<String, String>> wrong = List.of(Pair.of("z05", "frn"), Pair.of("z21", "gmq"), Pair.of("z39", "wtt"), Pair.of("wnf", "vtj"));
        for (Pair<String, String> stringStringPair : wrong) {
            swapGateOutputs(corrected, stringStringPair.a(), stringStringPair.b());
        }

        res = performCalculation(new HashMap<>(states), corrected);
        System.out.println("x = " + input1 + ", y = " + input2 + ": x + y = " + res + (res == (input1 + input2) ? " == " : " != ") + (input1 + input2));

        final String out = wrong.stream().flatMap(stringStringPair -> Stream.of(stringStringPair.a(), stringStringPair.b())).sorted().collect(Collectors.joining(","));
        System.out.println("Part 2: " + out);
    }

    private static void swapGateOutputs(List<GateConnection> corrected, String gate1Name, String gate2Name) {
        final GateConnection gate1 = getAndRemove(corrected, gate1Name);
        final GateConnection gate2 = getAndRemove(corrected, gate2Name);
        corrected.add(GateConnection.of(gate1.input1(), gate1.input2(), gate1.gate(), gate2.output()));
        corrected.add(GateConnection.of(gate2.input1(), gate2.input2(), gate2.gate(), gate1.output()));
    }

    private static GateConnection getAndRemove(List<GateConnection> gateConnections, String gateOutputName) {
        final GateConnection toRemove = gateConnections.stream().filter(gateConnection -> gateConnection.output().equals(gateOutputName)).findAny().orElseThrow();
        gateConnections.remove(toRemove);
        return toRemove;
    }

    private static void printGraph(final List<GateConnection> gateConnections) {
        System.out.println("graph {");
        for (GateConnection gateConnection : gateConnections) {
            System.out.println("\t" + gateConnection.input1() + " -- " + gateConnection.output() + "[label=\"" + gateConnection.gate() + "\"]");
            System.out.println("\t" + gateConnection.input2() + " -- " + gateConnection.output() + "[label=\"" + gateConnection.gate() + "\"]");
        }
        System.out.println("}");
        // dot -Tx11 input.dot
    }

    private enum LogicGate {
        AND, OR, XOR;

        private static LogicGate fromString(final String logicGate) {
            return switch (logicGate) {
                case "AND" -> AND;
                case "OR" -> OR;
                case "XOR" -> XOR;
                default -> throw new IllegalArgumentException("Invalid logicGate: " + logicGate);
            };
        }

        private boolean perform(boolean input1, boolean input2) {
            return switch (this) {
                case AND -> input1 && input2;
                case OR -> input1 || input2;
                case XOR -> input1 ^ input2;
            };
        }
    }

    private record GateConnection(String input1, String input2, LogicGate gate, String output) {
        private static GateConnection of(String input1, String input2, LogicGate gate, String output) {
            return new GateConnection(input1, input2, gate, output);
        }
    }
}

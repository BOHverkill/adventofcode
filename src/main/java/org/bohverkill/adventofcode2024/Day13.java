package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.MathUtils;
import org.bohverkill.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {
    private static final int A_COST = 3;
    private static final int B_COST = 1;
    private static final long PART_TWO_OFFSET = 10000000000000L;

    private static final Pattern MACHINE_PATTERN = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)\\nButton B: X\\+(\\d+), Y\\+(\\d+)\\nPrize: X=(\\d+), Y=(\\d+)");

    public static void main(String[] args) {
//        final String path = "/2024/Day13_example";
        final String path = "/2024/Day13_input";
        final String input = Utils.getInput(path);
        final List<Machine> machines = parse(input);
        task1(machines);
        task2(machines);
    }

    private static List<Machine> parse(String input) {
        final Matcher matcher = MACHINE_PATTERN.matcher(input);
        List<Machine> machines = new ArrayList<>();
        while (matcher.find()) {
            final long aX = Long.parseLong(matcher.group(1));
            final long aY = Long.parseLong(matcher.group(2));
            final long bX = Long.parseLong(matcher.group(3));
            final long bY = Long.parseLong(matcher.group(4));
            final long priceX = Long.parseLong(matcher.group(5));
            final long priceY = Long.parseLong(matcher.group(6));
            machines.add(Machine.of(Pair.of(aX, aY), Pair.of(bX, bY), Pair.of(priceX, priceY)));
        }
        return machines;
    }

    private static void task1(final List<Machine> machines) {
        long res = machines.stream().mapToLong(Day13::solve).sum();
        System.out.println("Part 1: " + res);
    }

    private static long solve(Machine machine) {
        double[][] coefficientMatrix = new double[2][2];
        double[] rhs = new double[2];
        coefficientMatrix[0][0] = machine.a().a();
        coefficientMatrix[1][0] = machine.a().b();
        coefficientMatrix[0][1] = machine.b().a();
        coefficientMatrix[1][1] = machine.b().b();
        rhs[0] = machine.price().a();
        rhs[1] = machine.price().b();
        MathUtils.gaussianElimination(coefficientMatrix, rhs);

        if (MathUtils.isInt(rhs[0], 1e-2) && MathUtils.isInt(rhs[1], 1e-2)) {
            return Math.round(rhs[0] * A_COST + rhs[1] * B_COST);
        } else {
            return 0;
        }
    }

    private static void task2(final List<Machine> machines) {
        long res = machines.stream().map(machine -> Machine.of(machine.a(), machine.b(), Pair.of(machine.price().a() + PART_TWO_OFFSET, machine.price().b() + PART_TWO_OFFSET))).mapToLong(Day13::solve).sum();
        System.out.println("Part 2: " + res);
    }

    private record Machine(Pair<Long, Long> a, Pair<Long, Long> b, Pair<Long, Long> price) {
        private static Machine of(Pair<Long, Long> a, Pair<Long, Long> b, Pair<Long, Long> price) {
            return new Machine(a, b, price);
        }
    }
}

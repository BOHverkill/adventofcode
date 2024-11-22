package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bohverkill.adventofcode2023.Day20Part1.Module;
import static org.bohverkill.adventofcode2023.Day20Part1.*;

public class Day20Part2 {

    private static final String FINAL_MODULE_NAME = "rx";

    public static void main(String[] args) {
        final Stream<String> lines = Utils.getInputLines("/2023/Day20_input");

        final List<Module> modules = lines.map(Day20Part1::parseModule).toList();

        Map<String, Module> modulesMap = modules.stream().collect(Collectors.toMap(Module::name, module -> module));

        Map<String, List<String>> moduleInputs = new HashMap<>();
        modules.forEach(module -> module.destinationModules().stream().map(string -> moduleInputs.computeIfAbsent(string, k -> new ArrayList<>())).forEach(moduleInput -> moduleInput.add(module.name())));
        modules.forEach(module -> module.initializeWithInputs(moduleInputs.get(module.name())));
        final String moduleBeforeFinal = moduleInputs.get(FINAL_MODULE_NAME).getFirst(); // let assume we have only on module that ends in "rx"
        final int moduleBeforeFinalNumberInputs = moduleInputs.get(moduleBeforeFinal).size();

        final long res = perform(modulesMap, moduleBeforeFinal, moduleBeforeFinalNumberInputs);
        System.out.println("Day 20 Part 2: " + res);
    }

    public static long perform(Map<String, Module> modulesMap, String moduleBeforeFinal, int moduleBeforeFinalNumberInputs) {
        final Module broadcastModule = modulesMap.get(Day20Part1.BroadcastModule.NAME);
        final List<Day20Part1.ModuleState> initialState = List.of(new Day20Part1.ModuleState(broadcastModule, "button", false));
        Map<String, Integer> loopMap = new HashMap<>();
        int i = 1;
        while (!pushButton(modulesMap, initialState, moduleBeforeFinal, loopMap, moduleBeforeFinalNumberInputs, i)) {
            i++;
        }
//        return i;
        return lcm(loopMap.values().stream().mapToLong(value -> value).toArray());
    }

    public static boolean pushButton(Map<String, Module> modulesMap, List<ModuleState> initialState, String moduleBeforeFinal, Map<String, Integer> loopMap, int moduleBeforeFinalNumberInputs, int i) {
        Queue<ModuleState> currentModules = new ArrayDeque<>(initialState);

        while (!currentModules.isEmpty()) {
            ModuleState current = currentModules.remove();
//            if (current.module().name().equals(FINAL_MODULE_NAME) && (!current.signal())) {
//                    return true;
//            }
            if (current.module().name().equals(moduleBeforeFinal) && current.signal()) { // let's assume that the module before final is a conjunction module
                loopMap.putIfAbsent(current.inputName(), i);
            }
            Optional<Boolean> outputSignal = current.module().process(current.inputName(), current.signal());
            outputSignal.ifPresent(b -> currentModules.addAll(current.module().destinationModules().stream().map(key -> modulesMap.getOrDefault(key, new UntypedModule(key))).map(module -> new ModuleState(module, current.module().name(), b)).toList()));
        }
        return loopMap.size() == moduleBeforeFinalNumberInputs;
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

    private static long lcm(long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }
}

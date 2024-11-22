package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day20Part1 {
    private static final Pattern MODULE_PATTERN = Pattern.compile("^([%&]?)([a-z]+) -> ([a-z, ]+)$");
    private static final int BUTTON_PRESS_TIMES = 1000;

    public static void main(String[] args) {
//        final Stream<String> lines = Utils.getInputLines("/2023/Day20_example");
//        final Stream<String> lines = Utils.getInputLines("/2023/Day20_1_1_example");
        final Stream<String> lines = Utils.getInputLines("/2023/Day20_input");

        final List<Module> modules = lines.map(Day20Part1::parseModule).toList();

        Map<String, Module> modulesMap = modules.stream().collect(Collectors.toMap(Module::name, module -> module));

        Map<String, List<String>> moduleInputs = new HashMap<>();
        modules.forEach(module -> module.destinationModules().stream().map(string -> moduleInputs.computeIfAbsent(string, k -> new ArrayList<>())).forEach(moduleInput -> moduleInput.add(module.name())));
        modules.forEach(module -> module.initializeWithInputs(moduleInputs.get(module.name())));

        final int res = perform(modulesMap);
        System.out.println("Day 20 Part 1: " + res);
    }

    public static int perform(Map<String, Module> modulesMap) {
        final Module broadcastModule = modulesMap.get(BroadcastModule.NAME);
        final List<ModuleState> initialState = List.of(new ModuleState(broadcastModule, "button", false));
        Pulses pulses = new Pulses(0, 0);
        for (int i = 0; i < BUTTON_PRESS_TIMES; i++) {
            final Pulses currentPulses = pushButton(modulesMap, initialState);
            pulses = new Pulses(pulses.low() + currentPulses.low(), pulses.high() + currentPulses.high());
        }
        return pulses.low() * pulses.high();
    }

    public static Pulses pushButton(Map<String, Module> modulesMap, List<ModuleState> initialState) {
        Queue<ModuleState> currentModules = new ArrayDeque<>(initialState);

        int lowPulses = 0;
        int highPulses = 0;
        while (!currentModules.isEmpty()) {
            ModuleState current = currentModules.remove();
//            System.out.println(current.inputName() + " -" + (current.signal() ? "high" : "low") + "-> " + current.module().name());
            if (current.signal()) {
                highPulses++;
            } else {
                lowPulses++;
            }
            Optional<Boolean> outputSignal = current.module().process(current.inputName(), current.signal());
            outputSignal.ifPresent(b -> currentModules.addAll(current.module().destinationModules().stream().map(key -> modulesMap.getOrDefault(key, new UntypedModule(key))).map(module -> new ModuleState(module, current.module().name(), b)).toList()));
        }
        return new Pulses(lowPulses, highPulses);
    }

    public static Module parseModule(String line) {
        final Matcher moduleMatcher = Utils.getMatcher(MODULE_PATTERN, line);
        final String type = moduleMatcher.group(1);
        final String name = moduleMatcher.group(2);
        final List<String> destinationModules = Arrays.stream(moduleMatcher.group(3).split(",")).map(String::trim).toList();

        return AbstractModule.createNewModule(type, name, destinationModules);
    }

    public interface Module {
        String name();

        List<String> destinationModules();

        void initializeWithInputs(List<String> inputNames);

        /**
         * Processes the input.
         *
         * @param name   the input name
         * @param signal the input signal
         * @return the output signal
         */
        Optional<Boolean> process(String name, boolean signal);
    }

    public record ModuleState(Module module, String inputName, boolean signal) {
    }

    public record Pulses(int low, int high) {
    }

    public static class FlipFlopModule extends AbstractModule {

        private static final String PREFIX = "%";
        private boolean state;

        public FlipFlopModule(String name, List<String> destinationModules) {
            super(name, destinationModules);
            this.state = false;
        }

        @Override
        public Optional<Boolean> process(String name, boolean signal) {
            if (!signal) {
                this.state = !this.state;
                return Optional.of(this.state);
            }
            return Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            return super.equals(other);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "FlipFlopModule{" + "name='" + name() + '\'' + ", destinationModules=" + destinationModules() + ", state=" + state + '}';
        }
    }

    public static class ConjunctionModule extends AbstractModule {

        private static final String PREFIX = "&";
        private final Map<String, Boolean> inputs;

        public ConjunctionModule(String name, List<String> destinationModules) {
            super(name, destinationModules);
            inputs = new HashMap<>();
        }

        @Override
        public void initializeWithInputs(List<String> inputNames) {
            inputNames.forEach(inputName -> this.inputs.put(inputName, false));
        }

        @Override
        public Optional<Boolean> process(String name, boolean signal) {
            this.inputs.put(name, signal);
            return Optional.of(!this.inputs.values().stream().allMatch(b -> b));
        }

        @Override
        public boolean equals(Object other) {
            return super.equals(other);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "ConjunctionModule{" + "name='" + name() + '\'' + ", destinationModules=" + destinationModules() + ", inputs=" + inputs + '}';
        }
    }

    public static class BroadcastModule extends AbstractModule {

        public static final String NAME = "broadcaster";
        private static final String PREFIX = "";

        protected BroadcastModule(String name, List<String> destinationModules) {
            super(name, destinationModules);
        }

        @Override
        public Optional<Boolean> process(String name, boolean signal) {
            return Optional.of(signal);
        }

        @Override
        public boolean equals(Object other) {
            return super.equals(other);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "BroadcastModule{" + "name='" + name() + '\'' + ", destinationModules=" + destinationModules() + '}';
        }
    }

    public static class UntypedModule extends AbstractModule {

        protected UntypedModule(String name) {
            super(name, List.of());
        }

        @Override
        public Optional<Boolean> process(String name, boolean signal) {
            return Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            return super.equals(other);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "UntypedModule{" + "name='" + name() + '\'' + '}';
        }
    }

    public abstract static class AbstractModule implements Module {
        private final String name;
        private final List<String> destinationModules;

        protected AbstractModule(String name, List<String> destinationModules) {
            this.name = name;
            this.destinationModules = destinationModules;
        }

        public static Module createNewModule(String type, String name, List<String> destinationModules) {
            return switch (type) {
                case FlipFlopModule.PREFIX -> new FlipFlopModule(name, destinationModules);
                case ConjunctionModule.PREFIX -> new ConjunctionModule(name, destinationModules);
                case BroadcastModule.PREFIX -> new BroadcastModule(name, destinationModules);
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }

        @Override
        public final String name() {
            return this.name;
        }

        @Override
        public final List<String> destinationModules() {
            return this.destinationModules;
        }

        @Override
        public void initializeWithInputs(List<String> inputNames) {
            // default implementation does nothing
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AbstractModule that = (AbstractModule) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "AbstractModule{" + "name='" + name + '\'' + ", destinationModules=" + destinationModules + '}';
        }
    }
}

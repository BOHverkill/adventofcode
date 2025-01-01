package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.models.Triple;
import org.bohverkill.utils.GraphUtils;
import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Day21 {
    private static final List<NumericKeypadButton> NUMERIC_KEYPAD_VALUES = List.of(NumericKeypadButton.SEVEN, NumericKeypadButton.EIGHT, NumericKeypadButton.NINE, NumericKeypadButton.FOUR, NumericKeypadButton.FIVE, NumericKeypadButton.SIX, NumericKeypadButton.ONE, NumericKeypadButton.TWO, NumericKeypadButton.THREE, NumericKeypadButton.ZERO, NumericKeypadButton.ACTIVATE);
    private static final List<DirectionalKeypadButton> DIRECTIONAL_KEYPAD_VALUES = List.of(DirectionalKeypadButton.UP, DirectionalKeypadButton.ACTIVATE, DirectionalKeypadButton.LEFT, DirectionalKeypadButton.DOWN, DirectionalKeypadButton.RIGHT);
    private static final Map<NumericKeypadButton, Pair<Integer, Integer>> NUMERIC_KEYPAD_BUTTON_POSITION_MAP = Map.ofEntries(Map.entry(NumericKeypadButton.SEVEN, Pair.of(0, 0)), Map.entry(NumericKeypadButton.EIGHT, Pair.of(0, 1)), Map.entry(NumericKeypadButton.NINE, Pair.of(0, 2)), Map.entry(NumericKeypadButton.FOUR, Pair.of(1, 0)), Map.entry(NumericKeypadButton.FIVE, Pair.of(1, 1)), Map.entry(NumericKeypadButton.SIX, Pair.of(1, 2)), Map.entry(NumericKeypadButton.ONE, Pair.of(2, 0)), Map.entry(NumericKeypadButton.TWO, Pair.of(2, 1)), Map.entry(NumericKeypadButton.THREE, Pair.of(2, 2)), Map.entry(NumericKeypadButton.ZERO, Pair.of(3, 1)), Map.entry(NumericKeypadButton.ACTIVATE, Pair.of(3, 2)));
    private static final Map<DirectionalKeypadButton, Pair<Integer, Integer>> DIRECTIONAL_KEYPAD_BUTTON_POSITION_MAP = Map.ofEntries(Map.entry(DirectionalKeypadButton.UP, Pair.of(0, 1)), Map.entry(DirectionalKeypadButton.ACTIVATE, Pair.of(0, 2)), Map.entry(DirectionalKeypadButton.LEFT, Pair.of(1, 0)), Map.entry(DirectionalKeypadButton.DOWN, Pair.of(1, 1)), Map.entry(DirectionalKeypadButton.RIGHT, Pair.of(1, 2)));
    private static final Set<Pair<Integer, Integer>> NUMERIC_KEYPAD_BUTTON_VALID_POSITION_SET = Set.of(Pair.of(0, 0), Pair.of(0, 1), Pair.of(0, 2), Pair.of(1, 0), Pair.of(1, 1), Pair.of(1, 2), Pair.of(2, 0), Pair.of(2, 1), Pair.of(2, 2), Pair.of(3, 1), Pair.of(3, 2));
    private static final Set<Pair<Integer, Integer>> DIRECTIONAL_KEYPAD_BUTTON_VALID_POSITION_SET = Set.of(Pair.of(0, 1), Pair.of(0, 2), Pair.of(1, 0), Pair.of(1, 1), Pair.of(1, 2));
    private static final Map<NumericKeypadButton, Map<NumericKeypadButton, List<List<DirectionalKeypadButton>>>> NUMERIC_KEYPAD_BUTTON_MAP = precalculateNumericKeypadButtonMapMap();
    private static final Map<DirectionalKeypadButton, Map<DirectionalKeypadButton, List<List<DirectionalKeypadButton>>>> DIRECTIONAL_KEYPAD_BUTTON_MAP = precalculateDirectionalKeypadButtonMapMap();

    public static void main(String[] args) {
//        final String path = "/2024/Day21_example";
        final String path = "/2024/Day21_input";
        final List<List<NumericKeypadButton>> input = Utils.getInputLines(path).map(s -> Arrays.stream(s.split("")).map(NumericKeypadButton::fromString).toList()).toList();
        task1(input);
        task2(input);
    }

    private static Map<NumericKeypadButton, Map<NumericKeypadButton, List<List<DirectionalKeypadButton>>>> precalculateNumericKeypadButtonMapMap() {
        final Map<NumericKeypadButton, Map<NumericKeypadButton, List<List<DirectionalKeypadButton>>>> numericKeypadButtonMapMap = new EnumMap<>(NumericKeypadButton.class);
        for (NumericKeypadButton numericKeypadValue1 : NUMERIC_KEYPAD_VALUES) {
            final Map<NumericKeypadButton, List<List<DirectionalKeypadButton>>> numericKeypadButtonListMap = new EnumMap<>(NumericKeypadButton.class);
            for (NumericKeypadButton numericKeypadValue2 : NUMERIC_KEYPAD_VALUES) {
                final List<List<DirectionalKeypadButton>> numericKeypadPaths = findOneKeypadPaths(NUMERIC_KEYPAD_BUTTON_POSITION_MAP.get(numericKeypadValue2), numericKeypadValue1, NUMERIC_KEYPAD_BUTTON_POSITION_MAP, NUMERIC_KEYPAD_BUTTON_VALID_POSITION_SET);
                numericKeypadButtonListMap.put(numericKeypadValue2, numericKeypadPaths.stream().filter(numericKeypadPath -> !numericKeypadPath.isEmpty()).toList());
            }
            numericKeypadButtonMapMap.put(numericKeypadValue1, numericKeypadButtonListMap);
        }
        return numericKeypadButtonMapMap;
    }

    private static Map<DirectionalKeypadButton, Map<DirectionalKeypadButton, List<List<DirectionalKeypadButton>>>> precalculateDirectionalKeypadButtonMapMap() {
        final Map<DirectionalKeypadButton, Map<DirectionalKeypadButton, List<List<DirectionalKeypadButton>>>> directionalKeypadButtonMapMap = new EnumMap<>(DirectionalKeypadButton.class);
        for (DirectionalKeypadButton directionalKeypadValue1 : DIRECTIONAL_KEYPAD_VALUES) {
            final Map<DirectionalKeypadButton, List<List<DirectionalKeypadButton>>> directionalKeypadButtonListMap = new EnumMap<>(DirectionalKeypadButton.class);
            for (DirectionalKeypadButton directionalKeypadValue2 : DIRECTIONAL_KEYPAD_VALUES) {
                final List<List<DirectionalKeypadButton>> directionalKeypadPaths = findOneKeypadPaths(DIRECTIONAL_KEYPAD_BUTTON_POSITION_MAP.get(directionalKeypadValue2), directionalKeypadValue1, DIRECTIONAL_KEYPAD_BUTTON_POSITION_MAP, DIRECTIONAL_KEYPAD_BUTTON_VALID_POSITION_SET);
                directionalKeypadButtonListMap.put(directionalKeypadValue2, directionalKeypadPaths.stream().filter(directionalKeypadPath -> !directionalKeypadPath.isEmpty()).toList());
            }
            directionalKeypadButtonMapMap.put(directionalKeypadValue1, directionalKeypadButtonListMap);
        }
        return directionalKeypadButtonMapMap;
    }

    private static void task1(final List<List<NumericKeypadButton>> input) {
        int sum = 0;

        for (List<NumericKeypadButton> numericKeypadButtons : input) {
            final List<List<DirectionalKeypadButton>> numericKeypadPaths = findNumericKeypadPaths(numericKeypadButtons, NumericKeypadButton.ACTIVATE);
            final List<List<DirectionalKeypadButton>> directionalKeypadPathLevel2List = new ArrayList<>();
            for (List<DirectionalKeypadButton> directionalKeypadButtons : numericKeypadPaths) {
                final List<List<DirectionalKeypadButton>> directionalKeypadPathLevel1 = findDirectionalKeypadPaths(directionalKeypadButtons, directionalKeypadButtons.getLast());
                for (List<DirectionalKeypadButton> keypadButtons : directionalKeypadPathLevel1) {
                    final List<List<DirectionalKeypadButton>> directionalKeypadPathLevel2 = findDirectionalKeypadPaths(keypadButtons, keypadButtons.getLast());
                    directionalKeypadPathLevel2List.addAll(directionalKeypadPathLevel2);
                }
            }
            final List<DirectionalKeypadButton> directionalKeypadPathLevel2 = directionalKeypadPathLevel2List.stream().min(Comparator.comparingInt(List::size)).orElseThrow();
            sum += directionalKeypadPathLevel2.size() * Integer.parseInt(numericKeypadButtons.stream().filter(numericKeypadButton -> numericKeypadButton != NumericKeypadButton.ACTIVATE).map(NumericKeypadButton::toString).collect(Collectors.joining()));
        }
        // could also be solved with task2 and depth 2
        System.out.println("Part 1: " + sum);
    }

    private static void task2(final List<List<NumericKeypadButton>> input) {
        long sum = 0;

        for (List<NumericKeypadButton> numericKeypadButtons : input) {
            final List<List<DirectionalKeypadButton>> numericKeypadPaths = findNumericKeypadPaths(numericKeypadButtons, NumericKeypadButton.ACTIVATE);
            final int minNumericKeypadSize = numericKeypadPaths.stream().mapToInt(List::size).min().orElseThrow();
            List<List<DirectionalKeypadButton>> directionalKeypadPathList = new ArrayList<>(numericKeypadPaths.stream().filter(value -> value.size() == minNumericKeypadSize).toList());
            long minLength = Long.MAX_VALUE;
            for (List<DirectionalKeypadButton> directionalKeypadButtons : directionalKeypadPathList) {
                final long length = solve(directionalKeypadButtons, 25, new HashMap<>());
                if (length < minLength) {
                    minLength = length;
                }
            }
            sum += minLength * Integer.parseInt(numericKeypadButtons.stream().filter(numericKeypadButton -> numericKeypadButton != NumericKeypadButton.ACTIVATE).map(NumericKeypadButton::toString).collect(Collectors.joining()));
        }
        System.out.println("Part 2: " + sum);
    }


    private static long solve(final List<DirectionalKeypadButton> directionalKeypadButtons, final int depth, Map<Pair<List<DirectionalKeypadButton>, Integer>, Long> map) {
        if (depth == 0) {
            return directionalKeypadButtons.size();
        }
        if (map.containsKey(Pair.of(directionalKeypadButtons, depth))) {
            return map.get(Pair.of(directionalKeypadButtons, depth));
        }

        long sum = 0;
        DirectionalKeypadButton last = DirectionalKeypadButton.ACTIVATE;
        for (DirectionalKeypadButton directionalKeypadButton : directionalKeypadButtons) {
            long min = Long.MAX_VALUE;
            for (List<DirectionalKeypadButton> keypadButtons : DIRECTIONAL_KEYPAD_BUTTON_MAP.get(directionalKeypadButton).get(last)) {
                final long solve = solve(keypadButtons, depth - 1, map);
                if (solve < min) {
                    min = solve;
                }
            }
            sum += min;
            last = directionalKeypadButton;
        }
        map.put(Pair.of(directionalKeypadButtons, depth), sum);
        return sum;
    }

    private static List<List<DirectionalKeypadButton>> findNumericKeypadPaths(List<NumericKeypadButton> numericKeypadButtons, NumericKeypadButton startButton) {
        return findKeypadPaths(numericKeypadButtons, startButton, NUMERIC_KEYPAD_BUTTON_MAP);
    }

    private static List<List<DirectionalKeypadButton>> findDirectionalKeypadPaths(List<DirectionalKeypadButton> directionalKeypadButtons, DirectionalKeypadButton startButton) {
        return findKeypadPaths(directionalKeypadButtons, startButton, DIRECTIONAL_KEYPAD_BUTTON_MAP);
    }

    private static <T> List<List<DirectionalKeypadButton>> findKeypadPaths(List<T> keypadButtons, T keypadButton, Map<T, Map<T, List<List<DirectionalKeypadButton>>>> keypadButtonMap) {
        final List<List<DirectionalKeypadButton>> paths = new ArrayList<>();
        Queue<Triple<T, Integer, List<DirectionalKeypadButton>>> queue = new LinkedList<>();
        queue.add(Triple.of(keypadButton, 0, List.of()));
        while (!queue.isEmpty()) {
            final Triple<T, Integer, List<DirectionalKeypadButton>> current = queue.remove();
            if (current.b() == keypadButtons.size()) {
                paths.add(current.c());
            } else {
                final T endButton = keypadButtons.get(current.b());
                final List<List<DirectionalKeypadButton>> oneNumericKeypadPaths = keypadButtonMap.get(endButton).get(current.a());
                for (List<DirectionalKeypadButton> oneNumericKeypadPath : oneNumericKeypadPaths) {
                    final List<DirectionalKeypadButton> c = new ArrayList<>(current.c());
                    c.addAll(oneNumericKeypadPath);
                    queue.add(Triple.of(endButton, current.b() + 1, c));
                }
            }
        }
        return paths;
    }

    private static <T> List<List<DirectionalKeypadButton>> findOneKeypadPaths(final Pair<Integer, Integer> startPosition, final T endButton, Map<T, Pair<Integer, Integer>> keypadButtonPositionMap, Set<Pair<Integer, Integer>> validPositionSet) {
        final List<List<DirectionalKeypadButton>> paths = new ArrayList<>();
        final Pair<Integer, Integer> endPosition = keypadButtonPositionMap.get(endButton);
        final int manhattanDistance = GraphUtils.manhattanDistance(startPosition, endPosition);
        Queue<Pair<Pair<Integer, Integer>, List<DirectionalKeypadButton>>> queue = new LinkedList<>();
        queue.add(Pair.of(startPosition, new ArrayList<>()));
        Set<Pair<Integer, Integer>> visited = new HashSet<>();
        visited.add(startPosition);
        while (!queue.isEmpty()) {
            final Pair<Pair<Integer, Integer>, List<DirectionalKeypadButton>> current = queue.remove();
            final Pair<Integer, Integer> currentPosition = current.a();
            final List<DirectionalKeypadButton> currentPath = current.b();
            if (currentPosition.equals(endPosition)) {
                currentPath.add(DirectionalKeypadButton.ACTIVATE);
                paths.add(currentPath);
            } else if (currentPath.size() < manhattanDistance) {
                visited.add(currentPosition);
                queue.addAll(getNextPositions(current, validPositionSet, visited));
            }
        }
        return paths;
    }

    private static Collection<Pair<Pair<Integer, Integer>, List<DirectionalKeypadButton>>> getNextPositions(Pair<Pair<Integer, Integer>, List<DirectionalKeypadButton>> current, Set<Pair<Integer, Integer>> validPositionSet, Set<Pair<Integer, Integer>> visited) {
        List<Pair<Pair<Integer, Integer>, List<DirectionalKeypadButton>>> list = new ArrayList<>();
        Pair<Integer, Integer> currentPosition = Pair.of(current.a().a(), current.a().b() + 1);
        if (isValid(currentPosition, validPositionSet, visited)) {
            list.add(Pair.of(currentPosition, newAndAdd(current.b(), DirectionalKeypadButton.RIGHT)));
        }
        currentPosition = Pair.of(current.a().a() - 1, current.a().b());
        if (isValid(currentPosition, validPositionSet, visited)) {
            list.add(Pair.of(currentPosition, newAndAdd(current.b(), DirectionalKeypadButton.UP)));
        }
        currentPosition = Pair.of(current.a().a(), current.a().b() - 1);
        if (isValid(currentPosition, validPositionSet, visited)) {
            list.add(Pair.of(currentPosition, newAndAdd(current.b(), DirectionalKeypadButton.LEFT)));
        }
        currentPosition = Pair.of(current.a().a() + 1, current.a().b());
        if (isValid(currentPosition, validPositionSet, visited)) {
            list.add(Pair.of(currentPosition, newAndAdd(current.b(), DirectionalKeypadButton.DOWN)));
        }
        return list;
    }

    private static List<DirectionalKeypadButton> newAndAdd(List<DirectionalKeypadButton> current, DirectionalKeypadButton directionalKeypadButton) {
        final List<DirectionalKeypadButton> directionalKeypadButtons = new ArrayList<>(current);
        directionalKeypadButtons.add(directionalKeypadButton);
        return directionalKeypadButtons;
    }

    private static boolean isValid(Pair<Integer, Integer> current, Set<Pair<Integer, Integer>> validPositionSet, Set<Pair<Integer, Integer>> visited) {
        return validPositionSet.contains(current) && !visited.contains(current);
    }

    private enum NumericKeypadButton {
        SEVEN, EIGHT, NINE, FOUR, FIVE, SIX, ONE, TWO, THREE, ZERO, ACTIVATE;

        private static NumericKeypadButton fromString(String numericKeypadButton) {
            return switch (numericKeypadButton) {
                case "7" -> SEVEN;
                case "8" -> EIGHT;
                case "9" -> NINE;
                case "4" -> FOUR;
                case "5" -> FIVE;
                case "6" -> SIX;
                case "1" -> ONE;
                case "2" -> TWO;
                case "3" -> THREE;
                case "0" -> ZERO;
                case "A" -> ACTIVATE;
                default -> throw new IllegalStateException("Unexpected numericKeypadButton: " + numericKeypadButton);
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case SEVEN -> "7";
                case EIGHT -> "8";
                case NINE -> "9";
                case FOUR -> "4";
                case FIVE -> "5";
                case SIX -> "6";
                case ONE -> "1";
                case TWO -> "2";
                case THREE -> "3";
                case ZERO -> "0";
                case ACTIVATE -> "A";
            };
        }
    }


    private enum DirectionalKeypadButton {
        UP, ACTIVATE, LEFT, DOWN, RIGHT;

        @Override
        public String toString() {
            return switch (this) {
                case UP -> "^";
                case ACTIVATE -> "A";
                case LEFT -> "<";
                case DOWN -> "v";
                case RIGHT -> ">";
            };
        }
    }
}

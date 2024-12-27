package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20 {
    //     private static final int MIN_CHEAT_TIME = 0;
//    private static final int MIN_CHEAT_TIME = 50;
    private static final int MIN_CHEAT_TIME = 100;

    private static final List<Pair<Integer, Integer>> POSITION_OFFSETS_TO_TRY = List.of(Pair.of(1, 0), Pair.of(-1, 0), Pair.of(0, 1), Pair.of(0, -1));

    public static void main(String[] args) {
//        final String path = "/2024/Day20_example";
        final String path = "/2024/Day20_input";
        final List<List<MapItem>> map = Utils.getInputLines(path).map(line -> StringUtils.parseCharToStringStream(line).map(MapItem::fromString).toList()).toList();
        task1(map);
        task2(map);
    }

    private static void task1(final List<List<MapItem>> map) {
        final int res = performCheats(map, generateCheatPositionOffsetsToTry(2));
        System.out.println("Part 1: " + res);
    }

    private static void task2(final List<List<MapItem>> map) {
        final int res = performCheats(map, generateCheatPositionOffsetsToTry(20));
        System.out.println("Part 2: " + res);
    }

    private static int performCheats(List<List<MapItem>> map, List<Pair<Integer, Integer>> cheatPositionOffsetsToTry) {
        final List<Pair<Integer, Integer>> path = findPath(map);
        // System.out.println("path length = " + (path.size() - 1));

        final Map<Pair<Integer, Integer>, Integer> pathIndexMap = IntStream.range(0, path.size()).boxed().collect(Collectors.toMap(path::get, Function.identity()));
        final Map<Integer, Set<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>> cheats = new HashMap<>();
        for (Pair<Integer, Integer> step : path) {
            putCheatPositions(step, path, pathIndexMap, cheats, cheatPositionOffsetsToTry);
        }

//        cheats.keySet().stream().filter(i -> i >= MIN_CHEAT_TIME).sorted().map(i -> cheats.get(i).size() + ": " + i).forEach(System.out::println);
//        for (int key : cheats.keySet().stream().filter(i -> i >= MIN_CHEAT_TIME).sorted().toList()) {
//            System.out.println(cheats.get(key).size() + ": " + key);
//            for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair : cheats.get(key)) {
//                printCheatMap(map, pair);
//            }
//        }

        return cheats.entrySet().stream().filter(integerSetEntry -> integerSetEntry.getKey() >= MIN_CHEAT_TIME).mapToInt(integerSetEntry -> integerSetEntry.getValue().size()).sum();
    }

    private static List<Pair<Integer, Integer>> findPath(final List<List<MapItem>> map) {
        Pair<Integer, Integer> currentPosition = findFirstTilePosition(map, MapItem.START);
        final Set<Pair<Integer, Integer>> visited = new HashSet<>();
        visited.add(currentPosition);
        final List<Pair<Integer, Integer>> path = new ArrayList<>();
        path.add(currentPosition);
        while (!map.get(currentPosition.a()).get(currentPosition.b()).equals(MapItem.END)) {
//            printMap(map, visited);
            currentPosition = getNextPosition(currentPosition, map, visited);
            visited.add(currentPosition);
            path.add(currentPosition);
        }
        return path;
    }

    private static Pair<Integer, Integer> getNextPosition(final Pair<Integer, Integer> currentPosition, final List<List<MapItem>> map, final Set<Pair<Integer, Integer>> visited) {
        for (Pair<Integer, Integer> positionOffset : POSITION_OFFSETS_TO_TRY) {
            final Pair<Integer, Integer> positionToTry = Pair.of(currentPosition.a() + positionOffset.a(), currentPosition.b() + positionOffset.b());
            if (isValid(positionToTry, map, visited)) {
                return positionToTry;
            }
        }
        throw new IllegalStateException("No valid next position found");
    }

    private static void putCheatPositions(final Pair<Integer, Integer> startPosition, List<Pair<Integer, Integer>> path, final Map<Pair<Integer, Integer>, Integer> pathIndexMap, Map<Integer, Set<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>> cheats, List<Pair<Integer, Integer>> cheatPositionOffsetsToTry) {
        final int startPositionIndex = pathIndexMap.get(startPosition);
        for (Pair<Integer, Integer> positionOffset : cheatPositionOffsetsToTry) {
            final Pair<Integer, Integer> positionToTry = Pair.of(startPosition.a() + positionOffset.a(), startPosition.b() + positionOffset.b());
            if (pathIndexMap.containsKey(positionToTry)) {
                final int endPositionIndex = pathIndexMap.get(positionToTry);
                final Pair<Integer, Integer> endPosition = path.get(endPositionIndex);
                final int length = (endPositionIndex - startPositionIndex) - (Math.abs(positionOffset.a()) + Math.abs(positionOffset.b()));
                if (length > 0) {
                    cheats.computeIfAbsent(length, _ -> new HashSet<>()).add(Pair.of(startPosition, endPosition));
                }
            }
        }
    }

    private static List<Pair<Integer, Integer>> generateCheatPositionOffsetsToTry(final int maxCheatTime) {
        final List<Pair<Integer, Integer>> cheatPositionOffsetsToTry = new ArrayList<>();
        for (int i = -maxCheatTime; i <= maxCheatTime; i++) {
            for (int j = -maxCheatTime; j <= maxCheatTime; j++) {
                final int abs = Math.abs(i) + Math.abs(j);
                if (abs > 0 && abs <= maxCheatTime) {
                    cheatPositionOffsetsToTry.add(Pair.of(i, j));
                }
            }
        }
        return cheatPositionOffsetsToTry;
    }

    private static boolean isValid(final Pair<Integer, Integer> position, final List<List<MapItem>> map, final Set<Pair<Integer, Integer>> visited) {
        return map.get(position.a()).get(position.b()) != MapItem.WALL && !visited.contains(position);
    }

    private static Pair<Integer, Integer> findFirstTilePosition(final List<List<MapItem>> map, final MapItem toFind) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == toFind) {
                    return Pair.of(i, j);
                }
            }
        }
        throw new IllegalStateException(toFind + " not found");
    }

    private static void printMap(final List<List<MapItem>> map) {
        for (List<MapItem> mapItems : map) {
            mapItems.forEach(System.out::print);
            System.out.println();
        }
    }

    private static void printMap(final List<List<MapItem>> map, final Pair<Integer, Integer> currentPosition) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (currentPosition.a() == i && currentPosition.b() == j) {
                    System.out.print("@");
                } else {
                    System.out.print(map.get(i).get(j));
                }
            }
            System.out.println();
        }
    }

    private static void printMap(List<List<MapItem>> map, Set<Pair<Integer, Integer>> positions) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (positions.contains(Pair.of(i, j))) {
                    System.out.print("$");
                } else {
                    System.out.print(map.get(i).get(j));
                }
            }
            System.out.println();
        }
    }

    private static void printCheatMap(List<List<MapItem>> map, Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> position) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (position.a().equals(Pair.of(i, j))) {
                    System.out.print("1");
                } else if (position.b().equals(Pair.of(i, j))) {
                    System.out.print("2");
                } else {
                    System.out.print(map.get(i).get(j));
                }
            }
            System.out.println();
        }
    }

    private enum MapItem {
        TRACK, START, END, WALL;

        private static MapItem fromString(String mapItem) {
            return switch (mapItem) {
                case "." -> TRACK;
                case "S" -> START;
                case "E" -> END;
                case "#" -> WALL;
                default -> throw new IllegalStateException("Unexpected mapItem: " + mapItem);
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case TRACK -> ".";
                case START -> "S";
                case END -> "E";
                case WALL -> "#";
            };
        }
    }

    public enum MoveDirection {
        UP, DOWN, LEFT, RIGHT
    }
}

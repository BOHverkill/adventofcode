package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.utils.CollectionUtils;
import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.IntStream;

public class Day15 {
    public static void main(String[] args) {
//        final String path = "/2024/Day15_example";
        final String path = "/2024/Day15_input";
        final Pair<List<List<MapItem>>, List<Move>> input = parse(Utils.getInput(path));
        task1(input);
        task2(input);
    }

    private static Pair<List<List<MapItem>>, List<Move>> parse(final String input) {
        final String[] split = input.split("\n\n");
        final List<List<MapItem>> map = Arrays.stream(split[0].split("\n")).map(s -> IntStream.range(0, s.length()).mapToObj(j -> MapItem.fromChar(s.charAt(j))).toList()).toList();
        final List<Move> moves = StringUtils.parseCharToStringStream(split[1].replace("\n", "")).map(Move::fromString).toList();
        return Pair.of(map, moves);
    }

    private static <T> Pair<Integer, Integer> findFirstItemPosition(final List<List<T>> map, T item) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == item) {
                    return Pair.of(i, j);
                }
            }
        }
        throw new IllegalStateException("Robot not found");
    }

    private static void task1(final Pair<List<List<MapItem>>, List<Move>> input) {
        final List<List<MapItem>> map = CollectionUtils.copyList(input.a());
//        printMap(map);
        Pair<Integer, Integer> currentRobotPosition = findFirstItemPosition(map, MapItem.ROBOT);
        for (Move move : input.b()) {
            Pair<Integer, Integer> nextPosition = nextPosition(currentRobotPosition, move);
            final MapItem nextItem = map.get(nextPosition.a()).get(nextPosition.b());
            if (nextItem == MapItem.EMPTY) {
                map.get(currentRobotPosition.a()).set(currentRobotPosition.b(), MapItem.EMPTY);
                map.get(nextPosition.a()).set(nextPosition.b(), MapItem.ROBOT);
                currentRobotPosition = nextPosition;
            } else if (nextItem == MapItem.BOX) {
                Pair<Integer, Integer> freeSpot = nextPosition;
                while (true) {
                    Pair<Integer, Integer> nextFreeSpot = nextPosition(freeSpot, move);
                    final MapItem nextSpot = map.get(nextFreeSpot.a()).get(nextFreeSpot.b());
                    if (nextSpot == MapItem.EMPTY) {
                        freeSpot = nextFreeSpot;
                        break;
                    } else if (nextSpot == MapItem.WALL) {
                        freeSpot = null;
                        break;
                    }

                    freeSpot = nextFreeSpot;
                }

                if (freeSpot != null) {
                    Pair<Integer, Integer> previousFreeSpot = freeSpot;
                    while (!previousFreeSpot.equals(nextPosition)) {
                        Pair<Integer, Integer> previousSpot = previousPosition(previousFreeSpot, move);
                        map.get(previousFreeSpot.a()).set(previousFreeSpot.b(), MapItem.BOX);
                        map.get(previousSpot.a()).set(previousSpot.b(), MapItem.EMPTY);
                        previousFreeSpot = previousSpot;
                    }

                    map.get(currentRobotPosition.a()).set(currentRobotPosition.b(), MapItem.EMPTY);
                    map.get(nextPosition.a()).set(nextPosition.b(), MapItem.ROBOT);
                    currentRobotPosition = nextPosition;
                }
            }
//            System.out.println(move);
//            printMap(map);
        }

        int sum = getGPSSum(map, MapItem.BOX);

        System.out.println("Part 1: " + sum);
    }

    private static void task2(final Pair<List<List<MapItem>>, List<Move>> input) {
        final List<List<ExpandedMapItem>> map = expand(CollectionUtils.copyList(input.a()));
//        printExpandedMap(map);
        Pair<Integer, Integer> currentRobotPosition = findFirstItemPosition(map, ExpandedMapItem.ROBOT);
        for (Move move : input.b()) {
            Pair<Integer, Integer> nextPosition = nextPosition(currentRobotPosition, move);
            final ExpandedMapItem nextItem = map.get(nextPosition.a()).get(nextPosition.b());
            if (nextItem == ExpandedMapItem.EMPTY) {
                map.get(currentRobotPosition.a()).set(currentRobotPosition.b(), ExpandedMapItem.EMPTY);
                map.get(nextPosition.a()).set(nextPosition.b(), ExpandedMapItem.ROBOT);
                currentRobotPosition = nextPosition;
            } else if (nextItem == ExpandedMapItem.WIDE_BOX_LEFT || nextItem == ExpandedMapItem.WIDE_BOX_RIGHT) {
                if (move == Move.LEFT || move == Move.RIGHT) {
                    Pair<Integer, Integer> freeSpot = nextPosition;
                    while (true) {
                        Pair<Integer, Integer> nextFreeSpot = nextPosition(freeSpot, move);
                        final ExpandedMapItem nextSpot = map.get(nextFreeSpot.a()).get(nextFreeSpot.b());
                        if (nextSpot == ExpandedMapItem.EMPTY) {
                            freeSpot = nextFreeSpot;
                            break;
                        } else if (nextSpot == ExpandedMapItem.WALL) {
                            freeSpot = null;
                            break;
                        }

                        freeSpot = nextFreeSpot;
                    }

                    if (freeSpot != null) {
                        Pair<Integer, Integer> previousFreeSpot = freeSpot;
                        while (!previousFreeSpot.equals(nextPosition)) {
                            Pair<Integer, Integer> previousSpot = previousPosition(previousFreeSpot, move);
                            map.get(previousFreeSpot.a()).set(previousFreeSpot.b(), map.get(previousSpot.a()).get(previousSpot.b()));
                            map.get(previousSpot.a()).set(previousSpot.b(), ExpandedMapItem.EMPTY);
                            previousFreeSpot = previousSpot;
                        }

                        map.get(currentRobotPosition.a()).set(currentRobotPosition.b(), ExpandedMapItem.EMPTY);
                        map.get(nextPosition.a()).set(nextPosition.b(), ExpandedMapItem.ROBOT);
                        currentRobotPosition = nextPosition;
                    }
                } else {
                    int add;
                    if (move == Move.UP) {
                        add = -1;
                    } else {
                        add = 1;
                    }

                    List<Set<Pair<Integer, Integer>>> toMove = new ArrayList<>();
                    toMove.add(addBox(new HashSet<>(), nextPosition, nextItem));
                    while (true) {
                        Set<Pair<Integer, Integer>> nextLine = new HashSet<>();
                        for (Pair<Integer, Integer> p : toMove.getLast()) {
                            final Pair<Integer, Integer> currentPosition = Pair.of(p.a() + add, p.b());
                            addBox(nextLine, currentPosition, map.get(currentPosition.a()).get(currentPosition.b()));
                        }
                        if (nextLine.isEmpty()) {
                            break;
                        }
                        toMove.add(nextLine);
                    }
                    boolean canMove = true;
                    for (Set<Pair<Integer, Integer>> pairs : toMove) {
                        for (Pair<Integer, Integer> p : pairs) {
                            final ExpandedMapItem expandedMapItem = map.get(p.a() + add).get(p.b());
                            if (expandedMapItem == ExpandedMapItem.WALL) {
                                canMove = false;
                                break;
                            }
                        }
                        if (!canMove) {
                            break;
                        }
                    }
                    if (canMove) {
                        for (Set<Pair<Integer, Integer>> line : toMove.reversed()) {
                            for (Pair<Integer, Integer> p : line) {
                                map.get(p.a() + add).set(p.b(), map.get(p.a()).get(p.b()));
                                map.get(p.a()).set(p.b(), ExpandedMapItem.EMPTY);
                            }
                        }

                        map.get(currentRobotPosition.a()).set(currentRobotPosition.b(), ExpandedMapItem.EMPTY);
                        map.get(nextPosition.a()).set(nextPosition.b(), ExpandedMapItem.ROBOT);
                        currentRobotPosition = nextPosition;
                    }
                }
            }
//            System.out.println(move);
//            printExpandedMap(map);
        }
//        printExpandedMap(map);

        int sum = getGPSSum(map, ExpandedMapItem.WIDE_BOX_LEFT);

        System.out.println("Part 2: " + sum);
    }

    private static Set<Pair<Integer, Integer>> addBox(Set<Pair<Integer, Integer>> line, Pair<Integer, Integer> currentPosition, ExpandedMapItem currentItem) {
        if (currentItem == ExpandedMapItem.WIDE_BOX_LEFT) {
            line.add(currentPosition);
            line.add(Pair.of(currentPosition.a(), currentPosition.b() + 1));
        } else if (currentItem == ExpandedMapItem.WIDE_BOX_RIGHT) {
            line.add(currentPosition);
            line.add(Pair.of(currentPosition.a(), currentPosition.b() - 1));
        }
        return line;
    }

    private static List<List<ExpandedMapItem>> expand(List<List<MapItem>> lists) {
        List<List<ExpandedMapItem>> result = new ArrayList<>(lists.size());
        for (List<MapItem> list : lists) {
            List<ExpandedMapItem> mapItems = new ArrayList<>(list.size() * 2);
            for (MapItem mapItem : list) {
                mapItems.addAll(ExpandedMapItem.fromMapItem(mapItem));
            }
            result.add(mapItems);
        }
        return result;
    }

    private static Pair<Integer, Integer> nextPosition(Pair<Integer, Integer> current, Move move) {
        return switch (move) {
            case UP -> Pair.of(current.a() - 1, current.b());
            case DOWN -> Pair.of(current.a() + 1, current.b());
            case LEFT -> Pair.of(current.a(), current.b() - 1);
            case RIGHT -> Pair.of(current.a(), current.b() + 1);
        };
    }

    private static Pair<Integer, Integer> previousPosition(Pair<Integer, Integer> current, Move move) {
        return switch (move) {
            case UP -> Pair.of(current.a() + 1, current.b());
            case DOWN -> Pair.of(current.a() - 1, current.b());
            case LEFT -> Pair.of(current.a(), current.b() + 1);
            case RIGHT -> Pair.of(current.a(), current.b() - 1);
        };
    }

    private static <T> int getGPSSum(List<List<T>> map, T item) {
        int sum = 0;
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == item) {
                    sum += 100 * i + j;
                }
            }
        }
        return sum;
    }

    private static void printMap(final List<List<MapItem>> map) {
        for (List<MapItem> mapItems : map) {
            for (MapItem mapItem : mapItems) {
                System.out.print(mapItem.toChar());
            }
            System.out.println();
        }
    }

    private static void printExpandedMap(final List<List<ExpandedMapItem>> map) {
        for (List<ExpandedMapItem> mapItems : map) {
            for (ExpandedMapItem mapItem : mapItems) {
                System.out.print(mapItem.toChar());
            }
            System.out.println();
        }
    }

    private enum MapItem {
        ROBOT, BOX, WALL, EMPTY;

        private static MapItem fromChar(char item) {
            return switch (item) {
                case '@' -> ROBOT;
                case 'O' -> BOX;
                case '#' -> WALL;
                case '.' -> EMPTY;
                default -> throw new IllegalStateException("Unexpected item: " + item);
            };
        }

        private char toChar() {
            return switch (this) {
                case ROBOT -> '@';
                case BOX -> 'O';
                case WALL -> '#';
                case EMPTY -> '.';
            };
        }
    }

    private enum ExpandedMapItem {
        ROBOT, WIDE_BOX_LEFT, WIDE_BOX_RIGHT, WALL, EMPTY;

        private static List<ExpandedMapItem> fromMapItem(MapItem mapItem) {
            return switch (mapItem) {
                case ROBOT -> List.of(ROBOT, EMPTY);
                case BOX -> List.of(WIDE_BOX_LEFT, WIDE_BOX_RIGHT);
                case WALL -> List.of(WALL, WALL);
                case EMPTY -> List.of(EMPTY, EMPTY);
            };
        }

        private char toChar() {
            return switch (this) {
                case ROBOT -> '@';
                case WIDE_BOX_LEFT -> '[';
                case WIDE_BOX_RIGHT -> ']';
                case WALL -> '#';
                case EMPTY -> '.';
            };
        }
    }

    private enum Move {
        UP, DOWN, LEFT, RIGHT;

        private static Move fromString(String move) {
            return switch (move) {
                case "^" -> UP;
                case "v" -> DOWN;
                case "<" -> LEFT;
                case ">" -> RIGHT;
                default -> throw new IllegalStateException("Unexpected move: " + move);
            };
        }
    }
}

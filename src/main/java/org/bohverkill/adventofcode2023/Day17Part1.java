package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.*;

public class Day17Part1 {
    public static void main(String[] args) {
//        String input = Utils.getInput("/2023/Day17_example");
        String input = Utils.getInput("/2023/Day17_input");
        final String[] map = Utils.getLines(input);
        final int[][] heatLossMap = Arrays.stream(map).map(string -> Arrays.stream(string.split("")).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
        final Node start = new Node(0, 0, null, heatLossMap[0][0]);
        final Node goal = new Node(heatLossMap.length - 1, heatLossMap[0].length - 1, null, heatLossMap[heatLossMap.length - 1][heatLossMap[0].length - 1]);
        final List<Node> nodes = aStar(start, goal, heatLossMap);
//        print(nodes, heatLossMap);
        int count = nodes.stream().filter(node -> !node.equals(start)).mapToInt(node -> node.heatLoss).sum();
        System.out.println("Day 17 Part 1: " + count);
    }

    public static void print(List<Node> nodes, int[][] heatLossMap) {
        for (int i = 0; i < heatLossMap.length; i++) {
            for (int j = 0; j < heatLossMap[i].length; j++) {
                final int finalI = i;
                final int finalJ = j;
                final Optional<Node> first = nodes.stream().filter(node -> node.row == finalI && node.column == finalJ).findFirst();
                System.out.print(first.map(node -> {
                    if (node.direction() == null) {
                        return String.valueOf(heatLossMap[finalI][finalJ]);
                    }
                    final String s = switch (node.direction()) {
                        case RIGHT -> ">";
                        case LEFT -> "<";
                        case UP -> "^";
                        case DOWN -> "v";
                    };
                    return "\033[0;1m" + s + "\033[0;0m";
                }).orElseGet(() -> String.valueOf(heatLossMap[finalI][finalJ])));
            }
            System.out.println();
        }
    }

    public static List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        List<Node> totalPath = new LinkedList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.addFirst(current);
        }
        return totalPath;
    }

    public static List<Node> aStar(Node start, Node goal, int[][] heatLossMap) {
        Queue<Node> openSet = new PriorityQueue<>();
        start.setFScore(start.h(goal));
        openSet.add(start);

        Map<Node, Node> cameFrom = new HashMap<>();

        Map<Node, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.remove();

//            print(reconstructPath(cameFrom, current), heatLossMap);
//            System.out.println(current + " " + gScore.get(current));

            if (current.column() == goal.column() && current.row() == goal.row()) {
                return reconstructPath(cameFrom, current);
            }

            for (Node neighbor : current.neighbors(heatLossMap)) {
                int tentativeGScore = gScore.get(current) + neighbor.d();
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    boolean changed = neighbor.setFScore(tentativeGScore + neighbor.h(goal));
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    } else {
                        if (changed) {
                            // needs to be done when the score is recalculated
                            openSet.remove(neighbor);
                            openSet.add(neighbor);
                        }
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    public enum Direction {
        RIGHT, LEFT, DOWN, UP
    }

    public static final class Node implements Comparable<Node> {
        private final int row;
        private final int column;
        private final Direction direction;
        private final int countSameDirection;
        private final int heatLoss;
        private int fScore = Integer.MAX_VALUE;

        public Node(int row, int column, Direction direction, int heatLoss) {
            this.row = row;
            this.column = column;
            this.direction = direction;
            this.countSameDirection = 0;
            this.heatLoss = heatLoss;
        }

        public Node(int row, int column, Direction direction, int countSameDirection, int heatLoss) {
            this.row = row;
            this.column = column;
            this.direction = direction;
            this.countSameDirection = countSameDirection;
            this.heatLoss = heatLoss;
        }

        private static void buildAndAdd(int row, int column, Direction direction, int countSameDirection, int[][] heatLossMap, Set<Node> neighbors) {
            if (!isNotValid(row, column, heatLossMap.length, heatLossMap[0].length) && countSameDirection < 3) {
                neighbors.add(new Node(row, column, direction, countSameDirection, heatLossMap[row][column]));
            }
        }

        private static boolean isNotValid(int row, int column, int rowSize, int columnSize) {
            return row < 0 || row >= rowSize || column < 0 || column >= columnSize;
        }

        private static int manhattanDistance(Node n0, Node n1) {
            return Math.abs(n1.row() - n0.row()) + Math.abs(n1.column() - n0.column());
        }

        public Set<Node> neighbors(int[][] heatLossMap) {
            Set<Node> neighbors = new HashSet<>();
            if (direction() == null) {
                buildAndAdd(row(), column() + 1, Direction.RIGHT, 0, heatLossMap, neighbors);
                buildAndAdd(row(), column() - 1, Direction.LEFT, 0, heatLossMap, neighbors);
                buildAndAdd(row() + 1, column(), Direction.DOWN, 0, heatLossMap, neighbors);
                buildAndAdd(row() - 1, column(), Direction.UP, 0, heatLossMap, neighbors);
            } else {
                switch (direction()) {
                    case RIGHT -> {
                        buildAndAdd(row() - 1, column(), Direction.UP, 0, heatLossMap, neighbors); // left
                        buildAndAdd(row(), column() + 1, Direction.RIGHT, countSameDirection() + 1, heatLossMap, neighbors); // straight
                        buildAndAdd(row() + 1, column(), Direction.DOWN, 0, heatLossMap, neighbors); // right
                    }
                    case LEFT -> {
                        buildAndAdd(row() + 1, column(), Direction.DOWN, 0, heatLossMap, neighbors); // left
                        buildAndAdd(row(), column() - 1, Direction.LEFT, countSameDirection() + 1, heatLossMap, neighbors); // straight
                        buildAndAdd(row() - 1, column(), Direction.UP, 0, heatLossMap, neighbors); // right
                    }
                    case DOWN -> {
                        buildAndAdd(row(), column() + 1, Direction.RIGHT, 0, heatLossMap, neighbors); // left
                        buildAndAdd(row() + 1, column(), Direction.DOWN, countSameDirection() + 1, heatLossMap, neighbors); // straight
                        buildAndAdd(row(), column() - 1, Direction.LEFT, 0, heatLossMap, neighbors); // right
                    }
                    case UP -> {
                        buildAndAdd(row(), column() - 1, Direction.LEFT, 0, heatLossMap, neighbors); // left
                        buildAndAdd(row() - 1, column(), Direction.UP, countSameDirection() + 1, heatLossMap, neighbors); // straight
                        buildAndAdd(row(), column() + 1, Direction.RIGHT, 0, heatLossMap, neighbors); // right
                    }
                }
            }
            return neighbors;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.fScore, o.fScore);
        }

        public int row() {
            return this.row;
        }

        public int column() {
            return this.column;
        }

        public Direction direction() {
            return this.direction;
        }

        public int countSameDirection() {
            return this.countSameDirection;
        }

        public int d() {
            return this.heatLoss;
        }

        public int h(Node goal) {
            return manhattanDistance(this, goal);
        }

        public boolean setFScore(int fScore) {
            if (this.fScore != fScore) {
                this.fScore = fScore;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Node) obj;
            // this is important, we add more than only grid positions to the graph, also count direction and each step count as own node to get all possibilities and find optimum
            return this.row == that.row && this.column == that.column && this.heatLoss == that.heatLoss && this.direction == that.direction && this.countSameDirection == that.countSameDirection;
        }

        @Override
        public int hashCode() {
            // reflect equals method, needed for the hashset/map
            return Objects.hash(row, column, heatLoss, direction, countSameDirection);
        }

        @Override
        public String toString() {
            return "Node[" + "row=" + row + ", " + "column=" + column + ", " + "direction=" + direction + ", " + "countSameDirection=" + countSameDirection + ", " + "heatLoss=" + heatLoss + ", " + "fScore=" + fScore + ']';
        }
    }
}

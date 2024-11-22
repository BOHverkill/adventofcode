package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23Part2 {
    public static void main(String[] args) {
//        final String input = Utils.getInput("/2023/Day23_example");
//        final String input = Utils.getInput("/2023/Day23_example");
//        final String input = Utils.getInput("/2023/Day23_example");
        final String input = Utils.getInput("/2023/Day23_input");

        final String[] map = Utils.getLines(input);

        final Tile start = new Tile(0, 1, Direction.DOWN);
        final Tile goal = new Tile(map.length - 1, map[map.length - 1].length() - 2, Direction.DOWN);

        final Map.Entry<Node, Node> nodeNodeEntry = reduceGraph(map, start, goal);
        final Node root = nodeNodeEntry.getKey();
        final Node goalNode = nodeNodeEntry.getValue();
//        printNode(root);

        final Map<Path, Set<Path>> pathMap = buildPathMap(root);
        final Map<Edge, Integer> weightMap = buildWeightMap(root);
        final int pathLength = findPath(root, goalNode, pathMap, weightMap);
        System.out.println("Day 23 Part 2: " + pathLength);
    }

    public static int findPath(Node root, Node goalNode, Map<Path, Set<Path>> pathMap, Map<Edge, Integer> weightMap) {
        Path goal = goalNode.path();

        Queue<Path> queue = new ArrayDeque<>();
        queue.add(root.path());

        int goalLength = -1;
        while (!queue.isEmpty()) {
            final Path current = queue.remove();
            final Set<Path> currentPath = pathSet(current);
            final int currentPathLength = pathLength(current, weightMap);

            if (current.equals(goal) && currentPathLength > goalLength) {
                goalLength = currentPathLength;
            }

            for (Path neighbor : pathMap.getOrDefault(current, Collections.emptySet())) {
                neighbor = new Path(neighbor);
                if (!currentPath.contains(neighbor)) {
                    neighbor.previous(current);
                    queue.add(neighbor);
                }
            }
        }
        return goalLength;
    }

    public static Set<Path> pathSet(Path current) {
        Set<Path> path = new HashSet<>();
        while (current != null) {
            path.add(current);
            current = current.previous();
        }
        return path;
    }

    public static int pathLength(Path current, Map<Edge, Integer> weightMap) {
        int length = 0;
        while (current != null) {
            length += weightMap.getOrDefault(new Edge(current, current.previous()), 0);
            current = current.previous();
        }
        return length;
    }

    public static Map<Path, Set<Path>> buildPathMap(Node root) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        Map<Path, Set<Path>> visited = new HashMap<>();
        while (!queue.isEmpty()) {
            final Node current = queue.remove();
            if (!visited.containsKey(current.path())) {
                visited.put(current.path(), current.nodes().keySet().stream().map(Node::path).collect(Collectors.toSet()));
                queue.addAll(current.nodes().keySet());
            }
        }
        return visited;
    }

    public static Map<Edge, Integer> buildWeightMap(Node root) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        Set<Node> visited = new HashSet<>();
        Map<Edge, Integer> weights = new HashMap<>();
        while (!queue.isEmpty()) {
            final Node current = queue.remove();
            if (!visited.contains(current)) {
                visited.add(current);
                current.nodes().forEach((key, value) -> {
                    weights.put(new Edge(current.path(), key.path()), value);
                    weights.put(new Edge(key.path(), current.path()), value);
                });
                queue.addAll(current.nodes().keySet());
            }
        }
        return weights;
    }

    public static Map.Entry<Node, Node> reduceGraph(String[] map, Tile start, Tile goal) {
        Queue<Entry> queue = new ArrayDeque<>();
        Set<Node> nodes = new HashSet<>();

        Node root = new Node(start.row(), start.column());
        nodes.add(root);
        Node goalNode = new Node(goal.row(), goal.column());

        queue.add(new Entry(start, root));
        while (!queue.isEmpty()) {
            final Entry entry = queue.remove();
            Tile current = entry.tile();
            Node parent = entry.node();

            List<Tile> neighbors = current.neighbors(map);

            int weight = current.equals(start) ? 0 : 1;
            while (neighbors.size() == 1) {
                current = neighbors.getFirst();
                neighbors = current.neighbors(map);
                weight++;
            }

            if (current.equals(goal)) {
                nodes.add(goalNode);
                parent.nodes().put(goalNode, weight);
                goalNode.nodes().put(parent, weight);
            }

            Node node = new Node(current.row(), current.column());
            if (nodes.contains(node)) {
                final Node finalNode = node;
                node = nodes.stream().filter(node1 -> node1.equals(finalNode)).findFirst().orElseThrow();
            } else {
                nodes.add(node);
            }

            if (!node.nodes().containsKey(parent)) {
                parent.nodes().put(node, weight);
                node.nodes().put(parent, weight);
                for (Tile neighbor : neighbors) {
                    queue.add(new Entry(neighbor, node));
                }
            }
        }
        return Map.entry(root, goalNode);
    }

    public static void print(List<Tile> path, String[] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length(); j++) {
                final int finalI = i;
                final int finalJ = j;
                final Optional<Tile> first = path.stream().filter(Objects::nonNull).filter(tile -> tile.row == finalI && tile.column == finalJ).findFirst();
                System.out.print(first.map(tile -> 'O').orElseGet(() -> map[finalI].charAt(finalJ)));
            }
            System.out.println();
        }
    }

    public static void printNode(Node root) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        Set<Node> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            final Node current = queue.remove();
            if (!visited.contains(current)) {
                visited.add(current);
                System.out.println(current + ": " + current.nodes());
                queue.addAll(current.nodes().keySet());
            }
        }
    }

    public enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    public record Edge(Path path1, Path path2) {
    }

    public static final class Path {
        private final int row;
        private final int column;
        private Path previous;

        public Path(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public Path(Path path) {
            this.row = path.row;
            this.column = path.column;
        }

        public void previous(Path previous) {
            this.previous = previous;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Path path = (Path) o;
            return row == path.row && column == path.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }

        public int row() {
            return row;
        }

        public int column() {
            return column;
        }

        public Path previous() {
            return previous;
        }

        @Override
        public String toString() {
            return "Path[" + "row=" + row + ", " + "column=" + column + ']';
        }

    }

    public record Entry(Tile tile, Node node) {

    }

    public record Node(int row, int column, Map<Node, Integer> nodes) {
        public Node(int row, int column) {
            this(row, column, new HashMap<>());
        }

        public Path path() {
            return new Path(row(), column());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return row == node.row && column == node.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }

        @Override
        public String toString() {
            return "Node{" + "row=" + row + ", column=" + column + '}';
        }
    }

    public record Tile(int row, int column, Direction direction) {

        private static Optional<Tile> build(int row, int column, Direction current, Direction next, String[] map) {
            if (isValid(row, column, map.length, map[0].length()) && isNotBack(current, next) && map[row].charAt(column) != '#') {
                return Optional.of(new Tile(row, column, next));
            } else {
                return Optional.empty();
            }
        }

        private static boolean isNotBack(Direction current, Direction next) {
            return switch (current) {
                case UP -> next != Direction.DOWN;
                case RIGHT -> next != Direction.LEFT;
                case DOWN -> next != Direction.UP;
                case LEFT -> next != Direction.RIGHT;
            };
        }

        private static boolean isValid(int row, int column, int rowSize, int columnSize) {
            return row >= 0 && row < rowSize && column >= 0 && column < columnSize;
        }

        public List<Tile> neighbors(String[] map) {
            return Stream.of(build(row() - 1, column(), direction(), Direction.UP, map), build(row(), column() + 1, direction(), Direction.RIGHT, map), build(row() + 1, column(), direction(), Direction.DOWN, map), build(row(), column() - 1, direction(), Direction.LEFT, map)).flatMap(Optional::stream).toList();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return row == tile.row && column == tile.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }
}

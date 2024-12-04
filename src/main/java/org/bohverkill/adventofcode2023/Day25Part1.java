package org.bohverkill.adventofcode2023;


import org.bohverkill.utils.StringUtils;
import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Day25Part1 {
    public static void main(String[] args) {
//        final List<String> lines = Utils.getInputLines("/2023/Day25_example").toList();
        final List<String> lines = Utils.getInputLines("/2023/Day25_input").toList();

        final Graph graph = repeatingContraction(lines);
        if (graph.nodes().size() != 2) {
            throw new IllegalStateException("Bug?");
        }
        final int first = StringUtils.splitAfterNChars(graph.nodes().getFirst().name(), 3).length;
        final int second = StringUtils.splitAfterNChars(graph.nodes().get(1).name(), 3).length;
        System.out.println("Day 25 Part 1: " + first * second);
    }

    // Karger's algorithm: https://en.wikipedia.org/wiki/Karger%27s_algorithm
    public static Graph repeatingContraction(List<String> lines) {
        while (true) {
            Map<String, Node> componentMap = new HashMap<>();
            lines.forEach(line -> parseComponent(line, componentMap));
            final Graph graph = contract(buildGraph(componentMap));
            if (graph.edges().size() == 3) {
                return graph;
            } else if (graph.edges().size() < 3) {
                System.out.println(graph);
                printGraph(graph);
                throw new IllegalStateException("Impossible?");
            }
        }
    }

    public static Graph contract(Graph graph) {
        while (graph.nodes().size() > 2) {
            final Edge edge = getRandom(graph.edges()).orElseThrow();
            contractEdge(graph, edge);
        }
        return graph;
    }

    private static void contractEdge(Graph graph, Edge edge) {
        graph.edges().remove(edge);

        final Node u = edge.u();
        graph.nodes().remove(u);
        final Node v = edge.v();
        graph.nodes().remove(v);

        final Node uv = new Node(u.name() + v.name(), new HashSet<>());

        List<Edge> toRemove = new ArrayList<>();
        List<Edge> toAdd = new ArrayList<>();
        for (Edge e : graph.edges()) {
            if ((e.u().equals(u) && e.v().equals(v)) || (e.v().equals(u) && e.u().equals(v))) {
                toRemove.add(e);
            } else if (e.u().equals(u) || e.u().equals(v)) {
                toRemove.add(e);
                uv.nodes().add(e.v());
                toAdd.add(new Edge(uv, e.v()));
            } else if (e.v().equals(u)) {
                toRemove.add(e);
                e.u().nodes().remove(u);
                e.u().nodes().add(uv);
                toAdd.add(new Edge(e.u(), uv));
            } else if (e.v().equals(v)) {
                toRemove.add(e);
                e.u().nodes().remove(v);
                e.u().nodes().add(uv);
                toAdd.add(new Edge(e.u(), uv));
            }
        }
        graph.edges().removeAll(toRemove);
        graph.edges().addAll(toAdd);

        graph.nodes().add(uv);
    }

    // from https://stackoverflow.com/a/40087987
    public static <E> Optional<E> getRandom(Collection<E> e) {
        return e.stream().skip((int) (e.size() * Math.random())).findFirst();
    }

    private static Graph buildGraph(Map<String, Node> componentMap) {
        final List<Node> nodes = new ArrayList<>(componentMap.values());
        final List<Edge> edges = new ArrayList<>();
        for (Node u : nodes) {
            for (Node v : u.nodes()) {
                edges.add(new Edge(u, v));
            }
        }
        return new Graph(nodes, edges);
    }

    public static void parseComponent(String line, Map<String, Node> componentMap) {
        final String[] split = line.split(":");
        if (split.length != 2) {
            throw new IllegalStateException("Malformed line: " + line);
        }
        final String name = split[0].trim();
        Set<Node> connected = Arrays.stream(split[1].trim().split(" ")).map(s -> componentMap.computeIfAbsent(s.trim(), n -> new Node(n, new HashSet<>()))).collect(Collectors.toSet());

        final Node node;
        if (componentMap.containsKey(name)) {
            node = componentMap.get(name);
            node.nodes().addAll(connected);
        } else {
            node = new Node(name, connected);
        }

        // comment this in if you want a fully connected graph
//        connected.forEach(c -> c.nodes().add(node));
        componentMap.put(name, node);
    }

    // https://csacademy.com/app/graph_editor/
    private static void printGraph(Graph graph) {
        for (Node node : graph.nodes()) {
            System.out.println(node.name());
        }

        for (Edge e : graph.edges()) {
            System.out.println(e.u().name() + " " + e.v().name());
        }
    }

    public record Graph(List<Node> nodes, List<Edge> edges) {
    }

    public record Edge(Node u, Node v) {
    }

    public record Node(String name, Set<Node> nodes) {

        @Override
        public String toString() {
            return "Node{" + "name='" + name + '\'' + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(name, node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}

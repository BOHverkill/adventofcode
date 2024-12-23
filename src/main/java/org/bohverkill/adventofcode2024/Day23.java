package org.bohverkill.adventofcode2024;

import org.bohverkill.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Day23 {
    public static void main(String[] args) {
//        final String path = "/2024/Day23_example";
        final String path = "/2024/Day23_input";
        final HashMap<String, Node> nodeMap = new HashMap<>();
        Utils.getInputLines(path).forEach(line -> parse(line, nodeMap));
        task1(new HashMap<>(nodeMap));
        task2(new HashMap<>(nodeMap));
    }

    private static void parse(String line, Map<String, Node> nodeMap) {
        final String[] split = line.split("-");
        final Node node1 = nodeMap.computeIfAbsent(split[0], Node::of);
        final Node node2 = nodeMap.computeIfAbsent(split[1], Node::of);

        node1.neighbors().add(node2);
        node2.neighbors().add(node1);
    }

    private static void task1(final HashMap<String, Node> nodeMap) {
        final Set<Set<Node>> threeSet = findThreeSet(nodeMap);
        final long res = threeSet.stream().filter(set -> set.stream().map(node -> node.name).anyMatch(string -> string.startsWith("t"))).count();
        System.out.println("Part 1: " + res);
    }

    private static void task2(final HashMap<String, Node> nodeMap) {
        final HashSet<Set<Node>> result = new HashSet<>();
        bronKerbosch1(new HashSet<>(), new HashSet<>(nodeMap.values()), new HashSet<>(), result);
        final Set<Node> nodes = result.stream().max(Comparator.comparing(Set::size)).orElseThrow();
        final String password = nodes.stream().map(Node::name).sorted().collect(Collectors.joining(","));
        System.out.println("Part 2: " + password);
    }

    private static Set<Set<Node>> findThreeSet(final HashMap<String, Node> nodeMap) {
        Set<Set<Node>> nodeSets = new HashSet<>();
        for (Node value : nodeMap.values()) {
            for (Node neighbor1 : value.neighbors()) {
                if (!neighbor1.equals(value)) {
                    for (Node neighbor2 : neighbor1.neighbors()) {
                        if (!neighbor2.equals(neighbor1)) {
                            for (Node neighbor3 : neighbor2.neighbors()) {
                                if (neighbor3.equals(value)) {
                                    nodeSets.add(Set.of(value, neighbor1, neighbor2));
                                }
                            }
                        }
                    }
                }
            }
        }
        return nodeSets;
    }


    // Source: https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
    // algorithm BronKerbosch1(R, P, X) is
    //    if P and X are both empty then
    //        report R as a maximal clique
    //    for each vertex v in P do
    //        BronKerbosch1(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
    //        P := P \ {v}
    //        X := X ⋃ {v}
    private static void bronKerbosch1(Set<Node> r, Set<Node> p, Set<Node> x, Set<Set<Node>> result) {
        if (p.isEmpty() && x.isEmpty()) {
            result.add(r);
        } else {
            for (Node v : new HashSet<>(p)) {
                final HashSet<Node> rNew = new HashSet<>(r);
                rNew.add(v);
                final HashSet<Node> pNew = new HashSet<>(p);
                pNew.retainAll(v.neighbors());
                final HashSet<Node> xNew = new HashSet<>(x);
                xNew.retainAll(v.neighbors());
                bronKerbosch1(rNew, pNew, xNew, result);
                p.remove(v);
                x.add(v);
            }
        }
    }

    private record Node(String name, List<Node> neighbors) {
        private static Node of(final String name) {
            return new Node(name, new ArrayList<>());
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(name(), node.name());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name());
        }

        @Override
        public String toString() {
            return "Node{" + "name='" + name + '\'' + ", neighbors=" + neighbors.stream().map(Node::name).collect(Collectors.joining(", ")) + '}';
        }
    }
}

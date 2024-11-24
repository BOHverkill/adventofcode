package org.bohverkill.adventofcode2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Day3 {
    public static void main(String[] args) throws FileNotFoundException {
        final int input = Integer.parseInt(new Scanner(new File(Day3.class.getResource("/2017/Day3_input").getPath())).next());

        task1(input);
        task2(input);
    }

    private static void task1(final int input) {
        int size = ((int) Math.sqrt(input)) + 1;
        int center = (int) (size / 2.0);
        //int nextCorner = (int) Math.pow(size, 2);
        //System.out.println(input);
        //System.out.println(center);
        //System.out.println(size);
        //System.out.println(nextCorner);

        //int diagonal1 = (int) (4 * Math.pow(center, 2) - 2 * center + 1);
        //int diagonal2 = (int) (4 * Math.pow(center - 1, 2) + 2 * (center - 1) + 1);
        //System.out.println(diagonal1 - input);
        //System.out.println(input - diagonal2);

//        int line1 = (int) (4 * Math.pow(center, 2) - 3 * center + 1);
//        int line2 = (int) (4 * Math.pow(center, 2) - center + 1);
//        int line3 = (int) (4 * Math.pow(center, 2) + center + 1);
        int line4 = (int) (4 * Math.pow(center, 2) + 3 * center + 1);

//        System.out.println(line1 - input);
//        System.out.println(line2 - input);
//        System.out.println(line3 - input);
//        System.out.println(line4 - input);

        System.out.println("Part 1: " + (line4 - input + center));
    }

    private static void task2(final int input) {
        Node first = Node.of(1, Direction.RIGHT);
        Node second = Node.of(1, Direction.UP);
        first.neighbors()[4] = second;
        second.neighbors()[3] = first;
        Node current = second;
        while (current.value() <= input) {
            int directionIndex = switch (current.direction()) {
                case RIGHT -> 1;
                case UP -> 3;
                case LEFT -> 6;
                case DOWN -> 4;
            };
            Direction nextDirection; // check if left node is null/empty
            if (current.neighbors()[directionIndex] == null) {
                nextDirection = current.direction().next();
            } else {
                nextDirection = current.direction();
            }
            Node[] nextNeighbors = Node.newNeighbors();
            switch (nextDirection) {
                case RIGHT -> {
                    nextNeighbors[3] = current;
                    nextNeighbors[0] = current.neighbors()[1];
                    nextNeighbors[1] = current.neighbors()[2];
                    if (current.neighbors()[2] != null) {
                        nextNeighbors[2] = current.neighbors()[2].neighbors()[4];
                    }
                }
                case UP -> {
                    nextNeighbors[6] = current;
                    nextNeighbors[5] = current.neighbors()[3];
                    nextNeighbors[3] = current.neighbors()[0];
                    if (current.neighbors()[0] != null) {
                        nextNeighbors[0] = current.neighbors()[0].neighbors()[1];
                    }
                }
                case LEFT -> {
                    nextNeighbors[4] = current;
                    nextNeighbors[7] = current.neighbors()[6];
                    nextNeighbors[6] = current.neighbors()[5];
                    if (current.neighbors()[5] != null) {
                        nextNeighbors[5] = current.neighbors()[5].neighbors()[3];
                    }
                }
                case DOWN -> {
                    nextNeighbors[1] = current;
                    nextNeighbors[2] = current.neighbors()[4];
                    nextNeighbors[4] = current.neighbors()[7];
                    if (current.neighbors()[7] != null) {
                        nextNeighbors[7] = current.neighbors()[7].neighbors()[6];
                    }
                }
            }
            int value = Arrays.stream(nextNeighbors).filter(Objects::nonNull).mapToInt(Node::value).sum();
            Node next = new Node(value, nextNeighbors, nextDirection);
            switch (nextDirection) {
                case RIGHT -> current.neighbors()[4] = next;
                case UP -> current.neighbors()[1] = next;
                case LEFT -> current.neighbors()[3] = next;
                case DOWN -> current.neighbors()[6] = next;
            }
            current = next;
        }

        System.out.println("Part 2: " + current.value());
    }

    public enum Direction {
        RIGHT, UP, LEFT, DOWN;

        public Direction next() {
            return switch (this) {
                case RIGHT -> UP;
                case UP -> LEFT;
                case LEFT -> DOWN;
                case DOWN -> RIGHT;
            };
        }
    }

    private record Node(int value, Node[] neighbors, Direction direction) {
        // 0 1 2
        // 3   4
        // 5 6 7
        public static Node of(int value, Direction direction) {
            return new Node(value, newNeighbors(), direction);
        }

        public static Node[] newNeighbors() {
            return new Node[8];
        }
    }
}

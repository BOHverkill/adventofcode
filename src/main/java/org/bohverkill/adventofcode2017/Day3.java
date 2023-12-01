package org.bohverkill.adventofcode2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day3 {
    public static void main(String[] args) throws FileNotFoundException {
        final int input = Integer.parseInt(new Scanner(new File(Day3.class.getResource("/Day3_input").getPath())).next());

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

        int line1 = (int) (4 * Math.pow(center, 2) - 3 * center + 1);
        int line2 = (int) (4 * Math.pow(center, 2) - center + 1);
        int line3 = (int) (4 * Math.pow(center, 2) + center + 1);
        int line4 = (int) (4 * Math.pow(center, 2) + 3 * center + 1);

        System.out.println(line1 - input);
        System.out.println(line2 - input);
        System.out.println(line3 - input);
        System.out.println(line4 - input);

        System.out.println(line4 - input + center);
    }

    private static void task2(final int input) {
        int i = 2;
        int j = 1;
        int[] prev = new int[3];
        prev[0] = 1;
        while(j <= input) {
            j += prev[0];
            i++;
        }
        System.out.println(i);
    }
}

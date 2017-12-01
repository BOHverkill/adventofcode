package org.bohverkill.adventofcode2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day1 {
    public static void main(String[] args) throws FileNotFoundException {
        String input = new Scanner(new File(Day1.class.getResource("/Day1_input").getPath())).next();
        int sum1 = 0;
        int sum2 = 0;

        for (int i = 0; i < input.length(); i++) {
            if (stringIntAt(input, i) == stringIntAt(input, (i + 1) % input.length()))
                sum1 += stringIntAt(input, i);
            if (stringIntAt(input, i) == stringIntAt(input, (input.length() / 2 + i) % input.length()))
                sum2 += stringIntAt(input, i);
        }
        System.out.println(sum1);
        System.out.println(sum2);
    }

    private static int stringIntAt(String s, int i) {
        return Integer.parseInt(String.valueOf(s.charAt(i)));
    }
}

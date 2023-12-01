package org.bohverkill.adventofcode2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File(Day2.class.getResource("/2017/Day2_input").getPath()));
        int sum1 = 0;
        int sum2 = 0;
        List<List<Integer>> lines = new ArrayList<>();
        while (input.hasNextLine()) {
            Scanner line = new Scanner(input.nextLine());
            int min = Integer.MAX_VALUE;
            int max = 0;
            List<Integer> l = new ArrayList<>();
            while (line.hasNextInt()) {
                int i = line.nextInt();
                if (i > max) max = i;
                if (i < min) min = i;
                l.add(i);
            }
            lines.add(l);
            sum1 += max - min;
        }
        System.out.println(sum1);
        for (List<Integer> line : lines) {
            int i1 = 0;
            int i2 = 0;
            for (int i = 0; i < line.size(); i++) {
                for (int j = i + 1; j < line.size(); j++) {
                    if (line.get(i) % line.get(j) == 0) {
                        i1 = line.get(i);
                        i2 = line.get(j);
                    }
                    if (line.get(j) % line.get(i) == 0) {
                        i1 = line.get(j);
                        i2 = line.get(i);
                    }
                }
            }
            sum2 += i1 / i2;
        }
        System.out.println(sum2);
    }
}

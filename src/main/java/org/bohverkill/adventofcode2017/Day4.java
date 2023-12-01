package org.bohverkill.adventofcode2017;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day4 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File(Day2.class.getResource("/Day4_input").getPath()));

        int i = 0;
        int j = 0;
        while(input.hasNextLine()) {
            Scanner line = new Scanner(input.nextLine());
            Set<String> s = new HashSet<>();
            while (line.hasNext()) {
                if (!s.add(line.next())) {
                    i--;
                    break;
                }
                // add stuff
            }
            i++;
        }
        System.out.println(i);
    }
}

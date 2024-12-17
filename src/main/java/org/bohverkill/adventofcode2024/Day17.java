package org.bohverkill.adventofcode2024;

import org.bohverkill.models.Pair;
import org.bohverkill.models.Triple;
import org.bohverkill.utils.Utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 {
    private static final Pattern PROGRAM_INFORMATION_PATTERN = Pattern.compile("Register A: (\\d+)\\nRegister B: (\\d+)\\nRegister C: (\\d+)\\n\\nProgram: ([0-7,]+)");

    public static void main(String[] args) {
//        final String path = "/2024/Day17_example";
        final String path = "/2024/Day17_input";
        final Pair<Triple<Long, Long, Long>, List<Byte>> programInformation = parse(Utils.getInput(path));
        task1(programInformation);
        task2(programInformation);
    }

    private static Pair<Triple<Long, Long, Long>, List<Byte>> parse(final String input) {
        final Matcher programInformationPattern = Utils.getMatcher(PROGRAM_INFORMATION_PATTERN, input);
        final long aRegisterInitialContent = Long.parseLong(programInformationPattern.group(1));
        final long bRegisterInitialContent = Long.parseLong(programInformationPattern.group(2));
        final long cRegisterInitialContent = Long.parseLong(programInformationPattern.group(3));
        final List<Byte> program = Arrays.stream(programInformationPattern.group(4).split(",")).map(Byte::parseByte).toList();
        return Pair.of(Triple.of(aRegisterInitialContent, bRegisterInitialContent, cRegisterInitialContent), program);
    }

    private static void task1(final Pair<Triple<Long, Long, Long>, List<Byte>> programInformation) {
        String out = runProgram(programInformation);
        System.out.println("Part 1: " + out);
    }

    private static String runProgram(Pair<Triple<Long, Long, Long>, List<Byte>> programInformation) {
        final List<Byte> program = programInformation.b();

        long a = programInformation.a().a();
        long b = programInformation.a().b();
        long c = programInformation.a().c();
        int instructionPointer = 0;

        StringBuilder out = new StringBuilder();

        while (instructionPointer >= 0 && instructionPointer < program.size() - 1) {
            final Instruction instruction = Instruction.values()[program.get(instructionPointer)];
            final byte literalOperand = program.get(instructionPointer + 1);

            switch (instruction) {
                case ADV -> a = (long) (a / Math.pow(2, decodeComboOperand(literalOperand, a, b, c)));
                case BXL -> b = b ^ literalOperand;
                case BST -> b = decodeComboOperand(literalOperand, a, b, c) % 8;
                case JNZ -> {
                    if (a == 0) {
                        instructionPointer += 2;
                    } else {
                        instructionPointer = literalOperand;
                    }
                }
                case BXC -> b = b ^ c;
                case OUT -> out.append(((byte) (decodeComboOperand(literalOperand, a, b, c) % 8))).append(",");
                case BDV -> b = (long) (a / Math.pow(2, decodeComboOperand(literalOperand, a, b, c)));
                case CDV -> c = (long) (a / Math.pow(2, decodeComboOperand(literalOperand, a, b, c)));
            }
            if (instruction != Instruction.JNZ) {
                instructionPointer += 2;
            }
        }
        if (!out.isEmpty()) {
            out.deleteCharAt(out.length() - 1);
        }
        return out.toString();
    }

    private static void task2(final Pair<Triple<Long, Long, Long>, List<Byte>> programInformation) {
//        long i = 35_184_372_088_832L; // first 16 number output (lower bound)
//        long i =  281_474_976_710_656L; // first 17 number output (upper bound)
//        long i = 236_556_006_886_912L; // first solution (too high)
        long i = 236_555_996_442_752L;
        disassemble(programInformation.b());
        while (!runProgram(programInformation, i)) {
            i++;
        }
        System.out.println("Part 2: " + i);
    }

    private static boolean runProgram(Pair<Triple<Long, Long, Long>, List<Byte>> programInformation, final long initialARegisterValue) {
        final List<Byte> program = programInformation.b();

        long a = initialARegisterValue;
        long b = programInformation.a().b();
        long c = programInformation.a().c();
        int instructionPointer = 0;

        List<Byte> out = new LinkedList<>();

        while (instructionPointer >= 0 && instructionPointer < program.size() - 1) {
            final Instruction instruction = Instruction.values()[program.get(instructionPointer)];
            final byte literalOperand = program.get(instructionPointer + 1);

            switch (instruction) {
                case ADV -> {
                    a = (long) (a / Math.pow(2, decodeComboOperand(literalOperand, a, b, c)));
                    instructionPointer += 2;
                }
                case BXL -> {
                    b = b ^ literalOperand;
                    instructionPointer += 2;
                }
                case BST -> {
                    b = decodeComboOperand(literalOperand, a, b, c) % 8;
                    instructionPointer += 2;
                }
                case JNZ -> {
                    if (a == 0) {
                        instructionPointer += 2;
                    } else {
                        instructionPointer = literalOperand;
                    }
                }
                case BXC -> {
                    b = b ^ c;
                    instructionPointer += 2;
                }
                case OUT -> {
                    out.add((byte) (decodeComboOperand(literalOperand, a, b, c) % 8));
                    instructionPointer += 2;
                }
                case BDV -> {
                    b = (long) (a / Math.pow(2, decodeComboOperand(literalOperand, a, b, c)));
                    instructionPointer += 2;
                }
                case CDV -> {
                    c = (long) (a / Math.pow(2, decodeComboOperand(literalOperand, a, b, c)));
                    instructionPointer += 2;
                }
            }
        }
        return out.equals(program);
    }

    private static void reverseProgram(Pair<Triple<Long, Long, Long>, List<Byte>> programInformation) {
        final List<Byte> program = programInformation.b();

        for (int i = program.size() - 1; i >= 0; i--) {
            byte digit = program.get(i);
            // A = 0
            // digit = B % 8 = 0
            // B = B ^ 5 = ? % 8 = 0
            // A = A/2^3 = 0
            // B = B ^ C
            // C = A/2^B
            // B = B ^ 3
            // B = A % 8

            // A 281_474_976_710_656
        }

        // digits the same (from behind)
        // 5
        // initialARegisterValue = 236,580,548,130,816, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 2, 2, 6, 4, 5, 1, 6, 3, 3, 5, 5, 5, 3, 0]
        // 6
        // initialARegisterValue = 216,184,477,644,800, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 2, 4, 7, 4, 2, 6, 2, 6, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 218,383,638,327,296, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 6, 1, 6, 4, 6, 1, 7, 5, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 233,777,763,104,768, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 2, 4, 6, 7, 7, 0, 2, 5, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 235,976,923,787,264, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 6, 5, 6, 7, 3, 6, 3, 4, 1, 5, 5, 5, 3, 0]
        // 7
        // initialARegisterValue = 233,777,483,948,032, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 6, 4, 5, 6, 1, 6, 1, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,572,243,165,184, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 6, 6, 7, 5, 7, 6, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,580,801,208,320, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 6, 0, 6, 6, 3, 6, 5, 3, 1, 5, 5, 5, 3, 0]
        // 8
        // initialARegisterValue = 236,572,243,165,184, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 6, 6, 7, 5, 7, 6, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,572,237,785,856, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 2, 1, 7, 1, 2, 0, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,573,174,940,672, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 4, 5, 3, 2, 6, 2, 0, 3, 1, 5, 5, 5, 3, 0]
        // 9
        // initialARegisterValue = 236,555,058,557,184, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 2, 3, 5, 6, 1, 1, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,555,595,387,008, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 4, 5, 7, 1, 6, 0, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,555,603,742,336, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 4, 1, 1, 0, 3, 1, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,555,996,442,752, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 4, 5, 5, 6, 2, 7, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,556,006,886,912, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 1, 5, 3, 5, 0, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,556,008,975,744, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 0, 5, 1, 1, 1, 1, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,572,237,111,552, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 2, 3, 4, 5, 4, 1, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,573,177,085,952, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 6, 6, 0, 2, 6, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,573,187,530,112, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 0, 3, 1, 0, 1, 6, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,573,189,618,944, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 2, 3, 0, 6, 1, 6, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        // initialARegisterValue = 236,581,565,835,264, a = 0, b = 0, c = 0, instructionPointer = 16, out = [6, 6, 2, 3, 5, 5, 2, 7, 0, 3, 1, 5, 5, 5, 3, 0]
        //
    }

    private static long decodeComboOperand(final byte operand, final long a, final long b, final long c) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            case 7 -> throw new IllegalArgumentException("Not a valid programs");
            default -> throw new IllegalStateException("Unexpected literal operand: " + operand);
        };
    }

    private static void disassemble(List<Byte> program) {
        for (int i = 0; i < program.size(); i += 2) {
            final byte b = program.get(i);
            final Instruction instruction = Instruction.values()[b];
            final byte literalOperand = program.get(i + 1);

            String out = switch (instruction) {
                case ADV -> "ADV " + comboOperandToString(literalOperand);
                case BXL -> "BXL " + literalOperand;
                case BST -> "BST " + comboOperandToString(literalOperand);
                case JNZ -> "JNZ " + literalOperand;
                case BXC -> "BXC";
                case OUT -> "OUT " + comboOperandToString(literalOperand);
                case BDV -> "BDV " + comboOperandToString(literalOperand);
                case CDV -> "CDV " + comboOperandToString(literalOperand);
            };
            System.out.println(out);
        }
    }

    private static String comboOperandToString(final byte operand) {
        return switch (operand) {
            case 0, 1, 2, 3 -> String.valueOf(operand);
            case 4 -> "A";
            case 5 -> "B";
            case 6 -> "C";
            case 7 -> throw new IllegalArgumentException("Not a valid programs");
            default -> throw new IllegalStateException("Unexpected literal operand: " + operand);
        };
    }

    private enum Instruction {
        ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV
    }
}

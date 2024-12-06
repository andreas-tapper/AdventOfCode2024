package com.adventofcode;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Day 1 of Advent of Code 2024.");

        if (args.length < 1) {
            System.out.println("usage <filename with inputs>");
            return;
        }

        if ("1".equals(args[0])) {
            partOne(args[1]);
        } else if ("2".equals(args[0])) {
            partTwo(args[1]);
        }
    }

    private static void partOne(String input) throws IOException {
        try (var reader = new BufferedReader(new FileReader(input, StandardCharsets.UTF_8))) {
            String line;
            String[] buffer = new String[4];
            var sum = 0;

            buffer[0] = null;
            buffer[1] = reader.readLine();
            buffer[2] = reader.readLine();
            buffer[3] = reader.readLine();

            while((line = reader.readLine()) != null) {
                pushBuffer(buffer, line);
                sum += findWord(buffer);
            }

            pushBuffer(buffer, null);
            sum += findWord(buffer);

            pushBuffer(buffer, null);
            sum += findWord(buffer);

            pushBuffer(buffer, null);
            sum += findWord(buffer);

            System.out.printf("Answer is: %d%n", sum);
        }
    }

    private static void pushBuffer(String[] buffer, String line) {
        for(var i = 0; i < buffer.length - 1; i += 1) {
            buffer[i] = buffer[i + 1];
        }
        buffer[buffer.length - 1] = line;
    }

    private static int findWord(String[] buffer) {
        return countHorizontal(buffer[0]) + countVertical(buffer) + countDiagonal(buffer);
    }

    private static int countHorizontal(String line) {
        var count = 0;
        var index = 0;
        while((index = line.indexOf("XMAS", index)) >= 0) {
            index += 4;
            count += 1;
        }

        index = 0;
        while((index = line.indexOf("SAMX", index)) >= 0) {
            index += 4;
            count += 1;
        }

        return count;
    }

    private static int countVertical(String[] buffer) {
        if(buffer[3] == null) {
            return 0;
        }

        var to = Math.min(Math.min(Math.min(buffer[0].length(), buffer[1].length()), buffer[2].length()), buffer[3].length());
        var count = 0;
        for(var i=0; i < to; i += 1) {
            if(buffer[0].charAt(i) == 'X' && buffer[1].charAt(i) == 'M' && buffer[2].charAt(i) == 'A' && buffer[3].charAt(i) == 'S') {
                count += 1;
            } else if(buffer[0].charAt(i) == 'S' && buffer[1].charAt(i) == 'A' && buffer[2].charAt(i) == 'M' && buffer[3].charAt(i) == 'X') {
                count += 1;
            }
        }

        return count;
    }

    private static int countDiagonal(String[] buffer) {
        if(buffer[3] == null) {
            return 0;
        }

        var count = 0;
        for(var i=0; i < buffer[3].length() - 3; i += 1) {
            if(buffer[0].charAt(i) == 'X' && buffer[1].charAt(i + 1) == 'M' && buffer[2].charAt(i + 2) == 'A' && buffer[3].charAt(i + 3) == 'S') {
                count += 1;
            } else if (buffer[0].charAt(i) == 'S' && buffer[1].charAt(i + 1) == 'A' && buffer[2].charAt(i + 2) == 'M' && buffer[3].charAt(i + 3) == 'X') {
                count += 1;
            }
        }

        for(var i=buffer[3].length() - 1; i >= 3; i -= 1) {
            if(buffer[0].charAt(i) == 'X' && buffer[1].charAt(i - 1) == 'M' && buffer[2].charAt(i - 2) == 'A' && buffer[3].charAt(i - 3) == 'S') {
                count += 1;
            } else if(buffer[0].charAt(i) == 'S' && buffer[1].charAt(i - 1) == 'A' && buffer[2].charAt(i - 2) == 'M' && buffer[3].charAt(i - 3) == 'X') {
                count += 1;
            }
        }

        return count;
    }

    private static void partTwo(String input) throws IOException {
        try (var reader = new BufferedReader(new FileReader(input, StandardCharsets.UTF_8))) {
            String line;
            String[] buffer = new String[3];
            var sum = 0;

            buffer[0] = null;
            buffer[1] = reader.readLine();
            buffer[2] = reader.readLine();

            while((line = reader.readLine()) != null) {
                pushBuffer(buffer, line);
                sum += findX(buffer);
            }

            System.out.printf("Answer is: %d%n", sum);
        }
    }

    private static int findX(String[] buffer) {
        if(buffer[2] == null) {
            return 0;
        }

        var to = Math.min(Math.min(buffer[0].length(), buffer[1].length()), buffer[2].length());
        var count = 0;

        for(var i=0; i < to - 2; i += 1) {
            if(isBackslashMAS(buffer, i) && isFrontslashMAS(buffer, i + 2)) {
                count += 1;
            }
        }

        return count;
    }

    private static boolean isBackslashMAS(String[] buffer, int offset) {
        if(buffer[0].charAt(offset) == 'M' && buffer[1].charAt(offset + 1) == 'A' && buffer[2].charAt(offset + 2) == 'S') {
            return true;
        }

        return buffer[0].charAt(offset) == 'S' && buffer[1].charAt(offset + 1) == 'A' && buffer[2].charAt(offset + 2) == 'M';
    }

    private static boolean isFrontslashMAS(String[] buffer, int offset) {
        if(buffer[0].charAt(offset) == 'M' && buffer[1].charAt(offset - 1) == 'A' && buffer[2].charAt(offset - 2) == 'S') {
            return true;
        }

        return buffer[0].charAt(offset) == 'S' && buffer[1].charAt(offset - 1) == 'A' && buffer[2].charAt(offset - 2) == 'M';
    }
}
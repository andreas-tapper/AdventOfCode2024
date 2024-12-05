package com.adventofcode;

import javax.swing.text.html.Option;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

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
            var lines = reader.lines()
                    .collect(toList());

            var sum = 0;

            for (String line : lines) {
                var numbers = Arrays.stream(line.split(" +")).map(Integer::parseInt).toList();

                if (IsIncreasing(numbers) || IsDecreasing(numbers)) {
                    sum += 1;
                }
            }

            System.out.printf("Answer is: %d%n", sum);
        }
    }

    private static void partTwo(String input) throws IOException {
        try (var reader = new BufferedReader(new FileReader(input, StandardCharsets.UTF_8))) {
            var lines = reader.lines()
                    .collect(toList());

            var sum = 0;

            for (String line : lines) {
                var numbers = Arrays.stream(line.split(" +")).map(Integer::parseInt).toList();

                var increasing = IsIncreasingWithDampener(numbers);
                var decreasing = IsIncreasingWithDampener(numbers.reversed());
                if (increasing || decreasing) {
                    sum += 1;
                }
            }

            System.out.printf("Answer is: %d%n", sum);
        }
    }

    private static boolean IsIncreasing(List<Integer> list) {
        return IsIncreasing(list.getFirst(), list.subList(1, list.size()));
    }

    private static boolean IsIncreasing(Integer value, List<Integer> list) {
        if(list.isEmpty()) return true;
        if(value < list.getFirst() && value + 3 >= list.getFirst()) return IsIncreasing(list.getFirst(), list.subList(1, list.size()));
        return false;
    }

    private static boolean IsDecreasing(List<Integer> list) {
        return IsDecreasing(list.getFirst(), list.subList(1, list.size()));
    }

    private static boolean IsDecreasing(Integer value, List<Integer> list)
    {
        if(list.isEmpty()) return true;
        if(value > list.getFirst() && value - 3 <= list.getFirst()) return IsDecreasing(list.getFirst(), list.subList(1, list.size()));
        return false;
    }

    private static boolean IsIncreasingWithDampener(List<Integer> list) {
        if(IsIncreasing(list)) return true;
        if(IsDecreasing(list)) return true;

        for(var i=0; i < list.size(); i++) {
            var list2 = new ArrayList<>(list);
            list2.remove(i);
            if(IsDecreasing(list2)) return true;
            if(IsDecreasing(list2)) return true;
        }

        return false;
    }
}
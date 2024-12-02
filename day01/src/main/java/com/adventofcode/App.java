package com.adventofcode;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class App
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Day 1 of Advent of Code 2024." );

        if(args.length < 1) {
            System.out.println("usage <filename with inputs>");
            return;
        }

        if("1".equals(args[0])) {
            partOne(args[1]);
        } else if("2".equals(args[0])) {
            partTwo(args[1]);
        }
    }

    private static void partOne(String input) throws IOException {
        var listOne = new ArrayList<Integer>();
        var listTwo = new ArrayList<Integer>();

        try(var reader = new BufferedReader(new FileReader(input, Charset.forName("UTF-8")))) {
            var lines = reader.lines()
                    .collect(toList());

            for (String line : lines) {
                var numbers = line.split("\s+");
                listOne.add(Integer.parseInt(numbers[0].trim()));
                listTwo.add(Integer.parseInt(numbers[1].trim()));
            }

            if(listOne.size() != listTwo.size()) {
                System.out.println("Number of elements does not match");
                return;
            }

            listOne.sort(Comparator.naturalOrder());
            listTwo.sort(Comparator.naturalOrder());

            var sum = 0;
            for (var i = 0; i < listOne.size(); i += 1) {
                sum += Math.max(listOne.get(i), listTwo.get(i)) - Math.min(listOne.get(i), listTwo.get(i));
            }

            System.out.printf("Answer is: %d%n", sum);
        }
    }

    private static void partTwo(String input) throws IOException {
        var listOne = new ArrayList<Integer>();
        var listTwo = new ArrayList<Integer>();

        try(var reader = new BufferedReader(new FileReader(input, Charset.forName("UTF-8")))) {
            var lines = reader.lines()
                    .collect(toList());

            for (String line : lines) {
                var numbers = line.split("\s+");
                listOne.add(Integer.parseInt(numbers[0].trim()));
                listTwo.add(Integer.parseInt(numbers[1].trim()));
            }

            if(listOne.size() != listTwo.size()) {
                System.out.println("Number of elements does not match");
                return;
            }

            listOne.sort(Comparator.naturalOrder());
            listTwo.sort(Comparator.naturalOrder());

            var counts = listTwo.stream().collect(groupingBy(x -> x, counting()));

            var sum = 0;
            for (var value : listOne) {
                sum += Optional.ofNullable(counts.get(value)).map(x -> x * value).orElse(0L);
            }

            System.out.printf("Answer is: %d%n", sum);
        }
    }
}
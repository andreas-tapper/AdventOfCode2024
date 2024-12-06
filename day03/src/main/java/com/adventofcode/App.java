package com.adventofcode;

import javax.swing.text.html.Option;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

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
            var sum = reader.lines()
                    .flatMapToInt(App::scan)
                    .sum();

            System.out.printf("Answer is: %d%n", sum);
        }
    }

    private static final Pattern Multiply = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

    private static IntStream scan(String input) {
        var matcher = Multiply.matcher(input);
        var spliterator = new Spliterator.OfInt() {
            @Override
            public boolean tryAdvance(IntConsumer action) {
                if(!matcher.find()) {
                    return false;
                }

                var a = Integer.parseInt(matcher.group(1));
                var b = Integer.parseInt(matcher.group(2));
                action.accept(a*b);
                return true;
            }

            @Override
            public OfInt trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        };

        return StreamSupport.intStream(spliterator, false);
    }

    private static void partTwo(String input) throws IOException {
        try (var reader = new BufferedReader(new FileReader(input, StandardCharsets.UTF_8))) {
            var program = reader.lines().reduce((a,b) -> a + b);
            var sum = scanWithOperatorSupport(program.get()).sum();

            System.out.printf("Answer is: %d%n", sum);
        }
    }

    private static final Pattern MultiplyWithOperator = Pattern.compile("(mul\\((?<a>\\d+),(?<b>\\d+)\\))|(((?<op>do|don't)\\(\\))+.*?mul\\((?<x>\\d+),(?<y>\\d+)\\))", Pattern.MULTILINE);

    private static IntStream scanWithOperatorSupport(String input) {
        var matcher = MultiplyWithOperator.matcher(input);
        var spliterator = new Spliterator.OfInt() {
            private boolean enabled = true;

            @Override
            public boolean tryAdvance(IntConsumer action) {
                if(!matcher.find()) {
                    return false;
                }

                var op = matcher.group("op");
                if("do".equalsIgnoreCase(op)) {
                    enabled = true;
                }

                if("don't".equalsIgnoreCase(op)) {
                    enabled = false;
                }

                if(!enabled) {
                    return true;
                }

                var hasInstruction = op != null;
                var x = Integer.parseInt(matcher.group(hasInstruction ? "x" : "a"));
                var y = Integer.parseInt(matcher.group(hasInstruction ? "y" : "b"));
                action.accept(x*y);

                return true;
            }

            @Override
            public OfInt trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        };

        return StreamSupport.intStream(spliterator, false);
    }
}
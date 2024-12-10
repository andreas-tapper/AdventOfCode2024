package com.adventofcode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.stream.Collectors.toList;

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
            var sum = 0;
            String line;
            var order = new HashMap<Integer, List<Integer>>();

            // Stage one setup rules
            while((line = reader.readLine()) != null) {
                if(line.trim().isEmpty()) {
                    break;
                }

                var divider = line.indexOf('|');
                int key = Integer.parseInt(line.substring(0, divider));
                if(!order.containsKey(key)) {
                    order.put(key, new ArrayList<>());
                }

                int value = Integer.parseInt(line.substring(divider + 1));
                order.get(key).add(value);
            }

            while((line = reader.readLine()) != null) {
                var update = Arrays.stream(line.trim().split(",")).map(Integer::parseInt).toArray(Integer[]::new);
                var invalid = false;

                for(var i=0; i<update.length; i += 1) {
                    // Ignore pages that doesn't have a rule
                    if(!order.containsKey(update[i])) {
                        continue;
                    }

                    var pages = order.get(update[i]);
                    for(var j=0; j<i; j += 1) {
                        for (var page : pages) {
                            if(page.equals(update[j])) {
                                // We found an invalid update
                                invalid = true;
                                break;
                            }
                        }
                    }

                    if(invalid) {
                        break;
                    }
                }

                if(invalid) {
                    continue;
                }

                // This was a valid update find the middle page
                var middle = (int)Math.floor(update.length / 2);
                sum += update[middle];
            }

            System.out.printf("Answer is: %d%n", sum);
        }
    }

    private static void partTwo(String input) throws IOException {
        try (var reader = new BufferedReader(new FileReader(input, StandardCharsets.UTF_8))) {
            var sum = 0;
            String line;
            var order = new HashMap<Integer, List<Integer>>();

            // Stage one setup rules
            while((line = reader.readLine()) != null) {
                if(line.trim().isEmpty()) {
                    break;
                }

                var divider = line.indexOf('|');
                int key = Integer.parseInt(line.substring(0, divider));
                if(!order.containsKey(key)) {
                    order.put(key, new ArrayList<>());
                }

                int value = Integer.parseInt(line.substring(divider + 1));
                order.get(key).add(value);
            }

            while((line = reader.readLine()) != null) {
                var update = Arrays.stream(line.trim().split(",")).map(Integer::parseInt).collect(toList());
                var invalid = false;

                for(var i=0; i<update.size(); i += 1) {
                    // Ignore pages that doesn't have a rule
                    if(!order.containsKey(update.get(i))) {
                        continue;
                    }

                    var pages = order.get(update.get(i));
                    for(var j=0; j<i; j += 1) {
                        for (var page : pages) {
                            if(page.equals(update.get(j))) {
                                // We found an invalid update
                                invalid = true;
                                break;
                            }
                        }
                    }

                    if(invalid) {
                        break;
                    }
                }

                // This time we need to order this correctly
                if(!invalid) {
                    continue;
                }

                var i = 0;
                while(i < update.size() - 1) {
                    for (i = 0; i < update.size(); i += 1) {
                        var page = update.get(i);
                        var rules = order.get(page);

                        if(rules == null) {
                            continue;
                        }

                        for(var j=0; j < i; j += 1) {
                            if(rules.contains(update.get(j))) {
                                update.add(j, page);
                                update.remove(i + 1); // Because we shifted the list...
                                break;
                            }
                        }
                    }
                }

                var middle = (int)Math.floor(update.size() / 2);
                sum += update.get(middle);
            }

            System.out.printf("Answer is: %d%n", sum);
        }
    }
}
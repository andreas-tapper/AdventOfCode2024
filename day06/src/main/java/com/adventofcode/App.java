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
            String line;
            var gameMap = new LinkedList<char[]>();
            while((line = reader.readLine()) != null) {
                gameMap.addFirst(line.toCharArray());
            }

            var position = findStart(gameMap);
            visit(gameMap, position);

            var player = new Player(position, Direction.UPWARDS);

            while (true) {
                player.move();

                if(isPositionOutOfBounds(gameMap, player.getPosition())) {
                    break;
                }

                if(isPositionBlocked(gameMap,player.getPosition())) {
                    player.back();
                    player.turn();
                    continue;
                }

                visit(gameMap, position);
            }

            System.out.printf("Answer is: %d%n", countVisited(gameMap));
        }
    }

    private static Position findStart(List<char[]> gameMap) {
        for (var i = 0; i < gameMap.size(); i += 1) {
            var row = gameMap.get(i);
            for(var j = 0; j < row.length; j += 1) {
                if(row[j] == '^') {
                    return new Position(j, i);
                }
            }
        }

        throw new IllegalArgumentException("No start found.");
    }

    private static void visit(List<char[]> gameMap, Position position) {
        gameMap.get(position.getY())[position.getX()] = 'X';
    }

    private static boolean isPositionBlocked(List<char[]> gameMap, Position position) {
        return gameMap.get(position.getY())[position.getX()] == '#';
    }

    private static boolean isPositionOutOfBounds(List<char[]> gameMap, Position position) {
        if(position.getY() < 0 || position.getY() >= gameMap.size()) {
            return true;
        }

        var row = gameMap.get(position.getY());
        return position.getX() < 0 || position.getX() >= row.length;
    }

    private static int countVisited(List<char[]> gameMap) {
        var sum = 0;
        for (var row : gameMap) {
            for (var c : row) {
                if (c == 'X') {
                    sum += 1;
                }
            }
        }
        return sum;
    }

    private static class Player {
        private Position position;
        private Direction direction;

        public Player(Position position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        public void turn() {
            direction = direction.rotate90();
        }

        public void move() {
            switch (direction) {
                case UPWARDS:
                    position.incrementY();
                    break;
                case DOWNWARDS:
                    position.decrementY();
                    break;
                case LEFTWARDS:
                    position.decrementX();
                    break;
                case RIGHTWARDS:
                    position.incrementX();
                    break;
            }
        }

        public void back() {
            switch (direction) {
                case UPWARDS:
                    position.decrementY();
                    break;
                case DOWNWARDS:
                    position.incrementY();
                    break;
                case LEFTWARDS:
                    position.incrementX();
                    break;
                case RIGHTWARDS:
                    position.decrementX();
                    break;
            }
        }

        public Position getPosition() {
            return position;
        }
    }

    private enum Direction {
        UPWARDS, DOWNWARDS, LEFTWARDS, RIGHTWARDS;

        public Direction rotate90() {
            return switch (this) {
                case UPWARDS -> RIGHTWARDS;
                case DOWNWARDS -> LEFTWARDS;
                case LEFTWARDS -> UPWARDS;
                case RIGHTWARDS -> DOWNWARDS;
            };
        }
    }

    private static class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void incrementX() {
            this.x += 1;
        }

        public void incrementY() {
            this.y += 1;
        }

        public void decrementX() {
            this.x -= 1;
        }

        public void decrementY() {
            this.y -= 1;
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
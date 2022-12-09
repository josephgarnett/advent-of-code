package com.mtab.adventofcode.year2022.day9;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.Direction;
import com.mtab.adventofcode.utils.InputUtils;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day9 implements Function<List<Day9.Instruction>, Integer> {
    public static List<Instruction> getInput() throws IOException {
        return InputUtils.readLines("2022/day9/input.txt")
                .map(Instruction::of)
                .toList();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2022/day9",
                () -> new Day9().apply(Day9.getInput()),
                1);

        Preconditions.checkArgument(result == 6269);
    }

    @Override
    public Integer apply(final List<Instruction> instructions) {
        final Rope start = new Rope(
                new Point2D.Double(0, 0),
                List.of(new Point2D.Double(0,0)));
        final Set<Point2D> visited = new HashSet<>();

        visited.add(start.tail());

        instructions.stream()
                .reduce(start, (rope, instruction) -> instruction.stream()
                        .boxed()
                        .reduce(
                                rope,
                                (r, i) -> {
                                    final Rope next = Rope.from(r, instruction);
                                    visited.add(next.tail());

                                    return next;
                                },
                                (r1, r2) -> {
                                    throw new RuntimeException();
                                }),
                        (r1, r2) -> {
                            throw new RuntimeException();
                        });

        return visited.size();
    }

    record Instruction(Direction direction, int value) {
        static Instruction of(final String s) {
            final String[] input = s.split(" ");
            final Direction direction = switch (input[0]) {
                case "R" -> Direction.EAST;
                case "U" -> Direction.NORTH;
                case "L" -> Direction.WEST;
                case "D" -> Direction.SOUTH;
                default -> throw new IllegalStateException();
            };

            return new Instruction(direction, Integer.parseInt(input[1]));
        }

        IntStream stream() {
            return IntStream.range(0, this.value());
        }
    }

    record Rope(Point2D head, List<Point2D> chain) {
        static Rope from(
                final Rope source,
                final Instruction instruction) {
            final Point2D head = switch(instruction.direction()) {
                case NORTH -> new Point2D.Double(source.head().getX(), source.head().getY() + 1);
                case EAST ->  new Point2D.Double(source.head().getX() + 1, source.head().getY());
                case SOUTH -> new Point2D.Double(source.head().getX(), source.head().getY() - 1);
                case WEST -> new Point2D.Double(source.head().getX() - 1, source.head().getY());
            };

            if (Rope.tailInRange(source, head)) {
                return new Rope(head, List.of(source.tail()));
            }

            final Point2D tail = switch(instruction.direction()) {
                case NORTH -> new Point2D.Double(head.getX(), head.getY() - 1);
                case EAST ->  new Point2D.Double(head.getX() - 1, head.getY());
                case SOUTH -> new Point2D.Double(head.getX(), head.getY() + 1);
                case WEST -> new Point2D.Double(head.getX() + 1, head.getY());
            };


            return new Rope(head, List.of(tail));
        }

        static boolean tailInRange(
                final Rope source,
                final Point2D newHead) {
            return Math.abs(newHead.getY() - source.tail().getY()) <= 1
                    && Math.abs(newHead.getX() - source.tail().getX()) <= 1;
        }

        Point2D tail() {
            return this.chain.get(this.chain.size() - 1);
        }
    }
}

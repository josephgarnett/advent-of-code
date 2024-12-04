package io.garnett.adventofcode.year2022.day9;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.CustomErrors;
import io.garnett.adventofcode.utils.Direction;
import io.garnett.adventofcode.utils.InputUtils;

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
                () -> new Day9().apply(Day9.getInput()));

        Preconditions.checkArgument(result == 2557);
    }

    private List<Point2D> getKnots() {
        return List.of(
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0));
    }

    private Motion instructionReducer(
            final Motion motion,
            final Instruction instruction) {
        return instruction.stream()
                .boxed()
                .reduce(motion,
                        (m, i) -> {
                            final Rope next = Rope.from(m.rope(), instruction);;
                            m.visited().add(next.tail());

                            return new Motion(
                                    next,
                                    m.visited());
                        },
                        CustomErrors.notImplementedCombiner());
    }

    private Motion getStart() {
        final Rope startRope = new Rope(
                new Point2D.Double(0, 0),
                this.getKnots());

        final Set<Point2D> visited = new HashSet<>();
        visited.add(startRope.tail());


        return new Motion(startRope, visited);
    }

    @Override
    public Integer apply(final List<Instruction> instructions) {
        return instructions.stream()
                .reduce(this.getStart(),
                        this::instructionReducer,
                        CustomErrors.notImplementedCombiner())
                .visited()
                .size();
    }

    record Motion(Rope rope, Set<Point2D> visited) {}

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
                default -> throw new IllegalStateException();
            };

            final List<Point2D> chain = source.chain()
                    .stream()
                    .reduce(
                            new ArrayList<>(),
                            (list, p) -> {
                                final Point2D previousPoint = list.isEmpty() ? head : list.get(list.size() - 1);

                                if (Rope.tailInRange(p, previousPoint)) {
                                    list.add(p);
                                } else {
                                    // Otherwise, if the head and tail aren't touching and aren't in the same row or column,
                                    // the tail always moves one step diagonally to keep up:
                                    final double deltaX = previousPoint.getX() - p.getX();
                                    final double deltaY = previousPoint.getY() - p.getY();

                                    final Point2D nextPoint = new Point2D.Double(p.getX(), p.getY());

                                    if (deltaX > 0) {
                                        nextPoint.setLocation(nextPoint.getX() + 1, nextPoint.getY());
                                    } else if (deltaX < 0) {
                                        nextPoint.setLocation(nextPoint.getX() - 1, nextPoint.getY());
                                    }

                                    if (deltaY > 0) {
                                        nextPoint.setLocation(nextPoint.getX(), nextPoint.getY() + 1);
                                    } else if (deltaY < 0) {
                                        nextPoint.setLocation(nextPoint.getX(), nextPoint.getY() - 1);
                                    }
                                    list.add(nextPoint);
                                }

                                return list;
                            },
                            CustomErrors.notImplementedCombiner());


            return new Rope(head, chain);
        }

        static boolean tailInRange(
                final Point2D source,
                final Point2D target) {
            return Math.abs(target.getY() - source.getY()) <= 1
                    && Math.abs(target.getX() - source.getX()) <= 1;
        }

        Point2D tail() {
            return this.chain.get(this.chain.size() - 1);
        }
    }
}

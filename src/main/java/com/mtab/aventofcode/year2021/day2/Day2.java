package com.mtab.aventofcode.year2021.day2;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.models.IInputLoader;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Day2 implements
        Supplier<Integer>,
        IInputLoader<Day2.Instruction> {

    private static final String INPUT_PATH = "2021/day2/input.txt";

    @Override
    public Integer get() {
        int depth = 0;
        int position = 0;
        int aim = 0;

        final List<Instruction> instructions = this.getInput(Day2.INPUT_PATH);

        for (int i = 0; i < instructions.size(); ++i) {
            final Instruction instruction = instructions.get(i);
            final Direction direction = instruction.direction;

            if (direction.isVertical()) {
                aim = direction.apply(aim, instruction.value);
            } else {
                position = direction.apply(position, instruction.value);
                depth = depth + (aim * instruction.value);
            }
        }

        return position * depth;
    }

    @Override
    public Instruction parseLine(final String line, final int index) {
        final String[] parts = line.trim().split(" ");
        final Direction direction = Direction.valueOf(parts[0].toUpperCase());
        final int value = Integer.parseInt(parts[1]);

        return new Instruction(direction, value);
    }

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final Integer result = new Day2().get();
        Preconditions.checkArgument(result == 1741971043);
        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    private enum Direction implements BiFunction<Integer, Integer, Integer> {
        FORWARD("forward") {
            @Override
            public Integer apply(final Integer value, final Integer delta) {
                return value + delta;
            }
        },
        BACKWARDS("backwards") {
            @Override
            public Integer apply(final Integer value, final Integer delta) {
                return value - delta;
            }
        },
        UP("up") {
            @Override
            public Integer apply(final Integer value, final Integer delta) {
                return value - delta;
            }
        },
        DOWN("down") {
            @Override
            public Integer apply(final Integer value, final Integer delta) {
                return value + delta;
            }
        };

        private final String label;

        private Direction(final String label) {
            this.label = label;
        }

        public boolean isVertical() {
            return this == Direction.UP || this == Direction.DOWN;
        }

        public String toString() {
            return this.label;
        }
    }

    public class Instruction {
        private final Direction direction;
        private final int value;

        Instruction(final Direction direction, final int value) {
            this.direction = direction;
            this.value = value;
        }

        public Direction getDirection() {
            return this.direction;
        }

        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("direction", this.direction)
                    .add("value", this.value)
                    .toString();
        }
    }
}

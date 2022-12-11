package com.mtab.adventofcode.year2022.day10;

import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.CustomErrors;
import com.mtab.adventofcode.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Day10 implements Function<List<Day10.Command>, String> {
    public static List<Command> getInput() throws IOException {
        return InputUtils.readLines("2022/day10/input.txt")
                        .map(Day10.Command::of)
                        .toList();
    }

    public static void main(final String[] args) throws Exception {
        Application.challenge(
                "2022/day10",
                () -> new Day10().apply(Day10.getInput()));
    }

    private static final Set<Integer> TEST_CYCLES = Set.of(
            20,
            60,
            100,
            140,
            180,
            220);

    private Long signalStrength(final Map<Integer, Integer> values) {
        return values.entrySet()
                .stream()
                .filter(e -> TEST_CYCLES.contains(e.getKey()))
                .reduce(0L, (acc, e) -> {
                    return acc + ((long)e.getKey() * (long)e.getValue());
                }, CustomErrors.notImplementedCombiner());
    }

    private String print(final Map<Integer, Integer> values) {
        final StringBuilder sb = new StringBuilder();

        for (int y = 0; y < 6; y++) {
            sb.append(System.lineSeparator());
            for (int x = 0; x < 40; x++) {
                final int cycle = (y* 40) + x + 1;

                final int register = values.get(cycle);

                if (Math.abs(x - register) <= 1) {
                    sb.append("@@");
                } else {
                    sb.append("  ");
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String apply(List<Command> commands) {
        final AtomicInteger registerX = new AtomicInteger(1);
        final AtomicInteger cycles = new AtomicInteger(0);
        final Map<Integer, Integer> values = new HashMap<>();

        for (final Command c : commands) {
            for (int i = 0; i < c.instruction().getCycles(); i++) {
                values.put(
                        cycles.incrementAndGet(),
                        registerX.get());
            }

            values.put(
                    cycles.incrementAndGet(),
                    registerX.getAndAdd(c.value()));
        }

        return this.print(values);
    }

    enum Instruction {
        NOOP(0),
        ADDX(1);

        private final int cycles;
        Instruction(final int cycles) {
            this.cycles = cycles;
        }

        int getCycles() {
            return this.cycles;
        }
    }
    record Command(Instruction instruction, int value){
        public static Command of(final String s) {
            final String[] parts = s.split(" ");

            if (StringUtils.equals(Instruction.NOOP.name(), parts[0].toUpperCase())) {
                return new Command(
                        Instruction.NOOP,
                        0);
            }

            return new Command(
                    Instruction.ADDX,
                    Integer.parseInt(parts[1]));
        }
    }
}

package com.mtab.aventofcode.year2022.day1;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.InputUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day1 implements
        Supplier<OptionalInt> {

    public static void main(final String[] args) throws IOException {
        final Stopwatch sw = Stopwatch.createStarted();
        final List<Elf> elves = Files.lines(
                Path.of(InputUtils.getInputPath("2022/day1/input.txt")))
                        .collect(new InputUtils.StringCollator())
                        .stream()
                        .map((strings) -> strings.stream()
                                .map(Integer::valueOf)
                                .collect(Collectors.toList()))
                        .map(Elf::new)
                        .collect(Collectors.toList());

        final var result = new Day1(elves)
                .get()
                .orElseThrow(RuntimeException::new);

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));

        Preconditions.checkArgument(result == 196804);
    }

    private final List<Elf> elves;

    private Day1(final List<Elf> elves) {
        this.elves = elves;
    }

    @Override
    public OptionalInt get() {
        return OptionalInt.of(
                this.elves.stream()
                        .map(Elf::sumSnacks)
                        .sorted(Comparator.reverseOrder())
                        .mapToInt(i -> i)
                        .limit(3)
                        .sum());
    }

    public static class Elf {
        private final List<Integer> snacks;

        public Elf(final List<Integer> snacks) {
            this.snacks = List.copyOf(snacks);
        }

        public int sumSnacks() {
            return this.snacks.stream()
                    .mapToInt(c -> c)
                    .sum();
        }
    }
}

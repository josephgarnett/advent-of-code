package com.mtab.adventofcode.year2022.day1;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.InputUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day1 implements
        Supplier<OptionalInt> {
    public static List<Elf> getInput() throws IOException, URISyntaxException {
        return InputUtils.readLines("2022/day1/input.txt")
                .collect(new InputUtils.StringCollator())
                .stream()
                .map((strings) -> strings.stream()
                        .map(Integer::valueOf)
                        .collect(Collectors.toList()))
                .map(Elf::new)
                .collect(Collectors.toList());
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2022/day1",
                () -> new Day1(Day1.getInput())
                            .get()
                            .orElseThrow(RuntimeException::new));

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

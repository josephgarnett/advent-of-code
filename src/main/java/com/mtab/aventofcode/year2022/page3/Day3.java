package com.mtab.aventofcode.year2022.page3;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.mtab.aventofcode.utils.InputUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3 implements Function<List<Day3.Backpack>, Long> {
    private static List<Backpack> getInput() throws IOException {
        return Files.lines(
                        Path.of(InputUtils.getInputPath("2022/day3/input.txt")))
                .map(Backpack::of)
                .collect(Collectors.toList());
    }

    public static void main(final String[] args) throws IOException {
        final Stopwatch sw = Stopwatch.createStarted();

        final var result = new Day3().apply(Day3.getInput());

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));

        Preconditions.checkArgument(result == 10835);
    }

    @Override
    public Long apply(List<Backpack> backpacks) {
        return backpacks
                .stream()
                .mapToLong(Backpack::getPriority)
                .sum();
    }

    static class Backpack {
        public static Backpack of(final String contents) {
            return new Backpack(
                    contents.substring(0, contents.length() / 2).split(""),
                    contents.substring(contents.length() / 2).split(""));
        }

        private final List<String> compartment1;
        private final List<String> compartment2;

        Backpack(
                final String[] compartment1,
                final String[] compartment2) {
            this.compartment1 = Arrays.asList(compartment1);
            this.compartment2 = Arrays.asList(compartment2);
        }

        int getPriority() {

            return Sets.intersection(
                    Set.copyOf(this.compartment1),
                            Set.copyOf(this.compartment2))
                    .stream()
                    .mapToInt(letter -> letter.codePointAt(0))
                    .map(value -> value >= 97 ? value - 96 : value - 64 + 26)
                    .sum();
        }
    }
}

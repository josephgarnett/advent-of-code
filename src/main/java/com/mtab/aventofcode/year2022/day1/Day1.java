package com.mtab.aventofcode.year2022.day1;

import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.models.InputLoader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Day1 implements
        InputLoader<List<Day1.Elf>>,
        Supplier<OptionalInt> {

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final var result = new Day1("2022/day1/input.txt")
                .get()
                .orElseThrow(RuntimeException::new);

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    private final List<Elf> input;

    private Day1(final String resourcePath) {
        this.input = this.getInput(resourcePath);
    }

    @Override
    public OptionalInt get() {
        return this.input.stream()
                .mapToInt(Elf::sumSnacks)
                .max();
    }

    @Override
    public List<Elf> transformResource(BufferedReader br) throws IOException {
        final List<Elf> elves = new ArrayList<>();

        List<Integer> snacks = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (StringUtils.isEmpty(line)) {
                elves.add(new Elf(snacks));
                snacks = new ArrayList<>();
            } else {
                snacks.add(Integer.parseInt(line));
            }
        }

        return elves;
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

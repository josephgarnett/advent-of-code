package com.mtab.aventofcode.year2021.day7;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.mtab.aventofcode.models.InputLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7 implements
        InputLoader<List<Integer>>,
        Supplier<Integer> {

    private static final String INPUT_PATH = "2021/day7/input.txt";

    private final boolean fuelRateConstant;

    private Day7() {
        this(true);
    }
    private Day7(final boolean constantFuelRate) {
        this.fuelRateConstant = constantFuelRate;
    }

    @Override
    public Integer get() {
        final List<Integer> positions = this.getInput(INPUT_PATH);
        final List<Integer> values = this.targetValues(positions);

        return values
                .stream()
                .mapToInt(i -> this.cost(i, positions))
                .min()
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Integer> transformResource(final BufferedReader br) throws IOException {
        final ImmutableList.Builder<Integer> builder = new ImmutableList.Builder<>();

        String line;
        while ((line = br.readLine()) != null) {
            final String[] numbers = line.split(",");

            for (final String n: numbers) {
                builder.add(Integer.parseInt(n));
            }
        }

        return builder.build();
    }

    private int cost(final int targetValue, final List<Integer> positions) {
        return positions
                .stream()
                .mapToInt(p -> this.fuelBurnt(p, targetValue))
                .sum();
    }

    private int fuelBurnt(final int start, final int target) {
        final int delta = Math.abs(start - target);

        if (this.fuelRateConstant) {
            return delta;
        }

        return (delta * (delta + 1)) / 2;
    }

    private List<Integer> targetValues(final List<Integer> numbers) {

        int min = 0;
        int max = 0;
        int acc = 0;
        for (final int v : numbers) {
            acc += v;

            if (v > max) {
                max = v;
            }
        }

        final int average = acc / numbers.size();

        return IntStream.range(0, numbers.size() / 2)
                .boxed()
                .map(i -> i % 2 == 0 ? average - i : average + 1)
                .collect(Collectors.toList());
    }

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final int result = new Day7(false).get();

        // Preconditions.checkArgument(result == 37);
        // Preconditions.checkArgument(result == 323647);
        Preconditions.checkArgument(result == 87640209);

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

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
        final List<Integer> sorted = new ArrayList<>(numbers);
        sorted.sort(Comparator.comparingInt(a -> a));

        final int[][] counts = new int[sorted.get(sorted.size() - 1)][2];

        for (int i = 0; i < counts.length; ++i) {
            counts[i][0] = i;
        }

        int acc = 0;
        for (int i = 0; i < sorted.size(); ++i) {
            if (i == sorted.size() - 1) {
                break;
            }

            final int v = sorted.get(i);

            counts[v][1]++;
            acc+=v;
        }

        final int average = acc / sorted.size();

        return Arrays.stream(counts)
                .sorted((a, b) -> {
                    if (b[1] > 0 || a[1] > 0) {
                        return b[1] - a[1];
                    }

                    return Math.abs(average - a[0]) - Math.abs(average - b[0]);
                })
                .map(t -> t[0])
                .collect(Collectors.toList());
    }

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final int result = new Day7(false).get();

        // Preconditions.checkArgument(result == 37);
        // Preconditions.checkArgument(result == 323647);

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

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

    @Override
    public Integer get() {
        final List<Integer> positions = this.getInput(INPUT_PATH);
        final List<Integer> values = this.targetValues(positions);

        return values
                .stream()
                .mapToInt(i -> this.delta(i, positions))
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

    private int delta(final int targetValue, final List<Integer> positions) {
        return positions
                .stream()
                .mapToInt(p -> Math.abs(p - targetValue))
                .sum();
    }

    private List<Integer> targetValues(final List<Integer> numbers) {
        final List<Integer> sorted = new ArrayList<>(numbers);
        sorted.sort(Comparator.comparingInt(a -> a));

        final int[][] counts = new int[sorted.get(sorted.size() - 1)][2];

        for (int i = 0; i < sorted.size(); ++i) {
            if (i == sorted.size() - 1) {
                break;
            }

            final int v = sorted.get(i);

            counts[v][0] = v;
            counts[v][1]++;
        }

        return Arrays.stream(counts)
                .sorted((a, b) -> b[1] - a[1])
                .filter(t -> t[1] > 0)
                .map(t -> t[0])
                .collect(Collectors.toList());
    }

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final int result = new Day7().get();

        // Preconditions.checkArgument(result == 37);
        Preconditions.checkArgument(result == 323647);

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

package com.mtab.adventofcode.year2021.day6;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.mtab.adventofcode.models.InputLoader;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Day6 implements
        InputLoader<List<Integer>>,
        Supplier<Long> {

    private static final String INPUT_PATH = "2021/day6/input.txt";
    private static final int CYCLES = 256;

    private final List<Integer> input;

    Day6() {
        this.input = this.getInput(INPUT_PATH);
    }

    private long compute() {
        long fishes = this.input.size();

        final long[] days = new long[9];
        this.input.forEach(i -> days[i]++);

        for (int i = 0; i < CYCLES; i++) {
            final long fishToAdd = days[0];
            ArrayUtils.shift(days, -1);
            fishes += fishToAdd;
            days[6] += days[days.length -1] = fishToAdd;
        }

        return fishes;
    }

    @Override
    public Long get() {
        return this.compute();
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

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final long result = new Day6().get();

        Preconditions.checkArgument(result == 1632146183902L);

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

package com.mtab.aventofcode.year2021.day6;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.mtab.aventofcode.models.InputLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Day6 implements
        InputLoader<List<Integer>>,
        Supplier<Integer> {

    private static final String INPUT_PATH = "2021/day6/input.txt";
    private static final int CYCLES = 80;
    private static final int SPAWN_CYCLE = 7;
    private static final int FIRST_SPAWN_OFFSET = 2;

    // TODO: int[9] add fish to array pos, each cycle shift down 1 pos - take all in 0 and add to total
    
    private final List<Integer> input;

    Day6() {
        this.input = this.getInput(INPUT_PATH);
    }

    private int spawn(final int t) {
        return this.spawn(t + 1, new AtomicInteger(1), CYCLES);
    }

    // TODO: eliminate recursion
    private int spawn(final int timeToFirstSpawn, final AtomicInteger acc, final int cycles) {
        final int remainingCycles = cycles - timeToFirstSpawn;

        if (remainingCycles < 0) {
            return acc.get();
        }

        final int spawn = remainingCycles / SPAWN_CYCLE + 1;

        acc.addAndGet(spawn);

        for (int i = 0; i < spawn; ++i) {
            this.spawn(
                    SPAWN_CYCLE + FIRST_SPAWN_OFFSET,
                    acc,
                    remainingCycles - (i * SPAWN_CYCLE));
        }

        return acc.get();
    }

    @Override
    public Integer get() {
        return this.input
                .parallelStream()
                .mapToInt(this::spawn)
                .sum();
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
        final int result = new Day6().get();

        Preconditions.checkArgument(result == 360268);

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

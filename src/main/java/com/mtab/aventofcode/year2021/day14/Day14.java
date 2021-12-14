package com.mtab.aventofcode.year2021.day14;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.mtab.aventofcode.models.InputLoader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Day14 implements
        InputLoader<Day14.Schematic>,
        Supplier<Long> {

    private final Map<String, Integer> counts;
    private final Schematic schematic;

    private static final int EXECUTIONS = 40;

    private Day14(final String resourcePath) {
        this.counts = new HashMap<>();
        this.schematic = this.getInput(resourcePath);
    }

    // TODO: too slow for 40 iterations
    private LinkedList<String> polymerize(final LinkedList<String> polymer) {
        final LinkedList<String> result = new LinkedList<>();

        this.counts.clear();

        for (int i = 0; i < polymer.size() - 1; ++i) {
            final var current = polymer.get(i);
            final var next = polymer.get(i + 1);

            final var synthesized = this.schematic.getTransformations().get(current + next);

            result.add(current);
            result.add(synthesized);

            this.counts.compute(current, (key, value) -> value == null ? 1 : value + 1);
            this.counts.compute(synthesized, (key, value) -> value == null ? 1 : value + 1);
        }

        result.add(polymer.getLast());
        this.counts.compute(polymer.getLast(), (key, value) -> value == null ? 1 : value + 1);

        return result;
    }

    @Override
    public Schematic transformResource(final BufferedReader br) throws IOException {
        final String input = br.readLine();
        final List<String> transformations = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            if (StringUtils.isEmpty(line)) {
                continue;
            }

            transformations.add(line.trim());
        }

        return new Schematic(input, transformations);
    }

    @Override
    public Long get() {
        LinkedList<String> formula = new LinkedList<>(Arrays.asList(this.schematic.input.split("")));

        for (int i = 0; i < EXECUTIONS; ++i) {
            formula = this.polymerize(formula);
        }

        final long max = this.counts.values()
                .stream()
                .mapToLong(Integer::intValue)
                .max()
                .orElse(0);
        final long min = this.counts.values()
                .stream()
                .mapToLong(Integer::intValue)
                .min()
                .orElse(0);

        return max - min;
    }

    public static void main(final String... args) {
        final var sw = Stopwatch.createStarted();
        final long result = new Day14("2021/day14/input.txt").get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    public static class Schematic {
        private final String input;
        private final Map<String, String> transformations;

        public Schematic(
                final String input,
                final List<String> transformations) {
            this.input = input;

            final ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<>();
            transformations.forEach(t -> {
                final var parts = t.split("\\s+->\\s+");

                builder.put(parts[0], parts[1]);
            });

            this.transformations = builder.build();
        }

        public String getInput() {
            return this.input;
        }

        public Map<String, String> getTransformations() {
            return this.transformations;
        }
    }
}

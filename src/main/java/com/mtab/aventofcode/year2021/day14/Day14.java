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

    private final Map<String, Long> counts;
    private final Schematic schematic;

    private static final int EXECUTIONS = 40;

    private Day14(final String resourcePath) {
        this.counts = new HashMap<>();
        this.schematic = this.getInput(resourcePath);
    }

    private void resetCounts() {
        this.counts.clear();

        for (final String c: this.schematic.input.split("")) {
            this.counts.compute(c, (k, v) -> v == null ? 1 : v + 1);
        }
    }

    private Map<String, Long> polymerize(final String input) {
        final var polymerMap = new HashMap<String, Long>();

        for (int i = 0; i < input.length() - 1; i++) {
            final String first = input.substring(i, i + 1);
            final String second = input.substring(i + 1, i + 2);

            polymerMap.compute(first + second, (key, value) -> value == null ? 1 : value + 1);
        }

        return polymerMap;
    }

    private Map<String, Long> polymerize(final Map<String, Long> polymerMap) {
        final Map<String, Long> result = new HashMap<>(polymerMap);

        for (final Map.Entry<String, Long> e: polymerMap.entrySet()) {
            final String pair = e.getKey();
            final long value = e.getValue();

            if (value == 0) {
                continue;
            }

            final String synthesized = this.schematic.getTransformations().get(pair);

            this.counts.compute(synthesized, (k, v) -> v == null ? value : v + value);

            result.compute(pair, (k, v) -> v == null ? 0 : v - value);
            result.compute(pair.charAt(0) + synthesized, (k, v) -> v == null ? value : v + value);
            result.compute(synthesized + pair.charAt(1), (k, v) -> v == null ? value : v + value);
        }

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
        Map<String, Long> formula = this.polymerize(this.schematic.input);

        this.resetCounts();

        for (int i = 0; i < EXECUTIONS; ++i) {
            formula = this.polymerize(formula);
        }

        final long max = this.counts.values()
                .stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);
        final long min = this.counts.values()
                .stream()
                .mapToLong(Long::longValue)
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

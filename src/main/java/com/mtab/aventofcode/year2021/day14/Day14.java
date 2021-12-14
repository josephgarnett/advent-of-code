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

    private static final int EXECUTIONS = 10;

    private Day14(final String resourcePath) {
        this.counts = new HashMap<>();
        this.schematic = this.getInput(resourcePath);
    }

    private Map<String, Integer> polymerize(final String input) {
        final var polymerMap = new HashMap<String, Integer>();

        for (int i = 0; i < input.length() - 1; i++) {
            final String first = input.substring(i, i + 1);
            final String second = input.substring(i + 1, i + 2);

            polymerMap.compute(first + second, (key, value) -> value == null ? 1 : value + 1);
        }

        return polymerMap;
    }
    // TODO: getting there - need to account for last character of input if its odd
    private Map<String, Integer> polymerize(final Map<String, Integer> polymerMap) {
        final Map<String, Integer> result = new HashMap<>(polymerMap);

        for (final Map.Entry<String, Integer> e: polymerMap.entrySet()) {
            final String pair = e.getKey();
            final int value = e.getValue();
            final String synthesized = this.schematic.getTransformations().get(pair);

            result.compute(pair.charAt(0) + synthesized, (k, v) -> v == null ? 1 : v + value);
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
        Map<String, Integer> formula = this.polymerize(this.schematic.input);
        System.out.println(formula);
        for (int i = 0; i < EXECUTIONS; ++i) {
            formula = this.polymerize(formula);
            System.out.println(formula);
        }

        final Map<String, Integer> result = new HashMap<>();

        for (final Map.Entry<String, Integer> e: formula.entrySet()) {
            final String key = e.getKey();
            result.compute(key.substring(0, 1), (k, v) -> v == null ? 1 : v + e.getValue());
            result.compute(key.substring(1, 2), (k, v) -> v == null ? 1 : v + e.getValue());
        }

        final long max = result.values()
                .stream()
                .mapToLong(Integer::intValue)
                .max()
                .orElse(0) / 2;
        final long min = result.values()
                .stream()
                .mapToLong(Integer::intValue)
                .min()
                .orElse(0) / 2;

        return max - min;
    }

    public static void main(final String... args) {
        final var sw = Stopwatch.createStarted();
        final long result = new Day14("2021/day14/test.txt").get();

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

package com.mtab.aventofcode.year2021.day12;

import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.models.InputLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day12 implements
        InputLoader<CaveSystem>,
        Supplier<Integer> {

    private final CaveSystem input;

    private Day12(final String resourcePath) {
        this.input = this.getInput(resourcePath);
    }
    @Override
    public CaveSystem transformResource(BufferedReader br) throws IOException {
        final CaveSystem caveSystem = new CaveSystem();

        String line;
        while ((line = br.readLine()) != null) {
            final List<Cave> caves = Arrays.stream(line.split("-"))
                    .map(n -> Optional.ofNullable(caveSystem.getCave(n))
                            .orElse(new Cave(n)))
                    .collect(Collectors.toList());

            caves.get(0).addConnection(caves.get(1));

            caveSystem.addAllCaves(caves);
        }
        return caveSystem;
    }

    @Override
    public Integer get() {
        this.input.map();

        return this.input.getPathCount();
    }

    public static void main(final String... args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final int result = new Day12("2021/day12/input.txt").get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

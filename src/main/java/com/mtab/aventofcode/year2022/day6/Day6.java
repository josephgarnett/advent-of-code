package com.mtab.aventofcode.year2022.day6;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.InputUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 implements Function<String, Integer> {
    private static final int LENGTH = 14;

    private static String getInput() throws IOException {
        return Files.lines(
                        Path.of(InputUtils.getInputPath("2022/day6/input.txt")))
                .collect(Collectors.joining());
    }

    public static void main(final String[] args) throws IOException {
        final Stopwatch sw = Stopwatch.createStarted();

        final var result = new Day6().apply(Day6.getInput());

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));

        Preconditions.checkArgument(result == 2950);
    }

    @Override
    public Integer apply(final String signal) {
        return IntStream.range(0, signal.length() - LENGTH)
                .boxed()
                .filter(i -> signal.substring(i, Math.min(i + LENGTH, signal.length()))
                            .chars()
                            .boxed()
                            .collect(Collectors.toSet())
                            .size() > LENGTH - 1)
                .findFirst()
                .map(n -> n + LENGTH)
                .orElse(-1);
    }
}

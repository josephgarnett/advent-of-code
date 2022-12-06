package com.mtab.aventofcode.year2022.day6;

import com.google.common.base.Preconditions;
import com.mtab.aventofcode.Application;
import com.mtab.aventofcode.utils.InputUtils;
import com.mtab.aventofcode.year2022.Challenge2022;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2022/day6",
                () -> new Day6().apply(Day6.getInput()));

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
                            .size() == LENGTH)
                .findFirst()
                .map(n -> n + LENGTH)
                .orElse(-1);
    }
}

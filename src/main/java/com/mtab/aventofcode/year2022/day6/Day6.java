package com.mtab.aventofcode.year2022.day6;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.InputUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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


        for (int i = 0; i < signal.length(); ++i) {;
            final String behind = signal.substring(Math.max(i - (LENGTH - 1), 0), i + 1);
            final String ahead = signal.substring(i, Math.min(i + LENGTH, signal.length()));
            final BitSet bitSet = new BitSet();

            behind.chars().forEach(bitSet::set);

            if (bitSet.cardinality() > LENGTH - 1) {
                return i;
            }

            bitSet.clear();

            ahead.chars().forEach(bitSet::set);

            if (bitSet.cardinality() > LENGTH - 1) {
                return i + ahead.length();
            }
        }

        return -1;
    }
}

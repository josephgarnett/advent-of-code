package com.mtab.adventofcode.year2021.day1;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.mtab.adventofcode.models.InputListLoader;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Day1 implements
        InputListLoader<Integer>,
        Supplier<Integer> {
    private final boolean compareCondensed;

    private Day1() {
        this.compareCondensed = false;
    }

    private Day1(final boolean compareCondensed) {
        this.compareCondensed = compareCondensed;
    }

    @Override
    public Integer get() {
        final List<Integer> input = this.getInput("2021/day1/input.txt");

        if (this.compareCondensed) {
            return this.compareCondensedList(input);
        }

        return this.compareFlatList(input);
    }

    @Override
    public Integer parseLine(final String line, final int index) {
        return Integer.parseInt(line);
    }

    private Integer compareFlatList(final List<Integer> input) {
        int count = 0;
        for (int i = 1; i < input.size(); ++i) {
            final int current = input.get(i);
            final int previous = input.get(i - 1);

            if (current > previous) {
                count++;
            }
        }

        return count;
    }

    private Integer compareCondensedList(final List<Integer> input) {
        final ImmutableList.Builder<Integer> condensed = new ImmutableList.Builder<>();

        for (int i = 0; i < input.size(); ++i) {
            try {
                condensed.add(input.get(i) + input.get(i + 1) + input.get(i + 2));
            } catch (final ArrayIndexOutOfBoundsException e) {
                break;
            }

        }

        return this.compareFlatList(condensed.build());
    }

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final Integer flatResult = new Day1().get();
        Preconditions.checkArgument(flatResult == 1581);
        System.out.println(flatResult);

        final Integer condensedResult = new Day1(true).get();
        Preconditions.checkArgument(condensedResult == 1618);
        System.out.println(condensedResult);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

package com.mtab.aventofcode.year2021.day1;

import com.google.common.collect.ImmutableList;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class Day1 implements Supplier<Integer> {
    private final boolean compareCondensed;

    private Day1() {
        this.compareCondensed = false;
    }

    private Day1(final boolean compareCondensed) {
        this.compareCondensed = compareCondensed;
    }

    @Override
    public Integer get() {
        if (this.compareCondensed) {
            return this.compareCondensedList(this.INPUT);
        }

        return this.compareFlatList(this.INPUT);
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

    private static List<Integer> getInput() {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final String path = Objects.requireNonNull(classloader.getResource("2021/day1/input.txt")).getPath();
        final File file = new File(path);
        final ImmutableList.Builder<Integer> builder = new ImmutableList.Builder<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.add(Integer.parseInt(line));
            }

            return builder.build();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Integer> INPUT = Day1.getInput();

    public static void main(final String[] args) {
        final Integer flatResult = new Day1().get();
        System.out.println(flatResult);

        final Integer condensedResult = new Day1(true).get();
        System.out.println(condensedResult);
    }
}

package com.mtab.adventofcode.year2021.day9;

import com.google.common.base.Stopwatch;
import com.mtab.adventofcode.models.InputLoader;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Day9 implements
        InputLoader<int[][]>,
        Supplier<Long> {

    private final int[][] input;

    public Day9(final String resourcePath) {
        this.input = this.getInput(resourcePath);
    }

    @Override
    public Long get() {
        final List<int[]> lowpoints = new ArrayList<>();

        for (int i = 0; i < this.input.length; ++i) {
            for (int j = 0; j < this.input[i].length; ++j) {
                if (this.isLowpoint(this.input, j, i)) {
                    lowpoints.add(new int[] { j, i });
                }
            }
        }

        /* return lowpoints.stream()
                .mapToInt(Integer::intValue)
                .sum(); */

        return this.getBasinProduct(this.input, lowpoints);
    }

    private int riskAdjustment(final int initial) {
        return initial + 1;
    }

    private long getBasinProduct(final int[][] grid, final List<int[]> lowpoints) {
        return lowpoints
                .stream()
                .map(lowpoint -> this.basinPoints(grid, lowpoint))
                .map(List::size)
                .sorted(Collections.reverseOrder())
                .limit(3)
                .reduce(1, (acc, e) -> acc * e);
    }

    // does not include original start point
    private List<Integer> basinPoints(final int[][] grid, final int[] pos) {
        final int x = pos[0];
        final int y = pos[1];
        final int target = grid[y][x];

        final Optional<Integer> north = this.accessGrid(grid, x, y-1);
        final Optional<Integer> east = this.accessGrid(grid, x + 1, y);
        final Optional<Integer> south = this.accessGrid(grid, x, y + 1);
        final Optional<Integer> west = this.accessGrid(grid, x - 1, y);
        final List<Integer> result = new ArrayList<>();

        if (target == -1) {
            return result;
        }

        grid[y][x] = -1;
        result.add(target);

        /* System.out.println("");
        System.out.println(Arrays.deepToString(grid)
                .replace("], ", "]\n")
                .replace("[", "")
                .replace("]", "")
                .replace("-1", "@"));
        System.out.println(""); */

        if (this.checkValue(target, north)) {
            result.addAll(this.basinPoints(grid, new int[]{ x, y -1 }));
        }

        if (this.checkValue(target, east)) {
            result.addAll(this.basinPoints(grid, new int[]{ x + 1, y }));
        }

        if (this.checkValue(target, south)) {
            result.addAll(this.basinPoints(grid, new int[]{ x, y + 1 }));
        }

        if (this.checkValue(target, west)) {
            result.addAll(this.basinPoints(grid, new int[]{ x - 1, y }));
        }

        return result;
    }

    private boolean checkValue(final int value, final Optional<Integer> test) {
        return value != -1 && test.isPresent() && test.get() > value && test.get() != 9;
    }

    private boolean isLowpoint(final int[][] grid, int x, int y) {
        final int target = grid[y][x];

        final Optional<Integer> north = this.accessGrid(grid, x, y-1);
        final Optional<Integer> east = this.accessGrid(grid, x + 1, y);
        final Optional<Integer> south = this.accessGrid(grid, x, y + 1);
        final Optional<Integer> west = this.accessGrid(grid, x - 1, y);

        return Stream.of(north, east, south, west)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .allMatch(n -> target < n);
    }

    private Optional<Integer> accessGrid(final int[][] grid, final int x, final int y) {
        try {
            return Optional.of(grid[y][x]);
        } catch (final ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public int[][] transformResource(final BufferedReader br) throws IOException {
        int [][] result = new int[0][0];
        String line;
        while ((line = br.readLine()) != null) {
            final String[] numbers = line.split("");

            int[] values = new int[0];

            for (final String n: numbers) {
                values = ArrayUtils.add(values, Integer.parseInt(n));
            }

            result = ArrayUtils.add(result, values);
        }

        return result;
    }

    public static void main(final String... args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final long result = new Day9("2021/day9/input.txt").get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

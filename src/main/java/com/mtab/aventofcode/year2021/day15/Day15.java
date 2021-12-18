package com.mtab.aventofcode.year2021.day15;

import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.models.InputLoader;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class Day15 implements
        InputLoader<int[][]>,
        Supplier<Integer> {

    private static final int GRID_SIZE = 100;

    private final int[][] grid;
    private final Map<Point2D, Double> unvisited;
    private final AtomicReference<Point2D> pointRef = new AtomicReference<>(new Point2D.Double());

    private Day15(final String resourcePath) {
        this.grid = this.getInput(resourcePath);
        this.unvisited = new HashMap<>();
    }

    @Override
    public Integer get() {
        return this.pathfinder();
    }

    private Integer pathfinder() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                unvisited.put(new Point2D.Double(j, i), Double.POSITIVE_INFINITY);
            }
        }

        final Point2D start = new Point2D.Double(0, 0);
        final Point2D destination = new Point2D.Double(GRID_SIZE - 1, GRID_SIZE - 1);

        unvisited.put(start, 0d);

        return this.pathfinder(start, destination);
    }
    private Integer pathfinder(
            final Point2D c,
            final Point2D destination) {
        Point2D current = c;

        while(current != null) {
            final double currentValue = unvisited.get(current);
            // north
            this.pointRef.get().setLocation(current.getX(), current.getY() - 1);
            if (this.pointRef.get().getY() > -1 && this.pointRef.get().getY() < GRID_SIZE) {
                final var distance = this.grid[(int)this.pointRef.get().getY()][(int)this.pointRef.get().getX()];
                unvisited.computeIfPresent(this.pointRef.get(), (k, v) -> Math.min(v, distance + currentValue));
            }

            // east
            this.pointRef.get().setLocation(current.getX() + 1, current.getY());
            if (this.pointRef.get().getX() > -1 && this.pointRef.get().getX() < GRID_SIZE) {
                final var distance = this.grid[(int)this.pointRef.get().getY()][(int)this.pointRef.get().getX()];
                unvisited.computeIfPresent(this.pointRef.get(), (k, v) -> Math.min(v, distance + currentValue));
            }

            // south
            this.pointRef.get().setLocation(current.getX(), current.getY() + 1);
            if (this.pointRef.get().getY() > -1 && this.pointRef.get().getY() < GRID_SIZE) {
                final var distance = this.grid[(int)this.pointRef.get().getY()][(int)this.pointRef.get().getX()];
                unvisited.computeIfPresent(this.pointRef.get(), (k, v) -> Math.min(v, distance + currentValue));
            }

            // west
            this.pointRef.get().setLocation(current.getX() - 1, current.getY());
            if (this.pointRef.get().getX() > -1 && this.pointRef.get().getX() < GRID_SIZE) {
                final var distance = this.grid[(int)this.pointRef.get().getY()][(int)this.pointRef.get().getX()];
                unvisited.computeIfPresent(this.pointRef.get(), (k, v) -> Math.min(v, distance + currentValue));
            }

            unvisited.remove(current);

            if (current.equals(destination)) {
                return (int)currentValue;
            }

            double minValue = Double.POSITIVE_INFINITY;

            for (final double e: unvisited.values()) {
                if (e < minValue) {
                    minValue = e;
                }
            }

            for (final Map.Entry<Point2D, Double> entry: unvisited.entrySet()) {
                if (entry.getValue() == minValue) {
                    current = entry.getKey();
                    break;
                }
            }

        }

        return -1;
    }

    public static void main(final String... args) {
        final var sw = Stopwatch.createStarted();
        final long result = new Day15("2021/day15/input.txt").get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    public int[][] transformResource(BufferedReader bufferedReader) throws IOException {
        final int[][] grid = new int[GRID_SIZE][GRID_SIZE];

        String line;
        int i = 0;
        while((line = bufferedReader.readLine()) != null) {
            final String[] numbers = line.split("");

            int j = 0;
            for (final String n: numbers) {
                grid[i][j++] = Integer.parseInt(n);
            }

            i++;
        }

        return grid;
    }
}

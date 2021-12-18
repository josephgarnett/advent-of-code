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
    private static final int VIRTUAL_GRID_SIZE = 500;

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

    private int getDistance(final Point2D point) {
        final int x = (int)point.getX();
        final int y = (int)point.getY();

        if (x < GRID_SIZE && y < GRID_SIZE) {
            return this.grid[y][x];
        }

        final int translatedX = x % GRID_SIZE;
        final int translatedY = y % GRID_SIZE;
        final int translationsFactorX = (int)Math.floor((double)x / GRID_SIZE);
        final int translationFactorY = (int)Math.floor((double)y / GRID_SIZE);

        int value = this.grid[translatedY][translatedX];

        if (x != translatedX) {
            value+=translationsFactorX;
        }

        if (y != translatedY) {
            value+=translationFactorY;
        }

        if (value > 9) {
            value-= 9;
        }

        return value;
    }

    private Integer pathfinder() {
        final List<List<Integer>> t = new ArrayList<>();
        for (int i = 0; i < VIRTUAL_GRID_SIZE; ++i) {
            t.add(new ArrayList<>());
            for (int j = 0; j < VIRTUAL_GRID_SIZE; ++j) {
                t.get(i).add(this.getDistance(new Point2D.Double(j, i)));
                unvisited.put(new Point2D.Double(j, i), Double.POSITIVE_INFINITY);
            }
        }

        /* System.out.println("");
        System.out.println(t.toString()
                .replace("], ", "]\n")
                .replace("[", "")
                .replace("]", "")
                .replace("-1", "@"));
        System.out.println(""); */

        final Point2D start = new Point2D.Double(0, 0);
        final Point2D destination = new Point2D.Double(VIRTUAL_GRID_SIZE - 1, VIRTUAL_GRID_SIZE - 1);

        unvisited.put(start, 0d);

        return this.pathfinder(start, destination);
    }
    private Integer pathfinder(
            final Point2D start,
            final Point2D destination) {
        Point2D current = start;

        while((current) != null) {
            final double currentValue = unvisited.get(current);
            // north
            this.pointRef.get().setLocation(current.getX(), current.getY() - 1);
            if (this.pointRef.get().getY() > -1 && this.pointRef.get().getY() < VIRTUAL_GRID_SIZE) {
                unvisited.computeIfPresent(
                        this.pointRef.get(),
                        (k, v) -> Math.min(v, this.getDistance(this.pointRef.get()) + currentValue));
            }

            // east
            this.pointRef.get().setLocation(current.getX() + 1, current.getY());
            if (this.pointRef.get().getX() > -1 && this.pointRef.get().getX() < VIRTUAL_GRID_SIZE) {
                unvisited.computeIfPresent(
                        this.pointRef.get(),
                        (k, v) -> Math.min(v, this.getDistance(this.pointRef.get()) + currentValue));
            }

            // south
            this.pointRef.get().setLocation(current.getX(), current.getY() + 1);
            if (this.pointRef.get().getY() > -1 && this.pointRef.get().getY() < VIRTUAL_GRID_SIZE) {
                unvisited.computeIfPresent(
                        this.pointRef.get(),
                        (k, v) -> Math.min(v, this.getDistance(this.pointRef.get()) + currentValue));
            }

            // west
            this.pointRef.get().setLocation(current.getX() - 1, current.getY());
            if (this.pointRef.get().getX() > -1 && this.pointRef.get().getX() < VIRTUAL_GRID_SIZE) {
                unvisited.computeIfPresent(
                        this.pointRef.get(),
                        (k, v) -> Math.min(v, this.getDistance(this.pointRef.get()) + currentValue));
            }

            unvisited.remove(current);

            if (current.equals(destination)) {
                return (int)currentValue;
            }

            double minValue = Double.POSITIVE_INFINITY;

            // TODO: eliminate these 2 loops

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

    public static class Node implements Map.Entry<Point2D, Double> {
        private final Point2D point;
        private final double value;

        public Node(final Point2D p, final double d) {
            this.point = p;
            this.value = d;
        }

        @Override
        public Point2D getKey() {
            return this.point;
        }

        @Override
        public Double getValue() {
            return this.value;
        }

        @Override
        public Double setValue(Double value) {
            return null;
        }
    }
}

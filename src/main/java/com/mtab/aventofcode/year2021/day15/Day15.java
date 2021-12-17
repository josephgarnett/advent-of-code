package com.mtab.aventofcode.year2021.day15;

import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.models.InputGridLoader;
import com.mtab.aventofcode.models.grid.IntGrid;
import com.mtab.aventofcode.models.grid.IntPoint;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day15 implements
        InputGridLoader<IntPoint, IntGrid>,
        Supplier<Integer> {

    private final IntGrid grid;

    private Day15(final String resourcePath) {
        this.grid = this.getInput(resourcePath);
    }

    @Override
    public IntPoint parseElement(String e, int x, int y) {
        return new IntPoint(Integer.parseInt(e), x, y);
    }

    @Override
    public IntGrid createGrid(List<IntPoint> points) {
        return new IntGrid(points);
    }

    @Override
    public Integer get() {
        final var start = this.grid.getPointAt(0, 0);

        final List<Integer> results = this.pathfinder(
                start,
                this.grid.getPointAt(9, 9));

        return results.size();
    }


    private List<Integer> pathfinder(final IntPoint current, final IntPoint destination) {
        final Map<IntPoint, Double> unvisited = new HashMap<>();

        this.grid.getGrid()
                .values()
                .forEach(p -> unvisited.put(p, Double.POSITIVE_INFINITY));

        unvisited.put(current, 0d);

        return this.pathfinder(current, destination, unvisited, new AtomicInteger(0));
    }
    private List<Integer> pathfinder(
            final IntPoint current,
            final IntPoint destination,
            final Map<IntPoint, Double> unvisited,
            final AtomicInteger risk) {
        final int currentValue = current.getValue().get();
        risk.addAndGet(currentValue);
        final var neighbours = this.grid.getCardinalNeighbours(current)
                .stream()
                .filter(unvisited::containsKey)
                .peek(p -> unvisited.compute(p, (k, v) -> Math.min(v, p.getValue().get() + unvisited.get(current))))
                .collect(Collectors.toList());

        unvisited.remove(current);

        if (current == destination) {
            return List.of(risk.get());
        }

        final double minValue = neighbours
                .stream()
                .map(unvisited::get)
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0);

        return unvisited
                .entrySet()
                .stream()
                .filter(e -> neighbours.contains(e.getKey()) && e.getValue() == minValue)
                .map(Map.Entry::getKey)
                .flatMap(p -> this.pathfinder(
                        p,
                        destination,
                        new HashMap<>(unvisited),
                        new AtomicInteger(risk.get()))
                        .stream())
                .collect(Collectors.toList());
    }

    public static void main(final String... args) {
        final var sw = Stopwatch.createStarted();
        final long result = new Day15("2021/day15/test.txt").get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    private static class Path {
        private final List<IntPoint> points;
        private final boolean isComplete;

        public Path(final List<IntPoint> points, final boolean isComplete) {
            this.points = List.copyOf(points);
            this.isComplete = isComplete;
        }

        public boolean isComplete() {
            return this.isComplete;
        }

        public List<IntPoint> getPoints() {
            return this.points;
        }

        public int riskIndex() {
            return this.points
                    .stream()
                    .mapToInt(p -> p.getValue().get())
                    .sum();
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("points", this.getPoints())
                    .add("isComplete", this.isComplete)
                    .toString();
        }
    }
}

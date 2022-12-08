package com.mtab.adventofcode.year2021.day11;

import com.google.common.base.Stopwatch;
import com.mtab.adventofcode.models.InputGridLoader;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class Day11 implements
        InputGridLoader<Octopus, OctopusGrid>,
        Supplier<Long> {

    private static final int CYCLES = 1000;

    private final OctopusGrid input;
    private final AtomicLong flashes = new AtomicLong(0);

    public Day11(final String resourcePath) {
        this.input = this.getInput(resourcePath);
    }

    private int compute(final OctopusGrid grid) {
        return grid.computeFlashes();
    }

    @Override
    public Long get() {
        for (int i = 0; i < CYCLES; i++) {
            final int flashes = this.compute(this.input);

            this.flashes.addAndGet(flashes);

            if (flashes == this.input.size()) {
                System.out.println("Synchronization: " + (i + 1));
                break;
            }
        }

        return this.flashes.get();
    }

    @Override
    public Octopus parseElement(final String e, final int x, final int y) {
        return new Octopus(Integer.parseInt(e), new Point2D.Float(x, y));
    }

    @Override
    public OctopusGrid createGrid(List<Octopus> grid) {
        return new OctopusGrid(grid);
    }

    public static void main(final String... args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final long result = new Day11("2021/day11/input.txt").get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

package com.mtab.adventofcode.year2021.day11;

import com.mtab.adventofcode.models.grid.GridPoint;

import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicInteger;

public class Octopus implements GridPoint<AtomicInteger> {

    private final AtomicInteger value;
    private final Point2D position;

    public Octopus(final int value, final Point2D position) {
        this.value = new AtomicInteger(value);
        this.position = position;
    }

    public int flash(final OctopusGrid grid) {
        final int value = this.value.incrementAndGet();

        if (value == 10) {
            return grid.getNeighbours(this)
                    .stream()
                    .mapToInt(o -> o.flash(grid))
                    .sum() + 1;
        }

        return 0;
    }

    public void reset() {
        if (this.getValue().get() >= 10) {
            this.getValue().set(0);
        }
    }

    @Override
    public AtomicInteger getValue() {
        return this.value;
    }

    @Override
    public Point2D getPosition() {
        return this.position;
    }
}

package com.mtab.adventofcode.models.grid;

import com.google.common.collect.ImmutableMap;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

public class IntGrid implements Grid<IntPoint> {
    private final Map<Point, IntPoint> gridMap;

    public IntGrid(final List<IntPoint> points) {
        this.gridMap = this.populate(points);
    }

    @Override
    public Map<Point, IntPoint> getGrid() {
        return this.gridMap;
    }

    @Override
    public Map<Point, IntPoint> populate(List<IntPoint> points) {
        final ImmutableMap.Builder<Point, IntPoint> builder = new ImmutableMap.Builder<>();

        for (final IntPoint o: points) {
            builder.put(o.getPosition(), o);
        }

        return builder.build();
    }
}

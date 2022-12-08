package com.mtab.adventofcode.year2021.day11;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.mtab.adventofcode.models.grid.Grid;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OctopusGrid implements Grid<Octopus> {
    private final Map<Point2D, Octopus> gridMap;

    private Set<Point2D> nextPoints = new HashSet<>();

    public OctopusGrid(final List<Octopus> grid) {
        this.gridMap = this.populate(grid);
        this.nextPoints.add(new Point2D.Float(0, 0));
    }

    public int computeFlashes() {
        final int value = this.gridMap.values()
                .stream()
                .mapToInt(o -> o.flash(this))
                .sum();

        this.gridMap.values().forEach(Octopus::reset);

        return value;
    }

    @Override
    public Map<Point2D, Octopus> populate(
            final List<Octopus> points) {
        final ImmutableMap.Builder<Point2D, Octopus> builder = new ImmutableMap.Builder<>();

        for (final Octopus o: points) {
            builder.put(o.getPosition(), o);
        }

        return builder.build();
    }

    @Override
    public Map<Point2D, Octopus> getGrid() {
        return this.gridMap;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("grid", this.gridMap)
                .toString();
    }
}

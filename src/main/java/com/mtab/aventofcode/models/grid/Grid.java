package com.mtab.aventofcode.models.grid;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Grid<ElementType extends GridPoint<?>> {

    Map<Point2D, ElementType> getGrid();
    Map<Point2D, ElementType> populate(final List<ElementType> points);

    default Optional<ElementType> getGridElement(final int x, final int y) {
        return this.getGridElement(new Point2D.Float(x, y));
    }
    default Optional<ElementType> getGridElement(final Point2D point) {
        return Optional.ofNullable(this.getGrid().get(point));
    }

    default List<ElementType> getNeighbours(ElementType point) {
        final int x = (int) point.getPosition().getX();
        final int y = (int) point.getPosition().getY();
        return Stream.of(
                        new Point2D.Double(x, (double)y - 1), // N
                        new Point2D.Double((double)x + 1, (double)y - 1), // NE
                        new Point2D.Double((double)x + 1, y), // E
                        new Point2D.Double((double)x + 1, (double)y + 1), // SE
                        new Point2D.Double(x, (double)y + 1), // S
                        new Point2D.Double((double)x - 1, (double)y + 1), // SW
                        new Point2D.Double((double)x - 1, y), // W
                        new Point2D.Double((double)x - 1, (double)y - 1)) // NW
                .map(this::getGridElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    default List<ElementType> getCardinalNeighbours(ElementType point) {
        final int x = (int) point.getPosition().getX();
        final int y = (int) point.getPosition().getY();
        return Stream.of(
                        new Point2D.Double(x, (double)y - 1), // N
                        new Point2D.Double((double)x + 1, y), // E
                        new Point2D.Double(x, (double)y + 1), // S
                        new Point2D.Double((double)x - 1, y)) // W
                .map(this::getGridElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    default ElementType getPointAt(final int x, final int y) {
        return this.getGrid().get(new Point2D.Double(x, y));
    }


    default int size() {
        return this.getGrid().size();
    }
}

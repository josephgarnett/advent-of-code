package io.garnett.adventofcode.models.grid;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Grid<ElementType extends GridPoint<?>> {

    Map<Point, ElementType> getGrid();
    Map<Point, ElementType> populate(final List<ElementType> points);

    default Optional<ElementType> getGridElement(final int x, final int y) {
        return this.getGridElement(new Point(x, y));
    }
    default Optional<ElementType> getGridElement(final Point point) {
        return Optional.ofNullable(this.getGrid().get(point));
    }

    default List<ElementType> getNeighbours(ElementType point) {
        final int x = (int) point.getPosition().getX();
        final int y = (int) point.getPosition().getY();
        return Stream.of(
                        new Point(x, y - 1), // N
                        new Point(x + 1, y - 1), // NE
                        new Point(x + 1, y), // E
                        new Point(x + 1, y + 1), // SE
                        new Point(x, y + 1), // S
                        new Point(x - 1, y + 1), // SW
                        new Point(x - 1, y), // W
                        new Point(x - 1, y - 1)) // NW
                .map(this::getGridElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    default List<ElementType> getCardinalNeighbours(ElementType point) {
        final int x = (int) point.getPosition().getX();
        final int y = (int) point.getPosition().getY();
        return Stream.of(
                        new Point(x, y - 1), // N
                        new Point(x + 1, y), // E
                        new Point(x, y + 1), // S
                        new Point(x - 1, y)) // W
                .map(this::getGridElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    default ElementType getPointAt(final int x, final int y) {
        return this.getGrid().get(new Point(x, y));
    }


    default int size() {
        return this.getGrid().size();
    }
}

package com.mtab.aventofcode.year2021.day13;

import com.mtab.aventofcode.models.grid.Grid;
import com.mtab.aventofcode.models.grid.GridPoint;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Paper implements Grid<Paper.Point> {

    private final Set<Point> points;

    public Paper(List<Paper.Point> points) {
        this(Set.copyOf(points));
    }
    public Paper(final Set<Paper.Point> points) {
        this.points = Set.copyOf(points);
    }

    public Paper fold(final FoldInstruction fold) {
        return new Paper(this.points
                .stream()
                .flatMap(p -> Stream.of(p, p.mirror(fold)))
                .filter(p -> {
                    if (fold.getAxis() == FoldAxis.HORIZONTAL) {
                        return p.getPosition().getY() <= fold.getFoldLine().getY();
                    }

                    return p.getPosition().getX() <= fold.getFoldLine().getX();
                })
                .collect(Collectors.toSet()));
    }

    public int countPoints() {
        return this.points.size();
    }

    public String print() {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 11; ++j) {
                final Point2D p = new Point2D.Float(j, i);

                if (this.points.contains(new Point(true, p))) {
                    sb.append("#");
                } else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public Map<Point2D, Point> getGrid() {
        return null;
    }

    @Override
    public Map<Point2D, Point> populate(List<Point> points) {
        return null;
    }

    public enum FoldAxis { HORIZONTAL, VERTICAL };

    public static class FoldInstruction {
        private final Point2D foldLine;
        private final FoldAxis axis;

        public FoldInstruction(final FoldAxis axis, final Point2D value) {
            this.axis = axis;
            this.foldLine = value;
        }

        public FoldAxis getAxis() {
            return this.axis;
        }

        public Point2D getFoldLine() {
            return this.foldLine;
        }
    }

    public static class Point implements GridPoint<Boolean> {
        private final boolean value;
        private final Point2D point;

        public Point(final boolean value, final Point2D point) {
            this.value = value;
            this.point = point;
        }

        public Point mirror(final FoldInstruction fold) {
            final Point2D p = this.getPosition();

            if (fold.getAxis() == FoldAxis.VERTICAL) {
                return new Point(
                        this.getValue(),
                        new Point2D.Float(
                                (float)(Math.abs(fold.getFoldLine().getX() - p.getX())),
                                (float)this.getPosition().getY()));
            }

            return new Point(
                    this.getValue(),
                    new Point2D.Float(
                            (float)this.getPosition().getX(),
                            (float)(Math.abs(fold.getFoldLine().getY() - p.getY()))));
        }

        @Override
        public Boolean getValue() {
            return this.value;
        }

        @Override
        public Point2D getPosition() {
            return this.point;
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }

            if (that instanceof Point) {
                return this.point.equals(((Point) that).point);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.point, this.value);
        }
    }
}

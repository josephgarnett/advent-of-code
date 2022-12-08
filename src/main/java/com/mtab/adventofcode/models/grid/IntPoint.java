package com.mtab.adventofcode.models.grid;

import com.google.common.base.MoreObjects;

import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class IntPoint implements GridPoint<AtomicInteger> {

    private final AtomicInteger value;
    private final Point2D position;

    public IntPoint(final int value, final int x, final int y) {
        this.value = new AtomicInteger(value);
        this.position = new Point2D.Double(x, y);
    }

    @Override
    public AtomicInteger getValue() {
        return this.value;
    }

    @Override
    public Point2D getPosition() {
        return this.position;
    }

    private boolean equalTo(final IntPoint that) {
        if (!this.getValue().equals(that.getValue())) {
            return false;
        }

        if (!this.getPosition().equals(that.getPosition())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        if (that instanceof IntPoint) {
            return this.equalTo((IntPoint) that);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPosition(), this.getValue());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", this.getValue())
                .add("position", this.getPosition())
                .toString();
    }
}

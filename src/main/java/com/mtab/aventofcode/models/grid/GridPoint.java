package com.mtab.aventofcode.models.grid;

import java.awt.geom.Point2D;

public interface GridPoint<E> {
    E getValue();
    Point2D getPosition();
}

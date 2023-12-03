package com.mtab.adventofcode.models.grid;

import java.awt.*;

public interface GridPoint<E> {
    E getValue();
    Point getPosition();
}

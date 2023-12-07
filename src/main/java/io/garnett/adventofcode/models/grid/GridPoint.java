package io.garnett.adventofcode.models.grid;

import java.awt.*;

public interface GridPoint<E> {
    E getValue();
    Point getPosition();
}

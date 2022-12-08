package com.mtab.adventofcode.models;

import com.google.common.collect.ImmutableList;
import com.mtab.adventofcode.models.grid.Grid;
import com.mtab.adventofcode.models.grid.GridPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public interface InputGridLoader<E extends GridPoint<?>, T extends Grid<E>> extends InputLoader<T> {

    E parseElement(final String e, final int x, final int y);

    T createGrid(final List<E> points);

    default String[] split(final String line) {
        return line.split("");
    }

    @Override
    default public T transformResource(final BufferedReader br) throws IOException {
        final ImmutableList.Builder<E> result = new ImmutableList.Builder<>();

        String line;
        int y = 0;
        while ((line = br.readLine()) != null) {
            final String[] elements = this.split(line);
            final ImmutableList.Builder<E> lineBuilder = new ImmutableList.Builder<>();

            int x = 0;
            for (final String e: elements) {
                result.add(this.parseElement(e, x++, y));
            }

            y++;
        }

        return this.createGrid(result.build());
    }

}

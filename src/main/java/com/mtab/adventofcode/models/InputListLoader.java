package com.mtab.adventofcode.models;

import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public interface InputListLoader<R> extends InputLoader<List<R>> {
    R parseLine(final String line, final int i);

    @Override
    default List<R> transformResource(final BufferedReader bufferedReader) throws IOException {
        final ImmutableList.Builder<R> builder = new ImmutableList.Builder<>();

        String line;
        int index = 0;
        while ((line = bufferedReader.readLine()) != null) {
            builder.add(this.parseLine(line, index));
            ++index;
        }

        return builder.build();
    }
}

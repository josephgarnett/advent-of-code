package com.mtab.aventofcode.models;

import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public interface InputListLoader<R> extends InputLoader<List<R>> {
    R parseLine(final String line, final int i);

    @Override
    default List<R> getInput(final String filePath) {
        final ImmutableList.Builder<R> builder = new ImmutableList.Builder<>();

        try (final BufferedReader br = new BufferedReader(
                new FileReader(
                        this.getResourceFile(filePath)))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                builder.add(this.parseLine(line, index));
                ++index;
            }

            return builder.build();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.mtab.aventofcode.models;

import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;

public interface IInputLoader <R> {
    R parseLine(final String line, final int i);

    default List<R> getInput(final String filePath) {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final String path = Objects.requireNonNull(classloader.getResource(filePath)).getPath();
        final File file = new File(path);
        final ImmutableList.Builder<R> builder = new ImmutableList.Builder<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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

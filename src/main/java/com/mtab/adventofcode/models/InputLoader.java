package com.mtab.adventofcode.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public interface InputLoader<R> {

    R transformResource(final BufferedReader bufferedReader) throws IOException;

    default R getInput(final String filePath) {
        try (final BufferedReader br = new BufferedReader(
                new FileReader(
                        this.getResourceFile(filePath)))) {
            return this.transformResource(br);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    default File getResourceFile(final String resourcePath) {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final String path = Objects.requireNonNull(classloader.getResource(resourcePath)).getPath();

        return new File(path);
    }
}

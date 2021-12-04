package com.mtab.aventofcode.models;

import java.io.File;
import java.util.Objects;

public interface InputLoader<R> {

    default File getResourceFile(final String resourcePath) {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final String path = Objects.requireNonNull(classloader.getResource(resourcePath)).getPath();

        return new File(path);
    }

    R getInput(final String filePath);
}

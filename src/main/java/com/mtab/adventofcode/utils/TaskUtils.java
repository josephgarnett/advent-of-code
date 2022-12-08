package com.mtab.adventofcode.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TaskUtils {
    public static String getDescription(final String path) throws IOException {
        return Files.lines(Path.of(InputUtils.getInputPath(path)))
                .filter(StringUtils::isNotEmpty)
                .findFirst()
                .orElse("unknown");
    }
}

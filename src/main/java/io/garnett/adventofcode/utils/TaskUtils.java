package io.garnett.adventofcode.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class TaskUtils {
    public static String getDescription(final String path) throws IOException {
        return InputUtils.readLines(path)
                .filter(StringUtils::isNotEmpty)
                .findFirst()
                .orElse("unknown");
    }
}

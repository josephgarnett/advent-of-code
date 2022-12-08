package com.mtab.adventofcode.year2022.day7;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.CustomCollectors;
import com.mtab.adventofcode.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 implements Function<List<Day7.File>, Long> {
    private static final Pattern COMMAND = Pattern.compile("^\\$\\s+(\\w+)\\s+(.*)$");
    private static final Pattern FILE = Pattern.compile("^(\\d+)\\s+(.*)$");

    private static final int THRESHOLD = 100000;
    private static final long MAX_SPACE = 70000000;
    private static final long REQUIRED_SPACE = 30000000;

    private static List<File> getInput() throws IOException {
        final Stack<String> tags = new Stack<>();
        final List<File> files = new ArrayList<>();

        final Map<Pattern, Consumer<Matcher>> processor = Map.of(
                FILE, (matcher) -> {
                    final long size = Long.parseLong(matcher.group(1));
                    final String name = matcher.group(2);

                    files.add(File.of(name, size, List.copyOf(tags)));
                },
                COMMAND, (matcher) -> {
                    final String command = matcher.group(1);
                    final String dir = matcher.group(2);

                    if (StringUtils.equals("cd", command)) {
                        final String[] parts = dir.split("/");

                        if (parts.length == 0) {
                            tags.push("");
                        }

                        for (final String part: parts) {
                            if (StringUtils.equals(part, "..")) {
                                tags.pop();
                            } else {
                                tags.push(part);
                            }
                        }
                    }
                });

        Files.lines(
                Path.of(InputUtils.getInputPath("2022/day7/input.txt")))
                .forEach(line -> {
                    processor.forEach((key, value) -> {
                        final var matcher = key.matcher(line);

                        if (matcher.matches()) {
                            value.accept(matcher);
                        }
                    });
                });

        return files;
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2022/day7",
                () -> new Day7().apply(Day7.getInput()));

        Preconditions.checkArgument(result == 3866390);
    }

    private long part1(final List<File> files) {
        return files.stream()
                .collect(CustomCollectors.collectFileTagGroups(File::tags))
                .values()
                .stream()
                .mapToLong(group -> group
                        .stream()
                        .mapToLong(File::size)
                        .sum())
                .filter(size -> size <= THRESHOLD)
                .sum();
    }

    private long part2(final List<File> files) {
        final long[] sizes = files.stream()
                .collect(CustomCollectors.collectFileTagGroups(File::tags))
                .values()
                .stream()
                .mapToLong((f) -> f.stream()
                        .mapToLong(File::size)
                        .sum())
                .toArray();

        final long unusedSpace = MAX_SPACE - Arrays.stream(sizes)
                .max()
                .orElseThrow(RuntimeException::new);

        return Arrays.stream(sizes)
                .sorted()
                .filter(t -> t + unusedSpace > REQUIRED_SPACE)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public Long apply(final List<File> files) {
        return this.part2(files);
    }

    record File(String name, long size, Set<String> tags) {
        public static File of(
                final String name,
                final long size,
                final List<String> tags) {

            final StringBuilder sb = new StringBuilder();
            final Set<String> fileTags = new HashSet<>();
            for (final String tag: tags) {
                sb.append(tag)
                        .append("/");

                fileTags.add(sb.toString());
            }

            return new File(name, size, Set.copyOf(fileTags));
        }
    }
}

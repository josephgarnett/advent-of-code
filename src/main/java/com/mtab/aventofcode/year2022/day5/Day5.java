package com.mtab.aventofcode.year2022.day5;

import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5 implements Function<Day5.ShipyardManager, String> {
    private static final Pattern INSTRUCTION = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    static Optional<Stack<String>> getStack(final List<Stack<String>> stacks, final int index) {
        try {
            return Optional.of(stacks.get(index));
        } catch (final IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    private static ShipyardManager getInput() throws IOException {
        final List<Instruction> instructions = Files.lines(
                Path.of(InputUtils.getInputPath("2022/day5/instructions.txt")))
                .map(line -> {
                    final Matcher matcher = INSTRUCTION.matcher(line);

                    if (matcher.matches()) {
                        return new Instruction(
                                Integer.parseInt(matcher.group(2)),
                                Integer.parseInt(matcher.group(3)),
                                Integer.parseInt(matcher.group(1)));
                    }

                    throw new IllegalStateException();
                })
                .toList();
        final List<Stack<String>> stacks = new ArrayList<>();
        Files.lines(
                Path.of(InputUtils.getInputPath("2022/day5/container-layout.txt")))
                .forEach(line -> {
                    if (line.contains("1")) {
                        final var numberOfContainers = line.replace(" ", "").length();

                        for (int i = 0; i < numberOfContainers; ++i) {
                            stacks.add(new Stack<>());
                        }

                        return;
                    }

                    if (line.contains("[")) {
                        for (int i = 0; (i * 3) + i < line.length(); ++i) {
                            final String container = line.substring((i * 3) + i, (i * 3) + i + 3).trim();

                            if (StringUtils.isNotEmpty(container)) {
                                final var stack = Day5.getStack(stacks, i)
                                        .orElse(new Stack<>());

                                stack.push(container
                                        .replace("[", "")
                                        .replace("]", ""));
                                stacks.set(i, stack);
                            }
                        }
                    }
                });

        stacks.forEach(Collections::reverse);

        return new ShipyardManager(stacks, instructions);
    }

    public static void main(final String[] args) throws IOException {
        final Stopwatch sw = Stopwatch.createStarted();

        final var result = new Day5().apply(Day5.getInput());

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    public String apply(final ShipyardManager manager) {
        manager.manage();

        return manager.stacks
                .stream()
                .map(Stack::peek)
                .collect(Collectors.joining());
    }

    record ShipyardManager(List<Stack<String>> stacks, List<Instruction> instructions) {
        public void manage() {
            instructions.forEach((instruction -> instruction.apply(this.stacks)));
        }
    }

    record Instruction(int source, int target, int iterations) implements Function<List<Stack<String>>, Void> {
        private void crateMover9000(final List<Stack<String>> containers) {
            for (int i = 0; i < iterations; ++i) {
                final var container = containers.get(this.source - 1).pop();
                containers.get(this.target - 1).push(container);
            }
        }

        private void crateMover9001(final List<Stack<String>> containers) {
            final List<String> crates = new ArrayList<>();

            for (int i = 0; i < iterations; ++i) {
                crates.add(containers.get(this.source - 1).pop());
            }

            Collections.reverse(crates);
            containers.get(this.target - 1).addAll(crates);
        }

        @Override
        public Void apply(final List<Stack<String>> containers) {

            this.crateMover9001(containers);

            return null;
        }
    }
}

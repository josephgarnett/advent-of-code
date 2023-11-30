package com.mtab.adventofcode.year2015.day1;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day1 implements Function<String, Long> {

    public Long apply(
            @NonNull final String input) {
        return Integer.toUnsignedLong(this.basementPosition(input));
    }

    public int basementPosition(
            @NonNull final String input) {
        final AtomicInteger floor = new AtomicInteger(0);
        final AtomicInteger position = new AtomicInteger(0);

        while (floor.get() != -1) {
            final char action = input.charAt(position.getAndIncrement());

            if ('(' == action) {
                floor.incrementAndGet();
            } else {
                floor.decrementAndGet();
            }

            if (floor.get() == -1) {
                return position.get();
            }
        }

        throw new IllegalStateException();
    }

    public Long location(
            @NonNull final String input) {
        final Map<String, Long> actions = Arrays.stream(input
                        .split(""))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()));
        return actions.getOrDefault("(", 0L) - actions.getOrDefault(")", 0L);
    }

    private static String getInput() throws IOException {
        return InputUtils.readLines(
                        "2015/day1/input.txt")
                .collect(Collectors.joining());
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2015/day1",
                () -> new Day1()
                        .apply(Day1.getInput()));

        Preconditions.checkArgument(result == 1771);
    }
}

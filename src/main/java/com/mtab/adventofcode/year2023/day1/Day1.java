package com.mtab.adventofcode.year2023.day1;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Day1 implements Function<List<String>, Long> {
    @Override
    public Long apply(
            @NonNull final List<String> input) {
        return input.stream()
                .map(t -> Arrays.stream(t
                                .replace("one", "o1e")
                                .replace("two", "t2o")
                                .replace("three", "t3e")
                                .replace("four", "f4r")
                                .replace("five", "f5e")
                                .replace("six", "s6x")
                                .replace("seven", "s7n")
                                .replace("eight", "e8t")
                                .replace("nine", "n9e")
                                .split(""))
                        .filter(u -> u.codePointAt(0) >= 48 && u.codePointAt(0) <= 57)
                        .map(Integer::parseInt)
                        .toList())
                .mapToLong(t -> Long.parseLong(String.format("%s%s", t.get(0), t.get(t.size() - 1))))
                .sum();
    }

    private static List<String> getInput() throws IOException {
        return InputUtils.readLines(
                        "2023/day1/input.txt")
                .toList();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day1",
                () -> new Day1()
                        .apply(Day1.getInput()));

        Preconditions.checkArgument(result == 55386);
    }
}

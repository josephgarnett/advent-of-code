package io.garnett.adventofcode.year2024.day2;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.IntStream;

public class Day2 implements ToLongFunction<List<List<Integer>>> {

    private static final long PART_1_RESULT = 534;
    private static final long PART_2_RESULT = 577;

    private static final int MIN_SAFE_DELTA = 1;
    private static final int MAX_SAFE_DELTA = 3;

    @Override
    public long applyAsLong(
            @NonNull final List<List<Integer>> input) {
        return this.part2(input);
    }

    private long part1(
            @NonNull final List<List<Integer>> input) {
        return input.stream()
                .filter(this.evaluateSafety(false))
                .count();
    }

    private long part2(
            @NonNull final List<List<Integer>> input) {
        return input.stream()
                .filter(this.evaluateSafety(true))
                .count();
    }


    private Predicate<List<Integer>> evaluateSafety(final boolean retry) {
        return report -> {
            var first = report.get(0);
            var second = report.get(1);
            var increasing = first < second;
            var decreasing = first > second;
            var errors = -1;

            if (!increasing && !decreasing) {
                errors = 0;
            }

            for (int i = 1; i < report.size(); i++) {
                if (errors != -1) {
                    break;
                }

                var previous = report.get(i - 1);
                var current = report.get(i);
                var difference = previous - current;

                if (increasing) {
                    if (difference > -MIN_SAFE_DELTA) {
                        errors = i - 1;
                    }

                    if (difference < -MAX_SAFE_DELTA) {
                        errors = i - 1;
                    }
                } else {
                    if (difference < MIN_SAFE_DELTA) {
                        errors = i - 1;
                    }

                    if (difference > MAX_SAFE_DELTA) {
                        errors = i - 1;
                    }
                }
            }

            if (errors > -1 && retry) {
                return IntStream.range(0, report.size())
                        .mapToObj(t -> {
                            var next = new ArrayList<>(report);
                            next.remove(t);

                            return next;
                        })
                        .anyMatch(this.evaluateSafety(false));
            }

            return errors == -1;
        };
    }

    private static List<List<Integer>> getInput() throws IOException {
        return InputUtils.readLines(
                        "2024/day2/input.txt")
                .map(t ->  t.split("\\s+"))
                .map(t -> Arrays.stream(t)
                        .map(Integer::parseInt)
                        .toList())
                .toList();
    }

    public static void main(final String[] args) {
        final var result = Application.challenge(
                "2024/day2",
                () -> new Day2()
                        .applyAsLong(Day2.getInput()));

        Preconditions.checkArgument(result == PART_2_RESULT);
    }
}

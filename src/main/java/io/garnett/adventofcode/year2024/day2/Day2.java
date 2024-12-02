package io.garnett.adventofcode.year2024.day2;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
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
            final int first = report.get(0);
            final int second = report.get(1);
            final boolean increasing = first < second;
            final boolean decreasing = first > second;

            int errors = -1;

            if (!increasing && !decreasing) {
                errors = 0;
            }

            for (int i = 1; i < report.size(); i++) {
                var previous = report.get(i - 1);
                var current = report.get(i);
                var difference = previous - current;

                if (unsafeValue(difference, increasing)) {
                    errors = i - 1;
                }

                if (errors != -1) {
                    break;
                }
            }

            if (errors > -1 && retry) {
                return IntStream.range(0, report.size())
                        .mapToObj(this.removeUnsafeLevel(report))
                        .anyMatch(this.evaluateSafety(false));
            }

            return errors == -1;
        };
    }

    private IntFunction<List<Integer>> removeUnsafeLevel(
            @NonNull final List<Integer> report) {
        return unsafePosition -> {
            final List<Integer> next = new ArrayList<>(report);
            next.remove(unsafePosition);

            return next;
        };
    }

    private boolean unsafeValue(final int value, final boolean increasing) {
        if (increasing) {
            return value > -MIN_SAFE_DELTA || value < -MAX_SAFE_DELTA;
        }

        return value < MIN_SAFE_DELTA || value > MAX_SAFE_DELTA;
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

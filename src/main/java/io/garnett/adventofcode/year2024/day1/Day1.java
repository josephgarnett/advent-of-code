package io.garnett.adventofcode.year2024.day1;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.models.ChallengeResult;
import io.garnett.adventofcode.models.ChallengeSolution;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day1 implements ChallengeSolution<List<ImmutablePair<Integer, Integer>>, Long> {

    private static final long PART_1_RESULT = 1722302;
    private static final long PART_2_RESULT = 20373490;

    @Override
    public ChallengeResult<Long> solve(
            @NonNull final List<ImmutablePair<Integer, Integer>> input) {
        return new ChallengeResult<>(
                this.part2(input),
                () -> IntStream.range(0, 10)
                        .mapToObj(t -> StringUtils.repeat("██", (int)(Math.random() * 30)))
                        .collect(Collectors.joining("\n"))
        );
    }

    private long part2(
            @NonNull final List<ImmutablePair<Integer, Integer>> input) {
        final Map<Integer, List<Integer>> right = input.stream()
                .map(ImmutablePair::getRight)
                .collect(Collectors.groupingBy(Function.identity()));

        return input.stream()
                .map(ImmutablePair::getLeft)
                .mapToInt(t -> t * right.getOrDefault(t, List.of()).size())
                .sum();
    }

    private long part1(
            @NonNull final List<ImmutablePair<Integer, Integer>> input) {
        final List<Integer> left = input.stream()
                .map(ImmutablePair::getLeft)
                .sorted()
                .toList();

        final List<Integer> right = input.stream()
                .map(ImmutablePair::getRight)
                .sorted()
                .toList();

        return IntStream.range(0, Math.min(left.size(), right.size()))
                .map(i -> Math.abs(left.get(i) - right.get(i)))
                .sum();
    }

    private static List<ImmutablePair<Integer, Integer>> getInput() throws IOException {
        return InputUtils.readLines(
                        "2024/day1/input.txt")
                .map(t ->  t.split("\\s+"))
                .map(t -> new ImmutablePair<>(
                        Integer.parseInt(t[0]),
                        Integer.parseInt(t[1])))
                .toList();
    }

    public static void main(final String[] args) {
        final var result = Application.challenge(
                "2024/day1",
                () -> new Day1()
                        .solve(Day1.getInput()));

        Preconditions.checkArgument(result.equals(PART_2_RESULT));
    }
}

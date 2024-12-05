package io.garnett.adventofcode.year2024.day5;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.models.ChallengeResult;
import io.garnett.adventofcode.models.ChallengeSolution;
import io.garnett.adventofcode.utils.InputUtils;
import io.garnett.adventofcode.year2024.day5.models.PrintingInstructions;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day5 implements ChallengeSolution<PrintingInstructions, Long> {

    @Override
    public ChallengeResult<Long> solve(
            @NonNull final PrintingInstructions input) {
        return new ChallengeResult<>(
                input.printJobs()
                        .stream()
                        .filter(t -> !t.stream()
                                .allMatch(this.validatePageOrder(t, input)))
                        .map(t -> t.stream()
                                .sorted(this.createOrderComparator(input))
                                .toList())
                        .mapToLong(t -> t.get(t.size() / 2))
                        .sum(),
                () -> "🖨");
    }

    private Comparator<Integer> createOrderComparator(
            @NonNull final PrintingInstructions input) {
        return (a, b) -> {
            final List<ImmutablePair<Integer, Integer>> testValues = input.pageOrdering().getOrDefault(a, List.of());

            if (testValues.contains(new ImmutablePair<>(a, b))) {
                return -1;
            }

            if (a.equals(b)) {
                return 0;
            }

            return 1;
        };
    }

    private Predicate<Integer> validatePageOrder(
            @NonNull final List<Integer> job,
            @NonNull final PrintingInstructions instructions) {
        return t -> {
            final int position = job.indexOf(t);

            if (position == job.size() - 1) {
                return true;
            }

            final Set<Integer> nextPages = Set.copyOf(job.subList(position + 1, job.size()));

            return nextPages
                    .stream()
                    .noneMatch(page -> instructions.pageOrdering()
                            .getOrDefault(page, List.of())
                            .contains(new ImmutablePair<>(page, t)));
        };
    }

    private static PrintingInstructions getInput() throws IOException {
        final Map<Integer, List<ImmutablePair<Integer, Integer>>> pageOrdering = InputUtils.readLines(
                        "2024/day5/input.txt")
                .filter(line -> line.contains("|"))
                .map(line -> Arrays.stream(line.split("\\|"))
                        .map(Integer::parseInt)
                        .toList())
                .map(t -> new ImmutablePair<>(t.getFirst(), t.get(1)))
                .collect(Collectors.groupingBy(ImmutablePair::getLeft));

        final List<List<Integer>> printJobs = InputUtils.readLines(
                        "2024/day5/input.txt")
                .filter(line -> line.contains(","))
                .map(line -> Arrays.stream(line.split(","))
                        .map(Integer::parseInt)
                        .toList())
                .toList();

        return new PrintingInstructions(
                pageOrdering,
                printJobs);
    }

    public static void main(final String[] args) {
        final var result = Application.challenge(
                "2024/day5",
                () -> new Day5()
                        .solve(Day5.getInput()));

        Preconditions.checkArgument(result.value() == 4971);
    }
}
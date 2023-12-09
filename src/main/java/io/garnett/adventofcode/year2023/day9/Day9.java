package io.garnett.adventofcode.year2023.day9;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Day9 implements Function<List<Day9.Sequence>, Integer> {
    @Override
    public Integer apply(
            @NonNull final List<Sequence> sequences) {
        return sequences
                .stream()
                .mapToInt(Sequence::decompose)
                .sum();
    }

    private static List<Sequence> getInput() throws IOException {
        return InputUtils.readLines("2023/day9/test.txt")
                .map(line -> Arrays.stream(line.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .boxed()
                        .toList())
                .map(Sequence::new)
                .toList();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day9",
                () -> new Day9()
                        .apply(Day9.getInput()));

        Preconditions.checkArgument(result == 1L);
    }

    public record Sequence(List<Integer> numbers) {
        public int decompose() {
            return 3;
        }
    }
}

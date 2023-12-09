package io.garnett.adventofcode.year2023.day9;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        return InputUtils.readLines("2023/day9/input.txt")
                .map(line -> Arrays.stream(line.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray())
                .map(Sequence::new)
                .toList();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day9",
                () -> new Day9()
                        .apply(Day9.getInput()));

        Preconditions.checkArgument(result == 1005);
    }

    public record Sequence(int[] numbers) {
        public int decompose() {
            ArrayUtils.reverse(this.numbers);
            return this.decompose(this.numbers, 0);
        }

        public int decompose(final int[] numbers, final int value) {
            final int size = numbers.length - 1;
            final int[] differences = new int[size];
            final Set<Integer> seen = new HashSet<>(Arrays.stream(numbers)
                    .boxed()
                    .toList());

            if (seen.contains(0) && seen.size() == 1) {
                return value;
            }

            for (int i = 0; i < differences.length; i++) {
                final int difference = numbers[i + 1] - numbers[i];
                differences[i] = difference;
            }

            return value + this.decompose(differences, (int)numbers[numbers.length - 1]);
        }
    }
}

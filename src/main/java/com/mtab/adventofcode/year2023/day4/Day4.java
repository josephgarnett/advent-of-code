package com.mtab.adventofcode.year2023.day4;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.InputUtils;
import lombok.Builder;
import lombok.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day4 implements Function<List<Day4.Scratchcard>, Long> {

    @Override
    public Long apply(
            @NonNull final List<Scratchcard> scratchcards) {
        return scratchcards
                .stream()
                .mapToLong(Scratchcard::getWinningValue)
                .sum();
    }

    public static List<Scratchcard> getInput() throws IOException {
        return InputUtils.readLines("2023/day4/input.txt")
                .map(s -> {
                    final String[] gameParts = s.split(": ");
                    final String[] numbersParts = gameParts[1].split(" \\| ");

                    return Scratchcard.builder()
                            .id(gameParts[0])
                            .numbers(Arrays.stream(numbersParts[1]
                                            .trim()
                                            .split("\\s+"))
                                    .map(Long::parseLong)
                                    .collect(Collectors.toSet()))
                            .winningNumbers(Arrays.stream(numbersParts[0]
                                            .trim()
                                            .split("\\s+"))
                                    .map(Long::parseLong)
                                    .collect(Collectors.toSet()))
                            .build();
                })
                .toList();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day4",
                () -> new Day4()
                        .apply(Day4.getInput()));

        Preconditions.checkArgument(result == 17803);
    }

    @Builder
    public record Scratchcard(String id, Set<Long> numbers, Set<Long> winningNumbers) {

        public long getWinning() {
            final Set<Long> intersection = new HashSet<>(this.winningNumbers());
            intersection.retainAll(this.numbers());

            return intersection.size();
        }
        public long getWinningValue() {
            return (long) Math.pow(2, this.getWinning() - 1);
        }
    }
}

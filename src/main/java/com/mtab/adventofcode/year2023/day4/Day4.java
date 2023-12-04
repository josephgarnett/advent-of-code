package com.mtab.adventofcode.year2023.day4;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.InputUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day4 implements Function<List<Day4.Scratchcard>, Long> {

    @Override
    public Long apply(
            @NonNull final List<Scratchcard> scratchcards) {
        final Map<Scratchcard, Long> scratchcardResults = new HashMap<>();
        scratchcards.forEach(s -> this.get(s, scratchcards, scratchcardResults));

        return scratchcardResults.values()
                .stream()
                .mapToLong(t -> t)
                .sum();
    }

    private void get(
            @NonNull final Scratchcard scratchcard,
            @NonNull final List<Scratchcard> scratchcards,
            @NonNull final Map<Scratchcard, Long> results) {
        results.computeIfPresent(scratchcard, (k, v) -> v + 1);
        results.computeIfAbsent(scratchcard, k -> 1L);

        if (scratchcard.getWinners() == 0) {
            return;
        }

        final long value = scratchcard.getWinners();
        final int position = scratchcards.indexOf(scratchcard);

        for (int i = 0; i < value; i++) {
            final Scratchcard next = scratchcards.get(position + i + 1);
            this.get(next, scratchcards, results);
        }
    }

    private long part1(
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
                        .apply(Day4.getInput()),
                50);

        Preconditions.checkArgument(result == 5554894);
    }

    @Getter
    public static class Scratchcard {
        private final String id;
        private final Set<Long> numbers;
        private final Set<Long> winningNumbers;
        private final long winners;

        @Builder
        public Scratchcard(
                @NonNull final String id,
                @NonNull final Set<Long> numbers,
                @NonNull final Set<Long> winningNumbers) {
            this.id = id;
            this.numbers = numbers;
            this.winningNumbers = winningNumbers;
            this.winners = this.computeWinners(numbers, winningNumbers);
        }

        private long computeWinners(
                @NonNull final Set<Long> numbers,
                @NonNull final Set<Long> winningNumbers) {
            final Set<Long> intersection = new HashSet<>(winningNumbers);
            intersection.retainAll(numbers);

            return intersection.size();
        }
        public long getWinningValue() {
            return (long) Math.pow(2, this.getWinners() - 1);
        }
    }
}

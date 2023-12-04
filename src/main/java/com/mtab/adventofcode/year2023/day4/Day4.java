package com.mtab.adventofcode.year2023.day4;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.InputUtils;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day4 implements Function<List<Day4.Scratchcard>, Integer> {

    @Override
    public Integer apply(
            @NonNull final List<Scratchcard> scratchcards) {
        AtomicInteger i = new AtomicInteger();
        final int[] results = new int[scratchcards.size()];
        scratchcards.forEach(s -> this.get(s, i.getAndIncrement(), scratchcards, results));

        return Arrays.stream(results)
                .sum();
    }

    private void get(
            @NonNull final Scratchcard scratchcard,
            final int index,
            @NonNull final List<Scratchcard> scratchcards,
            int[] results) {
        results[index]++;

        if (scratchcard.getWinners() == 0) {
            return;
        }

        final int value = scratchcard.getWinners();

        for (int i = 0; i < value; i++) {
            final int position = index + i + 1;
            final Scratchcard next = scratchcards.get(position);
            this.get(next, position, scratchcards, results);
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
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toSet()))
                            .winningNumbers(Arrays.stream(numbersParts[0]
                                            .trim()
                                            .split("\\s+"))
                                    .map(Integer::parseInt)
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

        Preconditions.checkArgument(result == 5554894);
    }

    @Value
    public static class Scratchcard {
        String id;
        Set<Integer> numbers;
        Set<Integer> winningNumbers;
        int winners;

        @Builder
        public Scratchcard(
                @NonNull final String id,
                @NonNull final Set<Integer> numbers,
                @NonNull final Set<Integer> winningNumbers) {
            this.id = id;
            this.numbers = numbers;
            this.winningNumbers = winningNumbers;
            this.winners = this.computeWinners(numbers, winningNumbers);
        }

        private int computeWinners(
                @NonNull final Set<Integer> numbers,
                @NonNull final Set<Integer> winningNumbers) {
            final Set<Integer> intersection = new HashSet<>(winningNumbers);
            intersection.retainAll(numbers);

            return intersection.size();
        }
        public int getWinningValue() {
            return (int) Math.pow(2, this.getWinners() - 1);
        }
    }
}

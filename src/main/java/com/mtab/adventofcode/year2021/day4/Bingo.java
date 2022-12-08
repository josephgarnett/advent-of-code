package com.mtab.adventofcode.year2021.day4;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.stream.Collectors;

public class Bingo implements Supplier<Map<BingoBoard, Integer>> {
    private final List<Integer> drawNumbers;
    private final List<BingoBoard> boards;

    private Bingo(
            final List<Integer> numbers,
            final List<BingoBoard> boards) {
        this.drawNumbers = ImmutableList.copyOf(numbers);
        this.boards = ImmutableList.copyOf(boards);
    }

    @Override
    public Map<BingoBoard, Integer> get() {
        final List<BingoBoard> boards = new ArrayList<>(this.boards);
        // final List<BingoBoard> winners = new ArrayList<>();
        final Map<BingoBoard, Integer> winners = new LinkedHashMap<>();

        for (int i = 0; i < this.drawNumbers.size(); ++i) {
            final int number = this.drawNumbers.get(i);

            boards
                    .stream()
                    .peek(b -> b.mark(number))
                    .filter(BingoBoard::isWinner)
                    .forEach(b -> {
                        // winners.add(b);
                        winners.put(b, number);
                    });

            boards.removeAll(winners.keySet());
        }

        return winners;
    }

    public static class Builder {
        private final List<Integer> numbers = new ArrayList<>();
        private final List<BingoBoard> boards = new ArrayList<>();

        public Builder addNumbers(final String[] numbers) {
            final int[] parsed = new int[numbers.length];

            int i = 0;
            for (final String number: numbers) {
                parsed[i++] = Integer.parseInt(number);
            }

            return this.addNumbers(parsed);
        }
        public Builder addNumbers(final int[] numbers) {
            this.numbers
                    .addAll(
                            Arrays.stream(numbers)
                                    .boxed()
                                    .collect(Collectors.toList()));

            return this;
        }

        public Builder addBoard(final BingoBoard board) {
            this.boards.add(board);

            return this;
        }

        public Bingo build() {
            return new Bingo(
                    this.numbers,
                    this.boards);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("drawNumbers", this.drawNumbers)
                .add("boards", this.boards)
                .toString();
    }
}

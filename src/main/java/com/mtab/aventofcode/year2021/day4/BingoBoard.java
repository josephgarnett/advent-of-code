package com.mtab.aventofcode.year2021.day4;

import com.google.common.base.MoreObjects;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BingoBoard {
    private static final int ROW_COUNT = 5;
    private static final int COLUMN_COUNT = 5;

    private final ArrayList<AtomicInteger> rows = new ArrayList<>(ROW_COUNT);
    private final ArrayList<AtomicInteger> columns = new ArrayList<>(COLUMN_COUNT);

    private final Map<Integer, Coordinate> numbers;

    private int currentLine = 0;

    BingoBoard() {
        this.numbers = new HashMap<>(ROW_COUNT * COLUMN_COUNT);
        for (int i = 0; i < ROW_COUNT; ++i) {
            this.rows.add(new AtomicInteger(0));
        }
        for (int j = 0; j < COLUMN_COUNT; ++j) {
            this.columns.add(new AtomicInteger(0));
        }
    }

    public void addLine(final List<String> numbers) {
        int i = 0;
        for (final String number: numbers) {
            final int n = Integer.parseInt(number);
            this.numbers.put(n, new Coordinate(i++, this.currentLine));
        }

        this.currentLine++;
    }

    public void mark(final int value) {
        if (!this.numbers.containsKey(value)) {
            return;
        }

        final Coordinate location = this.numbers.get(value);

        this.numbers.remove(value);
        this.rows.get(location.getX()).incrementAndGet();
        this.columns.get(location.getY()).incrementAndGet();
    }

    public boolean isWinner() {
        return this.columns
                .stream()
                .anyMatch((v) -> v.get() == COLUMN_COUNT) ||
                this.rows
                        .stream()
                        .anyMatch((v) -> v.get() == ROW_COUNT);
    }

    public int unmarkedValue() {
        return this.numbers
                .keySet()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("rows", this.rows)
                .add("columns", this.columns)
                .add("numbers", this.numbers)
                .toString();
    }

    private static class Coordinate {
        private final int x;
        private final int y;

        Coordinate(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        public int getX () {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("x", this.x)
                    .add("y", this.y)
                    .toString();
        }
    }
}

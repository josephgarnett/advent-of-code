package com.mtab.aventofcode.year2021.day4;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Streams;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BingoBoard {
    private static int ROW_COUNT = 5;
    private static int COLUMN_COUNT = 5;

    private ArrayList<Set<Integer>> rows = new ArrayList<>(ROW_COUNT);
    private ArrayList<Set<Integer>> columns = new ArrayList<>(COLUMN_COUNT);

    private int currentLine = 0;

    BingoBoard() {
        for (int i = 0; i < ROW_COUNT; ++i) {
            this.rows.add(new HashSet<>(COLUMN_COUNT));
        }
        for (int j = 0; j < COLUMN_COUNT; ++j) {
            this.columns.add(new HashSet<>(ROW_COUNT));
        }
    }

    public void addLine(final List<String> numbers) {
        int i = 0;
        for (final String number: numbers) {
            final int n = Integer.parseInt(number);
            this.columns.get(i).add(n);
            this.rows.get(currentLine).add(n);
            i++;
        }
        this.currentLine++;
    }

    public void mark(final int value) {
        this.columns
                .forEach((column) -> column.remove(value));
        this.rows
                .forEach((row) -> row.remove(value));
    }

    public boolean isWinner() {
        return Streams
                .concat(this.columns.stream(), this.rows.stream())
                .anyMatch(Set::isEmpty);
    }

    public int unmarkedValue() {
        return this.columns.stream()
                .flatMap(Set::stream)
                .mapToInt(Integer::intValue)
                .sum();
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("rows", this.rows)
                .add("columns", this.columns)
                .toString();
    }
}

package com.mtab.aventofcode.year2021.day4;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.models.InputLoader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day4 implements
        InputLoader<Bingo>,
        Supplier<Map<BingoBoard, Integer>> {

    private static final String INPUT_PATH = "2021/day4/input.txt";

    private final Bingo input;

    Day4() {
        this.input = this.getInput(INPUT_PATH);
    }

    @Override
    public Map<BingoBoard, Integer> get() {
        return this.input.get();
    }

    @Override
    public Bingo getInput(final String filePath) {
        try (BufferedReader br = new BufferedReader(
                new FileReader(
                        this.getResourceFile(filePath)))) {
            final Bingo.Builder builder = new Bingo.Builder();

            String line;
            int index = 0;
            Optional<BingoBoard> currentBoard = Optional.empty();
            while ((line = br.readLine()) != null) {
                if (index == 0 ) {
                    final String[] numbers = line.split(",");
                    builder.addNumbers(numbers);

                    index++;
                    continue;
                }

                if (StringUtils.isEmpty(line)) {
                    currentBoard.ifPresent(builder::addBoard);
                    currentBoard = Optional.of(new BingoBoard());
                    index++;
                    continue;
                }

                String finalLine = line;
                currentBoard.ifPresent(board -> {
                    board.addLine(
                            Arrays
                                    .stream(
                                            finalLine.split("\\s+"))
                                    .filter(StringUtils::isNotEmpty)
                                    .collect(Collectors.toList()));
                });

                index++;
            }

            currentBoard.ifPresent(builder::addBoard);

            return builder.build();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final Map<BingoBoard, Integer> result = new Day4().get();

        if (result.size() == 0) {
            throw new RuntimeException("No winning boards");
        }

        final List<Map.Entry<BingoBoard, Integer>> winningBoards = new ArrayList<>(result.entrySet());
        final Map.Entry<BingoBoard, Integer> firstWinningEntry = winningBoards.get(0);
        final Map.Entry<BingoBoard, Integer> lastWinningEntry = winningBoards.get(winningBoards.size() - 1);

        final int firstResult = firstWinningEntry.getKey().unmarkedValue() * firstWinningEntry.getValue();
        final int lastResult = lastWinningEntry.getKey().unmarkedValue() * lastWinningEntry.getValue();

        Preconditions.checkArgument(firstResult == 27027);
        Preconditions.checkArgument(lastResult == 36975);

        System.out.printf(
                "First winner %d%n",
                firstResult);

        System.out.printf(
                "Last winner %d%n",
                lastResult);

        System.out.println(sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

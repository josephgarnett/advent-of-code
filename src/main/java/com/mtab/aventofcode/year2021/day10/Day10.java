package com.mtab.aventofcode.year2021.day10;

import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.models.InputListLoader;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day10 implements
        Supplier<Long>,
        InputListLoader<String> {

    private static final Pattern TOKEN_OPENERS = Pattern.compile("[\\[{(<]");
    private static final Pattern TOKEN_CLOSERS = Pattern.compile("[])}>]");

    private final List<String> input;
    private final boolean analyzeCorruption;

    Day10(final boolean analyzeCorruption, final String resourcePath) {
        this.analyzeCorruption = analyzeCorruption;
        this.input = this.getInput(resourcePath);
    }


    @Override
    public String parseLine(final String line, final int index) {
        return line;
    }

    @Override
    public Long get() {
        if (this.analyzeCorruption) {
            return (long) this.input.stream()
                    .mapToInt(this::getCorruptionScore)
                    .sum();
        }

        final List<Long> autoCompleteScores = this.input.stream()
                .filter((l) -> this.getCorruptionScore(l) == 0)
                .map(this::getAutoCompleteScore)
                .sorted()
                .collect(Collectors.toList());

        return autoCompleteScores.get(autoCompleteScores.size() / 2);
    }

    private long getAutoCompleteScore(final String line) {
        final Stack<String> bracketStack = new Stack<>();

        for (final String s: line.split("")) {
            if (TOKEN_OPENERS.matcher(s).matches()) {
                bracketStack.push(s);
                continue;
            }

            if (TOKEN_CLOSERS.matcher(s).matches()) {
                bracketStack.pop();
            }
        }

        long score = 0;
        while (bracketStack.size() > 0) {
            final String bracket = bracketStack.pop();
            if (StringUtils.equals("(", bracket)) {
                score = (score * 5) + 1;
                continue;
            }

            if (StringUtils.equals("[", bracket)) {
                score = (score * 5) + 2;
                continue;
            }

            if (StringUtils.equals("{", bracket)) {
                score = (score * 5) + 3;
                continue;
            }

            if (StringUtils.equals("<", bracket)) {
                score = (score * 5) + 4;
                continue;
            }
        }

        return score;
    }

    private int getCorruptionScore(final String line) {
        final Stack<String> bracketStack = new Stack<>();

        for (final String s: line.split("")) {
            if (TOKEN_OPENERS.matcher(s).matches()) {
                bracketStack.push(s);
                continue;
            }

            if (TOKEN_CLOSERS.matcher(s).matches()) {
                final String closer = bracketStack.pop();
                if (StringUtils.equals(")", s) && !StringUtils.equals("(", closer)) {
                    return 3;
                }

                if (StringUtils.equals("]", s) && !StringUtils.equals("[", closer)) {
                    return 57;
                }

                if (StringUtils.equals("}", s) && !StringUtils.equals("{", closer)) {
                    return 1197;
                }

                if (StringUtils.equals(">", s) && !StringUtils.equals("<", closer)) {
                    return 25137;
                }
            }
        }

        return 0;
    }

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final long corruptionScore = new Day10(true, "2021/day10/input.txt").get();
        System.out.println(corruptionScore);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));

        sw.reset();
        sw.start();

        final long autoCompleteScore = new Day10(false, "2021/day10/input.txt").get();
        System.out.println(autoCompleteScore);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));


    }
}

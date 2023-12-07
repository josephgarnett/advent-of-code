package io.garnett.adventofcode.year2021.day5;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import io.garnett.adventofcode.models.InputListLoader;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day5 implements
        InputListLoader<Line2D>,
        Supplier<Long> {
    private static final String INPUT_PATH = "2021/day5/input.txt";

    private final List<Line2D> input;

    private Day5(final boolean considerDiagonal) {
        this.input = this.getInput(INPUT_PATH)
                .stream()
                .filter((line) -> {
                    if (!considerDiagonal) {
                        return line.getP1().getX() == line.getP2().getX() || line.getP1().getY() == line.getP2().getY();
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Long get() {
        final Map<Point2D, Integer> result = new HashMap<>();

        this.input
                .stream()
                .flatMap((line) -> this.lineToPoints(line).stream())
                .forEach((point) -> {
                    if (result.containsKey(point)) {
                        result.computeIfPresent(point, (k, i) -> i + 1);

                        return;
                    }
                    result.computeIfAbsent(point, (k) -> 1);
                });

        return result.values()
                .stream()
                .filter((i) -> i > 1)
                .count();
    }

    @Override
    public Line2D parseLine(final String line, final int index) {
        final String[] coords = line.split("\\s*->\\s*");

        final String[] values1 = coords[0].split(",");
        final String[] values2 = coords[1].split(",");
        final Point2D start = new Point2D.Float(Float.parseFloat(values1[0]), Float.parseFloat(values1[1]));
        final Point2D end = new Point2D.Float(Float.parseFloat(values2[0]), Float.parseFloat(values2[1]));

        return new Line2D.Double(start, end);
    }

    private List<Point2D> lineToPoints(final Line2D line) {
        final ImmutableList.Builder<Point2D> builder = new ImmutableList.Builder<>();

        if (line.getY1() == line.getY2()) {
            final double start = Math.min(line.getX1(), line.getX2());
            final double end = Math.max(line.getX1(), line.getX2());
            for (double i = start; i <= end; ++i) {
                builder.add(new Point2D.Double(i, line.getY1()));
            }

            return builder.build();
        }

        if (line.getX1() == line.getX2()) {
            final double start = Math.min(line.getY1(), line.getY2());
            final double end = Math.max(line.getY1(), line.getY2());
            for (double i = start; i <= end; ++i) {
                builder.add(new Point2D.Double(line.getX1(), i));
            }

            return builder.build();
        }

        // always 45 degrees
        final double iterations = Math.abs(line.getX1() - line.getX2());
        final boolean isLeftToRight = line.getX1() <= line.getX2();
        final boolean isTopToBottom = line.getY1() <= line.getY2();

        for (double i = 0; i <= iterations; ++i) {
            final double x = isLeftToRight ? line.getX1() + i : line.getX1() - i;
            final double y = isTopToBottom ? line.getY1() + i : line.getY1() - i;
            builder.add(new Point2D.Double(x, y));
        }

        return builder.build();
    }



    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final Long result = new Day5(true).get();

        // Preconditions.checkArgument(result == 7414);
        // Preconditions.checkArgument(result == 12);
        Preconditions.checkArgument(result == 19676);

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

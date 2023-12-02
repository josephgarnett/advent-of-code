package com.mtab.adventofcode.year2015.day3;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day3 implements Function<List<Day3.Direction>, Integer> {
    public Integer apply(
            @NonNull final List<Direction> directions) {
        return IntStream.range(0, directions.size())
                .boxed()
                .collect(Collectors.partitioningBy(i -> i % 2 == 0))
                .values()
                .stream()
                .map((list -> list.stream().map(directions::get)))
                .map(this::route)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .size();
    }

    private Set<Point> route(@NonNull final Stream<Direction> directions) {
        final Set<Point> points = new HashSet<>();
        final Point start = new Point(0, 0);
        final AtomicReference<Point> last = new AtomicReference<>(start);

        points.add(start);

        directions.forEach(d -> {
            final Point next = d.adjust(last.get());
            last.set(next);
            points.add(next);
        });

        return points;
    }

    private static List<Direction> getInput() throws IOException {
        return InputUtils.readLines(
                        "2015/day3/input.txt")
                .flatMap(line -> Arrays.stream(line.split("")))
                .map(Direction::new)
                .toList();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2015/day3",
                () -> new Day3()
                        .apply(Day3.getInput()));

        Preconditions.checkArgument(result == 2631);
    }

    public record Direction(String direction) {
        public Point adjust(
                @NonNull final Point source) {
            return switch (this.direction) {
                case ">" -> new Point(source.x + 1, source.y);
                case "<" -> new Point(source.x - 1, source.y);
                case "v" -> new Point(source.x, source.y + 1);
                case "^" -> new Point(source.x, source.y - 1);
                default -> throw new UnsupportedOperationException("Unsupported direction");
            };
        }
    }
}


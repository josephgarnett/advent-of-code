package io.garnett.adventofcode.year2024.day4;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.Direction;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day4 implements ToLongFunction<Map<Point, String>> {

    private static final String P1_TARGET = "XMAS";
    private static final String P2_TARGET = "MAS";
    private static final String P2_TARGET_ALT = "SAM";


    @Override
    public long applyAsLong(
            @NonNull final Map<Point, String> input) {
        return this.part2(input);
    }

    private long part2(
            @NonNull final Map<Point, String> input) {
        return input.entrySet()
                .stream()
                .filter(t -> StringUtils.equals("A", t.getValue()))
                .mapToLong(this.mapToX(input))
                .sum();
    }

    private long part1(
            @NonNull final Map<Point, String> input) {
        return input.entrySet()
                .stream()
                .filter(t -> StringUtils.equals("X", t.getValue()))
                .mapToLong(this.mapToMatches(P1_TARGET, input))
                .sum();
    }

    private ToLongFunction<Map.Entry<Point, String>> mapToX(
            @NonNull final Map<Point, String> map) {
        return entry -> Stream.of(
                        Direction.NORTH_EAST,
                        Direction.NORTH_WEST)
                .map(direction -> this.buildFromCenter(entry.getKey(), direction, map, 1))
                .allMatch(s -> StringUtils.equals(P2_TARGET, s) || StringUtils.equals(P2_TARGET_ALT, s)) ? 1 : 0;
    }

    private ToLongFunction<Map.Entry<Point, String>> mapToMatches(
            @NonNull final String target,
            @NonNull final Map<Point, String> map) {
        return entry -> Arrays.stream(Direction.values())
                .map(direction -> this.buildFromStart(entry.getKey(), direction, map, target.length()))
                .filter(s -> StringUtils.equals(target, s))
                .count();
    }

    private String buildFromCenter(
            @NonNull final Point start,
            @NonNull final Direction direction,
            @NonNull final Map<Point, String> map,
            int length) {
        final StringBuilder builder = new StringBuilder(length);

        builder.append(map.get(start));

        for (int i = 1; i < length + 1; ++i) {
            if (direction == Direction.NORTH_EAST) {
                builder.insert(0, map.get(new Point(start.x + i, start.y - i)));
                builder.append(map.get(new Point(start.x - i, start.y + i)));
            }

            if (direction == Direction.NORTH_WEST) {
                builder.insert(0, map.get(new Point(start.x - i, start.y - i)));
                builder.append(map.get(new Point(start.x + i, start.y + i)));
            }
        }

        return builder.toString();
    }

    private String buildFromStart(
            @NonNull final Point start,
            @NonNull final Direction direction,
            @NonNull final Map<Point, String> map,
            int length) {
        final StringBuilder builder = new StringBuilder(length);

        Point next = start;

        while (map.containsKey(next)) {
            builder.append(map.get(next));

            if (builder.length() == length) {
                return builder.toString();
            }

            switch (direction) {
                case NORTH -> next = new Point(next.x, next.y - 1);
                case EAST -> next = new Point(next.x + 1, next.y);
                case SOUTH -> next = new Point(next.x, next.y + 1);
                case WEST -> next = new Point(next.x - 1, next.y);
                case NORTH_EAST -> next = new Point(next.x + 1, next.y - 1);
                case NORTH_WEST -> next = new Point(next.x - 1, next.y - 1);
                case SOUTH_EAST -> next = new Point(next.x + 1, next.y + 1);
                case SOUTH_WEST -> next = new Point(next.x - 1, next.y + 1);
            }
        }

        return builder.toString();
    }

    private static Map<Point, String> getInput() throws IOException {
        final List<String> lines = InputUtils.readLines(
                        "2024/day4/input.txt")
                .toList();

        return IntStream.range(0, lines.size())
                .boxed()
                .flatMap(y -> IntStream.range(0, lines.get(y).length())
                        .mapToObj(x -> new Point(x, y)))
                .collect(Collectors.toMap(
                        Function.identity(),
                        p -> lines.get(p.y).substring(p.x, p.x + 1)));
    }

    public static void main(final String[] args) {
        final var result = Application.challenge(
                "2024/day4",
                () -> new Day4()
                .applyAsLong(Day4.getInput()));

        Preconditions.checkArgument(result == 1965);
    }
}
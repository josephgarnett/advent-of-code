package com.mtab.adventofcode.year2023.day2;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.InputUtils;
import lombok.Builder;
import lombok.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Day2 implements Function<List<Day2.Cubes>, Integer> {

    //only 12 red cubes, 13 green cubes, and 14 blue cubes
    private static final int MAX_RED = 12;
    private static final int MAX_GREEN = 13;
    private static final int MAX_BLUE = 14;

    @Override
    public Integer apply(List<Day2.Cubes> cubes) {
        return cubes.stream()
                .map(Cubes::minimumCubeSnapshot)
                .mapToInt(s -> s.red() * s.green() * s.blue())
                .sum();
    }

    public Long part1(List<Day2.Cubes> cubes) {
        return cubes.stream()
                .filter(c -> c.hasValidSnapshots(MAX_RED, MAX_GREEN, MAX_BLUE))
                .mapToLong(Cubes::gameId)
                .sum();
    }

    private static List<Snapshot> getSnapshots(
            @NonNull final String[] raw) {
        return Arrays.stream(raw)
                .map(s -> {
                    final String[] cubes = s.split(", ");
                    final Snapshot.SnapshotBuilder builder = Snapshot.builder();

                    Arrays.stream(cubes)
                            .forEach(c -> {
                                final String[] data = c.split(" ");
                                final String color = data[1];
                                final int value = Integer.parseInt(data[0]);

                                switch (color) {
                                    case "red" -> builder.red(value);
                                    case "green" -> builder.green(value);
                                    case "blue" -> builder.blue(value);
                                }
                            });

                    return builder.build();
                })
                .toList();
    }

    private static List<Cubes> getInput() throws IOException {
        final List<String> rounds = InputUtils.readLines(
                        "2023/day2/input.txt")
                .toList();

        return rounds.stream()
                .map(r -> {
                    final String[] gameParts = r.split(": ");
                    final String[] snapshotParts = gameParts[1].split("; ");
                    final long gameId = Long.parseLong(gameParts[0]
                            .toLowerCase()
                            .replace("game", "")
                            .trim());

                    return Cubes.builder()
                            .gameId(gameId)
                            .snapshots(Day2.getSnapshots(snapshotParts))
                            .build();
                })
                .toList();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day2",
                () -> new Day2()
                        .apply(Day2.getInput()));

        Preconditions.checkArgument(result == 71274);
    }

    @Builder
    public record Cubes(long gameId, List<Snapshot> snapshots) {
        boolean hasValidSnapshots(
                final int maxRed,
                final int maxGreen,
                final int maxBlue) {
            return this.snapshots
                    .stream()
                    .filter(s -> s.red() <= maxRed
                            && s.green() <= maxGreen
                            && s.blue() <= maxBlue)
                    .toList()
                    .size() == this.snapshots.size();
        }

        Snapshot minimumCubeSnapshot() {
            final Snapshot.SnapshotBuilder result = Snapshot.builder();
            this.snapshots()
                    .forEach(snapshot -> {
                        if (snapshot.red() > result.red) {
                            result.red(snapshot.red());
                        }
                        if (snapshot.green() > result.green) {
                            result.green(snapshot.green());
                        }
                        if (snapshot.blue() > result.blue) {
                            result.blue(snapshot.blue());
                        }
                    });
            return result.build();
        }
    }

    @Builder
    record Snapshot(int red, int green, int blue) {}
}

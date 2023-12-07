package io.garnett.adventofcode.year2023.day6;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day6 implements Function<Day6.Regatta, Long> {

    @Override
    public Long apply(@NonNull final Regatta regatta) {
        return regatta.getRaces()
                .stream()
                .mapToLong(Race::getMarginOfError)
                .reduce((a, b) -> a * b)
                .orElseThrow();
    }

    private static Regatta getInput() throws IOException {
        final List<Race.RaceBuilder> races = new ArrayList<>();
        InputUtils.readLines("2023/day6/input.txt")
                .forEach(line -> {
                    final String parsed = line.replaceAll("\\s+", "");
                    if (StringUtils.startsWithIgnoreCase(parsed, "time")) {
                        final String[] times = parsed.replace("Time:", "")
                                .trim()
                                .replace("\\s+", "")
                                .split("\\s+");

                        Arrays.stream(times)
                                .forEach(t -> races
                                        .add(Race.builder()
                                                .time(Integer.parseInt(t))));
                    } else {
                        final String[] distances = parsed.replace("Distance:", "")
                                .trim()
                                .split("\\s+");

                        IntStream.range(0, distances.length)
                                .forEach(i -> races.get(i)
                                        .record(Long.parseLong(distances[i])));
                    }
                });

        return Regatta.builder()
                .races(races.stream()
                        .map(Race.RaceBuilder::build)
                        .toList())
                .build();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day6",
                () -> new Day6()
                        .apply(Day6.getInput()),
                1);

        Preconditions.checkArgument(result == 45128024);
    }

    @Value
    @Builder
    public static class Regatta {
        List<Race> races;
    }

    @Builder
    public record Race(long time, long record) {
        long getMarginOfError() {
            final double center = (double) this.time() / 2;
            final long count = LongStream.range((long) Math.ceil(center), this.time())
                    .map(t -> t * (this.time() - t))
                    .takeWhile(t -> t > this.record())
                    .count();

            return this.time % 2 == 0 ? count + count - 1 : count * 2;
        }
    }
}

package io.garnett.adventofcode.year2015.day2;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day2 implements Function<List<Day2.Present>, Long> {
    public Long apply(
            @NonNull final List<Present> presents) {
        return presents.stream()
                .mapToLong(Present::getRibbonSize)
                .sum();
    }


    private static List<Present> getInput() throws IOException {
        return InputUtils.readLines(
                        "2015/day2/input.txt")
                .map(line -> {
                    final String[] parts = line.split("x");
                    return new Present(
                            Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]));
                })
                .toList();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2015/day2",
                () -> new Day2()
                        .apply(Day2.getInput()));

        Preconditions.checkArgument(result == 3812909);
    }

    public record Present(long length, long width, long height) {
        long getPaperSize() {
            final long areaLW = this.length * this.width;
            final long areaWH = this.width * this.height;
            final long areaHL = this.height * this.length;
            final long smallestArea = Collections.min(List.of(areaLW, areaWH, areaHL));

            return 2 * areaLW + 2 * areaWH + 2 * areaHL + smallestArea;
        }

        long getRibbonSize() {
            final long bowLength = this.length * this.height * this.width;
            return Stream.of(this.length, this.width, this.height)
                    .sorted()
                    .limit(2)
                    .reduce(bowLength, (acc, item) -> acc + item * 2);
        }
    }
}

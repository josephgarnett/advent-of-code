package io.garnett.adventofcode.year2015.day4;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 implements Function<String, Integer> {
    public Integer apply(
            @NonNull final String hash) {
        return IntStream.range(0, Integer.MAX_VALUE)
                .filter(t -> StringUtils.startsWith(
                        Hashing.md5()
                                .hashString(hash + t, Charsets.UTF_8).toString(),
                        "000000"))
                .findFirst()
                .orElseThrow();
    }

    private static String getInput() throws IOException {
        return InputUtils.readLines(
                        "2015/day4/input.txt")
                .collect(Collectors.joining());
    }

    public static void main(final String[] args) {
        final var result = Application.challenge(
                "2015/day4",
                () -> new Day4()
                        .apply(Day4.getInput()),
                20);

        Preconditions.checkArgument(result == 1038736);
    }
}


package io.garnett.adventofcode.year2015.day5;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.function.ToLongFunction;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/*
It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.

 */

public class Day5 implements ToLongFunction<List<String>> {
    private static final Pattern DOUBLE_LETTERS = Pattern.compile("([a-z])\\1");
    private static final Pattern VOWEL_REQUIREMENTS = Pattern.compile("[aeiou]");
    private static final List<String> NAUGHTY_LIST = List.of("ab", "cd", "pq", "xy");

    @Override
    public long applyAsLong(
            @NonNull final List<String> input) {
        return input.stream()
                .filter((t -> NAUGHTY_LIST.stream()
                        .noneMatch(t::contains)))
                .filter(t -> VOWEL_REQUIREMENTS.matcher(t).results().count() >= 3)
                .filter(t -> DOUBLE_LETTERS.matcher(t).find())
                .count();
    }

    private static List<String> getInput() throws IOException {
        return InputUtils.readLines(
                        "2015/day5/input.txt")
                .toList();
    }

    public static void main(final String[] args) {
        final var result = Application.challenge(
                "2015/day5",
                () -> new Day5()
                        .applyAsLong(Day5.getInput()));

        Preconditions.checkArgument(result == 236);
    }
}
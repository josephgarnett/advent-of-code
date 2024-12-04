package io.garnett.adventofcode.year2015.day5;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 implements ToLongFunction<List<String>> {
    private static final Pattern DOUBLE_LETTERS = Pattern.compile("([a-z])\\1");
    private static final Pattern VOWEL_REQUIREMENTS = Pattern.compile("[aeiou]");
    private static final Pattern SEQUENCE = Pattern.compile("([a-z])[a-z]\\1");
    private static final List<String> NAUGHTY_LIST = List.of("ab", "cd", "pq", "xy");

    private static final List<String> TEST_CASES = List.of(
            "abcdefghglmnop",
            "qjhvhtzxzqqjkmpb",
            "xxyxx",
            "afafa",
            "uurcxstgmygtbstg",
            "ieodomkazucvgmuy");

    @Override
    public long applyAsLong(
            @NonNull final List<String> input) {
        return this.part2(input);
    }

    private long part2(
            @NonNull final List<String> input) {
        return input.stream()
                // TODO: has a problem with overlaps
                .filter(t -> IntStream.range(0, t.length() - 1)
                        .mapToObj(i -> t.substring(i, i + 2))
                        .collect(Collectors.groupingBy(Function.identity()))
                        .values()
                        .stream()
                        .anyMatch(group -> group.size() > 1))
                .filter(t -> SEQUENCE.matcher(t).find())
                .peek(System.out::println)
                .count();
    }

    private long part1(
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
                        .applyAsLong(Day5.getInput()),
                1);

        Preconditions.checkArgument(result == 236);
    }
}
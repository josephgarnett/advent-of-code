package io.garnett.adventofcode.year2024.day3;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.models.ChallengeResult;
import io.garnett.adventofcode.models.ChallengeSolution;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 implements ChallengeSolution<String, Long> {
    private static final String MULTIPLICATION_INSTRUCTION = "mul";
    private static final String CONDITIONAL_TRUE_INSTRUCTION = "do";
    private static final String CONDITIONAL_FALSE_INSTRUCTION = "don't";
    private static final Pattern INSTRUCTIONS = Pattern.compile("(do\\(\\))|(don't\\(\\))|(mul\\(\\d+,\\d+\\))");

    private final AtomicBoolean enabled = new AtomicBoolean(true);

    @Override
    public ChallengeResult<Long> solve(
            @NonNull final String input) {
        final List<String> sequence = new ArrayList<>();
        final long result = INSTRUCTIONS.matcher(input)
                .results()
                .map(MatchResult::group)
                .peek(sequence::add)
                .reduce(0L,
                        this::apply,
                        Long::sum);

        return new ChallengeResult<>(
                result,
                () -> sequence.stream()
                        .limit(8)
                        .collect(Collectors.joining(" -> ")) + "...");
    }

    private long apply(
            final long result,
            @NonNull final String instruction) {
        if (StringUtils.startsWith(instruction, CONDITIONAL_TRUE_INSTRUCTION)) {
            enabled.set(true);
        }

        if (StringUtils.startsWith(instruction, CONDITIONAL_FALSE_INSTRUCTION)) {
            enabled.set(false);
        }

        if (StringUtils.startsWith(instruction, MULTIPLICATION_INSTRUCTION) && enabled.get()) {
            return result + multiply(instruction);
        }

        return result;
    }

    private int multiply(@NonNull final String expression) {
        final String[] components = expression.replace("mul(", StringUtils.EMPTY)
                .replace(")", StringUtils.EMPTY)
                .split(",");

        return Integer.parseInt(components[0]) * Integer.parseInt(components[1]);
    }

    private static String getInput() throws IOException {
        return InputUtils.readLines(
                        "2024/day3/input.txt")
                .collect(Collectors.joining());
    }

    public static void main(final String[] args) {
        final var result = Application.challenge(
                "2024/day3",
                () -> new Day3()
                        .solve(Day3.getInput()));

        Preconditions.checkArgument(result.value() == 76911921);
    }
}
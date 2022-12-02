package com.mtab.aventofcode.year2022.day2;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.InputUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day2 implements Function<List<Day2.RPSRound>, Long> {
    private static List<RPSRound> getInput() throws IOException {
        return Files.lines(
                        Path.of(InputUtils.getInputPath("2022/day2/input.txt")))
                .map(line -> {
                    final var parts = line.split(" ");
                    return new Day2.RPSRound(parts[0], parts[1]);
                })
                .collect(Collectors.toList());
    }

    public static void main(final String[] args) throws IOException {
        final Stopwatch sw = Stopwatch.createStarted();

        final var result = new Day2().apply(Day2.getInput());

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));

        Preconditions.checkArgument(result == 12156);
    }

    @Override
    public Long apply(final List<Day2.RPSRound> rounds) {
        return rounds
                .stream()
                .mapToLong((r) -> r.get())
                .sum();
    }

    public static class RPSRound implements Supplier<Integer> {
        private enum RPS {
            ROCK(1),
            PAPER(2),
            SCISSORS(3);

            private int value;
            RPS(final int value) {
                this.value = value;
            }

            int getValue() {
                return this.value;
            }

        }

        private static final Map<String, RPS> CIPHER_LHS = Map.of(
                "A", RPS.ROCK,
                "B", RPS.PAPER,
                "C", RPS.SCISSORS
        );

        private static final Map<String, RPS> CIPHER_RHS = Map.of(
                "Y", RPS.PAPER,
                "X", RPS.ROCK,
                "Z", RPS.SCISSORS
        );

        private final RPS lhs;
        private final RPS rhs;

        public RPSRound(final String hand1, final String hand2) {
            this.lhs = CIPHER_LHS.get(hand1);
            this.rhs = CIPHER_RHS.get(hand2);
        }

        @Override
        public Integer get() {
            if (lhs == RPS.ROCK) {
                if (rhs == RPS.ROCK) {
                    return 3 + RPS.ROCK.getValue();
                }

                if (rhs == RPS.PAPER) {
                    return 6 + RPS.PAPER.getValue();
                }

                if (rhs == RPS.SCISSORS) {
                    return RPS.SCISSORS.getValue();
                }
            }

            if (lhs == RPS.PAPER) {
                if (rhs == RPS.ROCK) {
                    return RPS.ROCK.getValue();
                }

                if (rhs == RPS.PAPER) {
                    return 3 + RPS.PAPER.getValue();
                }

                if (rhs == RPS.SCISSORS) {
                    return 6 + RPS.SCISSORS.getValue();
                }
            }

            if (lhs == RPS.SCISSORS) {
                if (rhs == RPS.ROCK) {
                    return 6 + RPS.ROCK.getValue();
                }

                if (rhs == RPS.PAPER) {
                    return RPS.PAPER.getValue();
                }

                if (rhs == RPS.SCISSORS) {
                    return 3 + RPS.SCISSORS.getValue();
                }
            }

            throw new IllegalStateException();
        }
    }
}

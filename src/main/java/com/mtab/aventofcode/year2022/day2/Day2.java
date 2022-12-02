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

        Preconditions.checkArgument(result == 10835);
    }

    @Override
    public Long apply(final List<Day2.RPSRound> rounds) {
        return rounds
                .stream()
                .mapToLong(RPSRound::get)
                .sum();
    }

    public static class RPSRound implements Supplier<Integer> {
        private enum RPS {
            ROCK(1),
            PAPER(2),
            SCISSORS(3);

            private final int value;
            RPS(final int value) {
                this.value = value;
            }

            int getValue() {
                return this.value;
            }

        }

        private enum Result {
            WIN(6),
            LOSE(0),
            DRAW(3);

            private final int value;
            Result(final int value) {
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

        private static final Map<String, Result> CIPHER_RHS = Map.of(
                "Y", Result.DRAW,
                "X", Result.LOSE,
                "Z", Result.WIN
        );

        private final RPS lhs;
        private final Result rhs;

        public RPSRound(final String hand1, final String hand2) {
            this.lhs = CIPHER_LHS.get(hand1);
            this.rhs = CIPHER_RHS.get(hand2);
        }

        @Override
        public Integer get() {
            if (lhs == RPS.ROCK) {
                if (rhs == Result.WIN) {
                    return Result.WIN.getValue() + RPS.PAPER.getValue();
                }

                if (rhs == Result.DRAW) {
                    return Result.DRAW.getValue() + RPS.ROCK.getValue();
                }

                if (rhs == Result.LOSE) {
                    return Result.LOSE.getValue() + RPS.SCISSORS.getValue();
                }
            }

            if (lhs == RPS.PAPER) {
                if (rhs == Result.WIN) {
                    return Result.WIN.getValue() + RPS.SCISSORS.getValue();
                }

                if (rhs == Result.DRAW) {
                    return Result.DRAW.getValue() + RPS.PAPER.getValue();
                }

                if (rhs == Result.LOSE) {
                    return Result.LOSE.getValue() + RPS.ROCK.getValue();
                }
            }

            if (lhs == RPS.SCISSORS) {
                if (rhs == Result.WIN) {
                    return Result.WIN.getValue() + RPS.ROCK.getValue();
                }

                if (rhs == Result.DRAW) {
                    return Result.DRAW.getValue() + RPS.SCISSORS.getValue();
                }

                if (rhs == Result.LOSE) {
                    return Result.LOSE.getValue() + RPS.PAPER.getValue();
                }
            }

            throw new IllegalStateException();
        }
    }
}

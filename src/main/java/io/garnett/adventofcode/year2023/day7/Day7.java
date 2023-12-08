package io.garnett.adventofcode.year2023.day7;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7 implements Function<List<Day7.Hand>, Long> {
    @Override
    public Long apply(@NonNull final List<Hand> hands) {
        final List<Hand> results = hands
                .stream()
                .sorted(Hand.handComparator())
                .toList();

        return IntStream.range(0, results.size())
                .mapToLong(i -> results.get(i).bid() * (i + 1))
                .sum();
    }

    private static List<Hand> getInput() throws IOException {
        return InputUtils.readLines("2023/day7/input.txt")
                .map(line -> line.split("\\s+"))
                .map(parts -> Hand.builder()
                        .value(parts[0])
                        .bid(Long.parseLong(parts[1]))
                        .build())
                .toList();
    }
    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day7",
                () -> new Day7()
                        .apply(Day7.getInput()),
                1);

        Preconditions.checkArgument(result == 254115617);
    }

    @Getter
    @RequiredArgsConstructor
    public enum HandType {
        HIGH_CARD(1),
        ONE_PAIR(2),
        TWO_PAIR(3),
        THREE_OF_A_KIND(4),
        FULL_HOUSE(5),
        FOUR_OF_A_KIND(6),
        FIVE_OF_A_KIND(7);

        final int value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum HandLabel {
        JACK(0), // wildcard
        TWO(1),
        THREE(2),
        FOUR(3),
        FIVE(4),
        SIX(5),
        SEVEN(6),
        EIGHT(7),
        NINE(8),
        TEN(9),
        QUEEN(11),
        KING(12),
        ACE(13);

        final int value;

        static HandLabel fromCharacter(@NonNull final String s) {
            return HandLabel.fromCharacter(s.charAt(0));
        }
        static HandLabel fromCharacter(final char c) {
            return switch(c) {
                case '2' -> HandLabel.TWO;
                case '3' -> HandLabel.THREE;
                case '4' -> HandLabel.FOUR;
                case '5' -> HandLabel.FIVE;
                case '6' -> HandLabel.SIX;
                case '7' -> HandLabel.SEVEN;
                case '8' -> HandLabel.EIGHT;
                case '9' -> HandLabel.NINE;
                case 'T' -> HandLabel.TEN;
                case 'J' -> HandLabel.JACK;
                case 'Q' -> HandLabel.QUEEN;
                case 'K' -> HandLabel.KING;
                case 'A' -> HandLabel.ACE;
                default -> throw new IllegalStateException();
            };
        }
    }

    @Builder
    public record Hand(String value, long bid) {
        public HandType getType() {
            final Map<HandLabel, Long> labelGroups = Arrays.stream(
                    value.split(""))
                    .collect(Collectors.groupingBy(
                            HandLabel::fromCharacter,
                            Collectors.counting()));
            final long wildcards = Optional.ofNullable(
                    labelGroups.get(HandLabel.JACK))
                    .orElse(0L);
            final long maxLabelCount = labelGroups.values()
                    .stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElseThrow();

            HandType initialHandType = HandType.HIGH_CARD;

            if (labelGroups.size() == 1) {
                return HandType.FIVE_OF_A_KIND;
            } else if (labelGroups.size() == 2) {
                if (maxLabelCount == 4) {
                    initialHandType = HandType.FOUR_OF_A_KIND;
                } else {
                    initialHandType = HandType.FULL_HOUSE;
                }
            } else if (labelGroups.size() == 3) {
                if (maxLabelCount == 3) {
                    initialHandType = HandType.THREE_OF_A_KIND;
                } else {
                    initialHandType = HandType.TWO_PAIR;
                }
            } else if (labelGroups.size() == 4) {
                initialHandType = HandType.ONE_PAIR;
            }

            if (initialHandType == HandType.FOUR_OF_A_KIND) {
                // JJJJQ OR QQQQJ
                if (wildcards > 0) {
                    return HandType.FIVE_OF_A_KIND;
                }
            }

            if (initialHandType == HandType.FULL_HOUSE) {
                // QQQJJ
                if (wildcards > 0) {
                    return HandType.FIVE_OF_A_KIND;
                }
            }

            if (initialHandType == HandType.THREE_OF_A_KIND) {
                // QQQJK | JJJQK
                if (wildcards > 0) {
                    return HandType.FOUR_OF_A_KIND;
                }
            }

            if (initialHandType == HandType.TWO_PAIR) {
                // QQKKJ
                if (wildcards == 1) {
                    return HandType.FULL_HOUSE;
                }
                // QQJJK
                if (wildcards == 2) {
                    return HandType.FOUR_OF_A_KIND;
                }
            }

            if (initialHandType == HandType.ONE_PAIR) {
                // QQKAJ
                if (wildcards > 0) {
                    return HandType.THREE_OF_A_KIND;
                }
            }

            if (initialHandType == HandType.HIGH_CARD) {
                if (wildcards > 0) {
                    return HandType.ONE_PAIR;
                }
            }

            // five of a kind
            // all same letter
            // four of a kind + 1 wild card
            // full house 2 wild cards
            // three of a kind 2 wildcards
            // high card + wildcard

            return initialHandType;
        }

        public static Comparator<Hand> handComparator() {
            return (h1, h2) -> {
                final HandType h1Type = h1.getType();
                final HandType h2Type = h2.getType();

                if (h1Type.getValue() != h2Type.getValue()) {
                    return h1Type.getValue() - h2Type.getValue();
                }

                for (int i = 0; i < 5; i++) {
                    final HandLabel h1Label = HandLabel.fromCharacter(h1.value().charAt(i));
                    final HandLabel h2Label = HandLabel.fromCharacter(h2.value().charAt(i));

                    if (h1Label.getValue() != h2Label.getValue()) {
                        return h1Label.getValue() - h2Label.getValue();
                    }
                }

                return 0;
            };
        }
    }
}

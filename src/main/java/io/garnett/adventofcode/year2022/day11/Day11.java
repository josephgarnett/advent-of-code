package io.garnett.adventofcode.year2022.day11;

import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.Builder;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day11 implements Function<List<Day11.Monkey>, Long> {

    private static final Pattern TEST_PATTERN = Pattern.compile("divisible by (\\d+)");
    private static final Pattern THROW_PATTERN = Pattern.compile("throw to monkey (\\d+)");
    private static final Pattern OPERATION_PATTERN = Pattern.compile("new = old ([+\\-/*]) (\\d+|old)");
    static final AtomicLong supermod = new AtomicLong(1);

    public static List<Monkey> getInput() throws IOException {
        final List<Monkey> result = new ArrayList<>();

        Monkey.MonkeyBuilder monkeyBuilder = Monkey.builder() ;
        final Iterator<String> iterator = InputUtils.readLines(
                "2022/day11/input.txt")
                .iterator();

        while(iterator.hasNext()) {
            final String line = iterator.next();

            if (StringUtils.isEmpty(line)) {
                result.add(monkeyBuilder.build());
                monkeyBuilder = Monkey.builder();
            } else {
                final String[] parts = line.trim().split(": ");
                if (parts.length == 1) {
                    monkeyBuilder.id(parts[0]);
                }
                if (parts.length == 2) {
                    final Matcher testMatcher = TEST_PATTERN.matcher(parts[1]);
                    final Matcher throwMatcher = THROW_PATTERN.matcher(parts[1]);
                    final Matcher operationMatcher = OPERATION_PATTERN.matcher(parts[1]);

                    if (StringUtils.startsWithIgnoreCase(
                            "starting items",
                            parts[0])) {
                        monkeyBuilder.items(Arrays.stream(
                                parts[1].trim().split(","))
                                .map(String::strip)
                                .map(Long::parseLong)
                                .map(c -> new Item(UUID.randomUUID(), c))
                                .collect(Collectors.toList()));
                    }

                    if (operationMatcher.matches()) {
                        final String operator = operationMatcher.group(1);
                        final String operand = operationMatcher.group(2);

                        monkeyBuilder.transformConcern(
                                (lhs) -> {
                                    final boolean useInputAsOperand = StringUtils.equals("old", operand);
                                    switch (operator) {
                                        case "+" -> {
                                            return useInputAsOperand
                                                    ? (lhs + lhs)
                                                    : (lhs + (Long.parseLong(operand)));
                                        }
                                        case "*" -> {
                                            return useInputAsOperand
                                                    ? (lhs * lhs)
                                                    : (lhs * (Long.parseLong(operand)));
                                        }
                                    }

                                    throw new IllegalStateException();
                                });
                    }

                    // assumes all divisible by
                    if (testMatcher.matches()) {
                        monkeyBuilder.testValue(Long.parseLong(testMatcher.group(1)));
                    }


                    if (StringUtils.startsWithIgnoreCase(
                            "if true",
                            parts[0])) {
                        if (throwMatcher.matches()) {
                            monkeyBuilder.nextMonkeyTrue(
                                    Integer.parseInt(throwMatcher.group(1)));
                        }
                    }

                    if (StringUtils.startsWithIgnoreCase(
                            "if false",
                            parts[0])) {
                        if (throwMatcher.matches()) {
                            monkeyBuilder.nextMonkeyFalse(
                                    Integer.parseInt(throwMatcher.group(1)));
                        }
                    }
                }
            }
        }

        result.add(monkeyBuilder.build());

        supermod.set(1);
        result.forEach(m -> {
            supermod.set(supermod.get() * m.testValue());
        });

        return result;
    }

    public static void main(final String[] args) {
        Application.challenge(
                "2022/day11",
                () -> new Day11().apply(Day11.getInput()));
    }

    @Override
    public Long apply(final List<Monkey> monkeys) {
        final var shenanigans = new Shenanigans(
                monkeys,
                10000,
                new HashMap<>())
                .perform();

        return shenanigans
                .ledger()
                .values()
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .reduce(1L, (acc, n) -> acc * n);
    }

    record Shenanigans(List<Monkey> monkeys, int rounds, Map<String, Long> ledger) {
        Shenanigans perform() {
            for (int r = 0; r < rounds; r++) {
                for (int i = 0; i < monkeys.size(); i++) {
                    final var m = monkeys.get(i);

                    for (final Item item: List.copyOf(m.items())) {
                        this.ledger.putIfAbsent(m.id(), 0L);
                        this.ledger.computeIfPresent(m.id(), (k, v) -> v + 1L);

                        this.next(m, m.transform(item));
                    }
                }
            }

            return this;
        }

        void next(final Monkey m, final Item i) {
            final Item toPass = i.withConcern(i.concern());

            m.items().removeIf(item -> item.id() == i.id());

            if (m.test(toPass.concern())) {
                monkeys.get(m.nextMonkeyTrue()).items().add(toPass);
            } else {
                monkeys.get(m.nextMonkeyFalse()).items().add(toPass);
            }
        }
    }

    record Item(UUID id, long concern) {
        Item withConcern(final long concern) {
            return new Item(
                    this.id(),
                    concern);
        }
    }

    record Monkey(
            @NonNull String id,
            @NonNull List<Item> items,
            long testValue,
            @NonNull Function<Long, Long> transformConcern,
            int nextMonkeyTrue,
            int nextMonkeyFalse) {

        @Builder public Monkey {}

        public Item transform(final Item old) {
            final var newConcern = this.transformConcern.apply(old.concern());

            if (newConcern < 0) {
                System.out.println("OVERFLOW");
            }
            return old.withConcern(newConcern % supermod.get());
        }

        public boolean test(final long concern) {
            return concern % this.testValue() == 0;
        }
    }
}

package com.mtab.adventofcode.year2021.day8;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.mtab.adventofcode.models.InputListLoader;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day8 implements
        InputListLoader<Day8.Signal>,
        Supplier<Integer> {

    private final List<Day8.Signal> input;

    public Day8(final String path) {
        this.input = this.getInput(path);
    }

    @Override
    public Integer get() {
        return this.input
                .stream()
                .mapToInt(Day8.Signal::getValue)
                .peek(System.out::println)
                .sum();
    }

    @Override
    public Day8.Signal parseLine(final String line, final int index) {
        final String[] parts = line.split("\\s+\\|\\s+");
        final String[] signal = parts[0].split("\\s+");
        final String[] output = parts[1].split("\\s+");

        return new Signal(signal, output);
    }

    public static void main(final String... args) {
        Day8.part1();
    }

    public static void part1() {
        final Stopwatch sw = Stopwatch.createStarted();
        final String inputPath = "2021/day8/input.txt";
        final int result = new Day8(inputPath).get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    public static class Signal {
        private final List<String> signal;
        private final List<Hash> output;

        private final HashMap<Hash, Integer> codeTable = new HashMap<>();

        public Signal(final String[] signal, final String[] output) {
            this.signal = ImmutableList.copyOf(signal);
            this.output = ImmutableList.copyOf(
                    Stream.of(output)
                            .map(Hash::new)
                            .collect(Collectors.toList()));

            final String[] display = this.display(this.signal);

            this.codeTable.put(new Hash(display[0] + display[1] + display[2] + display[3] + display[4] + display[5]), 0);
            this.codeTable.put(new Hash(display[3] + display[4] + display[6] + display[1] + display[0]), 2);
            this.codeTable.put(new Hash(display[3] + display[4] + display[6] + display[5] + display[0]), 3);
            this.codeTable.put(new Hash(display[3] + display[2] + display[6] + display[5] + display[0]), 5);
            this.codeTable.put(new Hash(display[3] + display[2] + display[6] + display[5] + display[0] + display[1]), 6);
            this.codeTable.put(new Hash(display[3] + display[2] + display[6] + display[4] + display[5] + display[0]), 9);
        }

        private String[] display(final List<String> signals) {
            /*
             *              3333
             *             2    4
             *             2    4
             *              6666
             *             1    5
             *             1    5
             *              0000
             */
            final Map<Integer, List<BitSet>> grouped = new HashMap<>(10);

            for (final String s: signals) {
                grouped.computeIfPresent(s.length(), (k, v) -> {
                    v.add(this.stringToBitset(s));

                    return v;
                });
                grouped.computeIfAbsent(s.length(), (k) -> {
                    final List<BitSet> r = new ArrayList<>();

                    r.add(this.stringToBitset(s));

                    return r;
                });
            }

            final String[] result = new String[7];

            final BitSet one = grouped.get(2).get(0);
            final BitSet seven = grouped.get(3).get(0);
            final BitSet four = grouped.get(4).get(0);
            final BitSet eight = grouped.get(7).get(0);

            final BitSet six = grouped.get(6)
                    .stream()
                    .filter((b) -> {
                        final BitSet t = new BitSet();
                        t.or(b);
                        t.and(one);

                        return t.cardinality() == 1;
                    })
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            final BitSet nine = grouped.get(6)
                    .stream()
                    .filter(b -> {
                        final BitSet t = new BitSet();
                        t.or(b);
                        t.and(four);

                        return t.cardinality() == 4;
                    })
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            final BitSet zero = grouped.get(6)
                    .stream()
                    .filter(b -> b != nine && b != six)
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            final BitSet displayPos0 = new BitSet();
            final BitSet displayPos1 = new BitSet();
            final BitSet displayPos2 = new BitSet();
            final BitSet displayPos3 = new BitSet();
            final BitSet displayPos4 = new BitSet();
            final BitSet displayPos5 = new BitSet();
            final BitSet displayPos6 = new BitSet();

            displayPos4.or(six);
            displayPos4.xor(eight);
            displayPos5.or(one);
            displayPos5.xor(displayPos4);

            displayPos3.or(one);
            displayPos3.xor(seven);

            displayPos2.or(four);
            displayPos2.xor(one);
            displayPos6.or(displayPos2);

            displayPos1.or(nine);
            displayPos1.xor(eight);

            displayPos0.or(four);
            displayPos0.or(seven);
            displayPos0.xor(eight);
            displayPos0.and(nine);

            displayPos6.or(eight);
            displayPos6.xor(zero);

            displayPos2.xor(displayPos6);

            result[0] = Character.toString(displayPos0.nextSetBit(0));
            result[1] = Character.toString(displayPos1.nextSetBit(0));
            result[2] = Character.toString(displayPos2.nextSetBit(0));
            result[3] = Character.toString(displayPos3.nextSetBit(0));
            result[4] = Character.toString(displayPos4.nextSetBit(0));
            result[5] = Character.toString(displayPos5.nextSetBit(0));
            result[6] = Character.toString(displayPos6.nextSetBit(0));

            return result;
        }

        private BitSet stringToBitset(final String s) {
            final BitSet b = new BitSet();

            for (int i = 0; i < s.length(); ++i) {
                final String letter = s.substring(i, i + 1);
                b.set(letter.codePointAt(0));
            }

            return b;
        }

        public List<Hash> getOutput() {
            return this.output;
        }

        public List<String> getSignal() {
            return this.signal;
        }

        public int getValue() {
            return Integer.parseInt(
                    this.output
                            .stream()
                            .map(n -> {
                                final int len = n.toString().length();
                                if (len == 2) {
                                    return 1;
                                }

                                if (len == 4) {
                                    return 4;
                                }

                                if (len == 3) {
                                    return 7;
                                }

                                if (len == 7) {
                                    return 8;
                                }

                                return this.codeTable.get(n);
                            })
                            .map(Object::toString)
                            .collect(Collectors.joining("")));
        }
    }

    public static class Hash {
        private final String decoded;
        private final int hashCode;

        public Hash(final String s) {
            this.decoded = s;
            this.hashCode = s.chars().sorted().reduce(1, (acc, e) -> acc * e);
        }

        @Override
        public String toString() {
            return this.decoded;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj instanceof Hash) {
                return this.hashCode == ((Hash) obj).hashCode;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }
}

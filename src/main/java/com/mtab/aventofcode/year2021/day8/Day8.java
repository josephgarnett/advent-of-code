package com.mtab.aventofcode.year2021.day8;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.mtab.aventofcode.models.InputListLoader;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
        final String inputPath = "2021/day8/test.txt";
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
            this.codeTable.put(new Hash(display[5] + display[4]), 1);
            this.codeTable.put(new Hash(display[3] + display[4] + display[6] + display[1] + display[0]), 2);
            this.codeTable.put(new Hash(display[3] + display[4] + display[6] + display[5] + display[0]), 3);
            this.codeTable.put(new Hash(display[2] + display[6] + display[4] + display[5]), 4);
            this.codeTable.put(new Hash(display[3] + display[2] + display[6] + display[5] + display[0]), 5);
            this.codeTable.put(new Hash(display[3] + display[2] + display[6] + display[5] + display[0] + display[1]), 6);
            this.codeTable.put(new Hash(display[3] + display[4] + display[5]), 7);
            this.codeTable.put(new Hash(display[0] + display[1] + display[2] + display[3] + display[4] + display[5] + display[6]), 8);
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
            final String[] result = new String[7];
            final List<String> sigs = new ArrayList<>(signals);

            sigs.sort(Comparator.comparing(String::length));

            // TODO: refine logic to provide accurate display
            for (final String s: sigs) {
                final int len = s.length();
                final String[] letters = s.split("");

                // overlay 1 & 7
                // get value for 3 + 2 possible values for 4 & 5
                // overlay 3 get value for 0
                // overlay 2 get value for 4 & 5 & 6 & 2
                // overlay 5 get value for 1

                if (len == 2) {
                    result[4] = letters[0];
                    result[5] = letters[1];
                    continue;
                }

                if (len == 3) {
                    if (!ArrayUtils.contains(result, letters[0])) {
                        result[3] = letters[0];
                    }
                    if (!ArrayUtils.contains(result, letters[1])) {
                        result[3] = letters[1];
                    }
                    if (!ArrayUtils.contains(result, letters[2])) {
                        result[3] = letters[2];
                    }
                    continue;
                }

                if (len == 4) {
                    result[2] = letters[0];
                    result[6] = letters[2];
                    continue;
                }

                if (len == 6 && !s.contains(result[4])) {
                    result[0] = letters[0];
                    result[1] = letters[3];
                }
            }

            return result;
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
                            .map(this.codeTable::get)
                            .peek(System.out::println)
                            .map(Object::toString)
                            .collect(Collectors.joining("")));
        }
    }

    public static class Hash {
        private final String decoded;
        private final int hashCode;

        public Hash(final String s) {
            this.decoded = s;
            this.hashCode = s.chars().sum();
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

package com.mtab.aventofcode.year2021.day16;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.mtab.aventofcode.models.InputLoader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class Day16 implements
        InputLoader<List<Day16.Packet>>,
        Supplier<Long> {

    private final List<Packet> input;

    private Day16(final String resourcePath) {
        this.input = this.getInput(resourcePath);
    }

    @Override
    public List<Packet> transformResource(BufferedReader bufferedReader) throws IOException {
        return Day16.Packet.decode(bufferedReader.readLine());
    }

    @Override
    public Long get() {
        return this.input
                .stream()
                .mapToLong(Packet::sumVersions)
                .sum();
    }

    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final var result = new Day16("2021/day16/input.txt").get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    public static class Packet {
        private final long version;
        private final long packetId;

        private final long value;

        // enum to cope with packet instructions
        // private final PacketType type;

        private final List<Packet> subPackets;

        public Packet(
                final long version,
                final long packetId,
                final long value,
                final List<Packet> subPackets) {
            this.version = version;
            this.packetId = packetId;
            this.value = value;
            this.subPackets = ImmutableList.copyOf(subPackets);
        }

        public long getVersion() {
            return this.version;
        }

        public long sumVersions() {
            return this.getVersion() + this.subPackets
                    .stream()
                    .mapToLong(Packet::sumVersions)
                    .sum();
        }

        public enum PacketType {
            LITERAL(4),
            OPERATOR(6);

            private final int value;
            PacketType(final int value) {
                this.value = value;
            }

            public int getValue() {
                return this.value;
            }
        }

        public static AtomicReference<String> decodeToBinaryString(final String encoded) {
            final String[] letters = encoded.split("");
            final StringBuilder binaryStringBuilder = new StringBuilder(letters.length * 4);

            for (final String letter: letters) {
                binaryStringBuilder.append(
                        StringUtils.leftPad(
                                Long.toBinaryString(
                                        Long.parseLong(
                                                letter,
                                                16)),
                                4,
                                '0'));
            }

            return new AtomicReference<>(binaryStringBuilder.toString());
        }

        public static Pattern ZEROES = Pattern.compile("^0+$");

        public static List<Packet> decode(final String encoded) {
            return Packet.decodeFromBinaryString(Packet.decodeToBinaryString(encoded));
        }

        public static List<Packet> decodeFromBinaryString(final AtomicReference<String> binary) {
            final List<Packet> result = new ArrayList<>();

            final AtomicReference<String> consumed = binary;

            while(consumed.get().length() > 0) {
                if (ZEROES.matcher(consumed.get()).matches()) {
                    return result;
                }

                final List<Packet> subPackets = new ArrayList<>();
                long literalValue = -1;

                final long version = Long.parseLong(consumed.get().substring(0, 3), 2);
                consumed.set(consumed.get().substring(3));
                final long packetId = Long.parseLong(consumed.get().substring(0, 3), 2);
                consumed.set(consumed.get().substring(3));

                if (Packet.isOperator(packetId)) {
                    final long packetLengthId = Long.parseLong(consumed.get().substring(0, 1), 2);
                    consumed.set(consumed.get().substring(1));

                    int packetLength = 0;

                    // 11 bits
                    if (packetLengthId == 1) {
                        packetLength = Integer.parseInt(consumed.get().substring(0, 11), 2);
                        consumed.set(consumed.get().substring(11));

                        subPackets.addAll(Packet.decodeFromBinaryString(consumed));
                    }

                    // 15 bits
                    if (packetLengthId == 0) {
                        packetLength = Integer.parseInt(consumed.get().substring(0, 15), 2);
                        consumed.set(consumed.get().substring(15));

                        final String sub = consumed.get().substring(0, packetLength);
                        consumed.set(consumed.get().substring(packetLength));
                        subPackets.addAll(Packet.decodeFromBinaryString(new AtomicReference<>(sub)));
                    }

                    if (packetLength == 0) {
                        throw new IllegalStateException("Unrecognized packet length ID");
                    }
                }

                if (isLiteral(packetId)) {
                    final StringBuilder literal = new StringBuilder();

                    String marker;
                    do {
                        marker = consumed.get().substring(0, 1);
                        consumed.set(consumed.get().substring(1));
                        literal.append(consumed.get(), 0, 4);
                        consumed.set(consumed.get().substring(4));
                    } while(!StringUtils.equals("0", marker));

                    literalValue = Long.parseLong(literal.toString(), 2);
                }

                result.add(new Packet(version, packetId, literalValue, subPackets));
            }
            return result;
        }

        public static boolean isOperator(final long packetId) {
            return packetId != 4;
        }

        public static boolean isLiteral(final long packetId) {
            return packetId == 4;
        }
    }
}

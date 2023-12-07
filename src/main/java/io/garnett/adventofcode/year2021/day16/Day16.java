package io.garnett.adventofcode.year2021.day16;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import io.garnett.adventofcode.models.InputLoader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
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
                .mapToLong(Packet::compute)
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

        private final PacketType type;

        private final List<Packet> subPackets;

        public Packet(
                final long version,
                final long packetId,
                final long value,
                final List<Packet> subPackets) {
            this.version = version;
            this.packetId = packetId;
            this.type = PacketType.fromValue((int)packetId);
            this.value = value;
            this.subPackets = ImmutableList.copyOf(subPackets);
        }

        public long getVersion() {
            return this.version;
        }

        public List<Packet> getSubPackets() {
            return this.subPackets;
        }

        public long getValue() {
            return this.value;
        }

        public long compute() {
            return this.type.apply(this);
        }

        public long sumVersions() {
            return this.getVersion() + this.subPackets
                    .stream()
                    .mapToLong(Packet::sumVersions)
                    .sum();
        }

        public enum PacketType implements Function<Packet, Long> {
            SUM(0) {
                @Override
                public Long apply(final Packet packet) {
                    return packet.getSubPackets()
                            .stream()
                            .mapToLong(Packet::compute)
                            .sum();
                }
            },
            PRODUCT(1) {
                @Override
                public Long apply(final Packet packet) {
                    return packet.getSubPackets()
                            .stream()
                            .mapToLong(Packet::compute)
                            .reduce(1, (acc, v) -> acc * v);
                }
            },
            MINIMUM(2) {
                @Override
                public Long apply(final Packet packet) {
                    return packet.getSubPackets()
                            .stream()
                            .mapToLong(Packet::compute)
                            .min()
                            .orElseThrow(RuntimeException::new);
                }
            },
            MAXIMUM(3) {
                @Override
                public Long apply(final Packet packet) {
                    return packet.getSubPackets()
                            .stream()
                            .mapToLong(Packet::compute)
                            .max()
                            .orElseThrow(RuntimeException::new);
                }
            },
            LITERAL(4) {
                @Override
                public Long apply(final Packet packet) {
                    return packet.getValue();
                }
            },
            GREATER_THAN(5) {
                @Override
                public Long apply(final Packet packet) {
                    if (packet.getSubPackets().size() == 2) {
                        if(packet.getSubPackets().get(0).compute() > packet.getSubPackets().get(1).compute()) {
                           return 1L;
                        }
                        return 0L;
                    }

                    return 0L;
                }
            },
            LESS_THAN(6) {
                @Override
                public Long apply(final Packet packet) {
                    if (packet.getSubPackets().size() == 2) {
                        if(packet.getSubPackets().get(0).compute() < packet.getSubPackets().get(1).compute()) {
                            return 1L;
                        }
                        return 0L;
                    }

                    return 0L;
                }
            },
            EQUAL(7) {
                @Override
                public Long apply(final Packet packet) {
                    if (packet.getSubPackets().size() == 2) {
                        if(packet.getSubPackets().get(0).compute() == packet.getSubPackets().get(1).compute()) {
                            return 1L;
                        }
                        return 0L;
                    }

                    return 0L;
                }
            };

            private final int value;
            PacketType(final int value) {
                this.value = value;
            }

            public int getValue() {
                return this.value;
            }

            public static PacketType fromValue(final int value) {
                switch(value) {
                    case 0:
                        return PacketType.SUM;
                    case 1:
                        return PacketType.PRODUCT;
                    case 2:
                        return PacketType.MINIMUM;
                    case 3:
                        return PacketType.MAXIMUM;
                    case 4:
                        return PacketType.LITERAL;
                    case 5:
                        return PacketType.GREATER_THAN;
                    case 6:
                        return PacketType.LESS_THAN;
                    case 7:
                        return PacketType.EQUAL;
                }

                throw new IllegalStateException("Value does not translate to PacketType");
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
            return Packet.decodeFromBinaryString(Packet.decodeToBinaryString(encoded), 0);
        }

        public static List<Packet> decodeFromBinaryString(final AtomicReference<String> binary, final int limit) {
            final List<Packet> result = new ArrayList<>();
            final AtomicReference<String> consumed = binary;

            int decoded = 0;

            while(consumed.get().length() > 0) {

                if (limit != 0 && decoded == limit) {
                    return result;
                }

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

                        subPackets.addAll(Packet.decodeFromBinaryString(consumed, packetLength));
                    }

                    // 15 bits
                    if (packetLengthId == 0) {
                        packetLength = Integer.parseInt(consumed.get().substring(0, 15), 2);
                        consumed.set(consumed.get().substring(15));

                        final String sub = consumed.get().substring(0, packetLength);
                        consumed.set(consumed.get().substring(packetLength));
                        subPackets.addAll(Packet.decodeFromBinaryString(new AtomicReference<>(sub), 0));
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
                decoded++;
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

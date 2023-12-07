package io.garnett.adventofcode.utils;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CustomCollectors {
    public static <T>
    Collector<List<T>, Set<T>, Set<T>> collectIntersection() {
        return new Collector<>() {
            @Override
            public Supplier<Set<T>> supplier() {
                return HashSet::new;
            }

            @Override
            public BiConsumer<Set<T>, List<T>> accumulator() {
                return (acc, v) -> {
                    if (acc.isEmpty()) {
                        acc.addAll(v);
                    } else {
                        acc.retainAll(v);
                    }
                };
            }

            @Override
            public BinaryOperator<Set<T>> combiner() {
                return (set1, set2) -> {
                    set1.addAll(set2);
                    return set1;
                };
            }

            @Override
            public Function<Set<T>, Set<T>> finisher() {
                return Set::copyOf;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Sets.immutableEnumSet(Characteristics.UNORDERED);
            }
        };
    }

    public static
    Collector<Integer, BitSet, BitSet> toBitSet() {
        return new Collector<>() {
            @Override
            public Supplier<BitSet> supplier() {
                return BitSet::new;
            }

            @Override
            public BiConsumer<BitSet, Integer> accumulator() {
                return BitSet::set;
            }

            @Override
            public BinaryOperator<BitSet> combiner() {
                return (set1, set2) -> {
                    set1.or(set2);

                    return set1;
                };
            }

            @Override
            public Function<BitSet, BitSet> finisher() {
                return bitSet -> bitSet;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Sets.immutableEnumSet(Characteristics.UNORDERED);
            }
        };
    }

    public static <T>
    Collector<T, Map<String, Set<T>>, Map<String, Set<T>>> collectFileTagGroups(
            final Function<T, Collection<String>> keyAccessor) {
        return Collector.of(
                HashMap::new,
                (map, item) -> keyAccessor.apply(item)
                        .forEach((key) -> {
                            map.computeIfAbsent(key, (k) -> {
                                final Set<T> set = new HashSet<>();
                                set.add(item);
                                return set;
                            });

                            map.computeIfPresent(key, (k, v) -> {
                                v.add(item);
                                return v;
                            });
                        }),
                (map1, map2) -> {
                    map1.putAll(map2);
                    return map1;
                },
                Map::copyOf);
    }
}

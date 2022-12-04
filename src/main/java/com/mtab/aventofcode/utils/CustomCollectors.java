package com.mtab.aventofcode.utils;

import com.google.common.collect.Sets;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
}

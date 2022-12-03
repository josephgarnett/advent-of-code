package com.mtab.aventofcode.utils;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CustomCollectors {
    public static final Collector<List<String>, Set<String>, Set<String>> collectIntersection = new Collector<>() {
        @Override
        public Supplier<Set<String>> supplier() {
            return HashSet::new;
        }

        @Override
        public BiConsumer<Set<String>, List<String>> accumulator() {
            return (acc, v) -> {
                if (acc.isEmpty()) {
                    acc.addAll(v);
                } else {
                    acc.retainAll(v);
                }
            };
        }

        @Override
        public BinaryOperator<Set<String>> combiner() {
            return (set1, set2) -> {
                set1.addAll(set2);
                return set1;
            };
        }

        @Override
        public Function<Set<String>, Set<String>> finisher() {
            return Set::copyOf;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Sets.immutableEnumSet(Characteristics.UNORDERED);
        }
    };
}

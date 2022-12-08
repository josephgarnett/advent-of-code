package com.mtab.adventofcode.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class InputUtils {

    public static String getInputPath(final String path) {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return Objects.requireNonNull(classloader.getResource(path)).getPath();
    }

    public static class StringCollator implements Collector<String, List<String>, List<List<String>>> {
        @Override
        public Supplier<List<String>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<String>, String> accumulator() {
            return List::add;
        }

        @Override
        public BinaryOperator<List<String>> combiner() {
            return (left, right) -> left;
        }

        @Override
        public Function<List<String>, List<List<String>>> finisher() {
            return (list) -> {
                final List<List<String>> result = new ArrayList<>();
                final List<String> temp = new ArrayList<>();

                for (final String e: list) {
                    if (StringUtils.equals(StringUtils.EMPTY, e)) {
                        result.add(List.copyOf(temp));
                        temp.clear();
                    } else {
                        temp.add(e);
                    }
                }

                return result;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of();
        }
    }
}

package com.mtab.adventofcode.utils;

import java.util.function.BinaryOperator;

public class CustomErrors {
    public static  <T>
    BinaryOperator<T> notImplementedCombiner() {
        return (a, b) -> {
            throw new IllegalCallerException("Combiner not implemented");
        };
    }
}

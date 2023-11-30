package com.mtab.adventofcode.year2023.day1;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;

import java.util.function.IntSupplier;

public class Day1 implements IntSupplier {

    @Override
    public int getAsInt() {
        return 1;
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day1",
                () -> new Day1()
                        .getAsInt());

        Preconditions.checkArgument(result == 1);
    }
}

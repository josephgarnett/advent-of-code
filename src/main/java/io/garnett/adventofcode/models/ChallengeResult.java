package io.garnett.adventofcode.models;

import java.util.Objects;
import java.util.function.Supplier;

public record ChallengeResult<T>(
        T value,
        Supplier<String> stringify
) {
    @Override
    public boolean equals(Object other) {
        return Objects.equals(this.value, other);
    }

    @Override
    public String toString() {
        return this.stringify.get();
    }
}

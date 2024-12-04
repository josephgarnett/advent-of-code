package io.garnett.adventofcode.models;

import lombok.NonNull;

public interface ChallengeSolution<T, R> {
    public ChallengeResult<R> solve(@NonNull final T input);
}

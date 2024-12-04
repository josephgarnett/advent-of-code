package io.garnett.adventofcode.models;

import lombok.NonNull;

public interface ChallengeSolution<T, R> {
    ChallengeResult<R> solve(@NonNull final T input);
}

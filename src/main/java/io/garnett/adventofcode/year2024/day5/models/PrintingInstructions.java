package io.garnett.adventofcode.year2024.day5.models;

import lombok.Builder;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.Map;

@Builder(toBuilder = true)
public record PrintingInstructions(
        @NonNull  Map<Integer, List<ImmutablePair<Integer, Integer>>> pageOrdering,
        @NonNull List<List<Integer>> printJobs) {
}

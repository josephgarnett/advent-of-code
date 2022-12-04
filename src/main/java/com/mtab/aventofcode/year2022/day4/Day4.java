package com.mtab.aventofcode.year2022.day4;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.CustomCollectors;
import com.mtab.aventofcode.utils.InputUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day4 implements Function<List<Day4.GroupAssignment>, Long> {
    private static List<Day4.GroupAssignment> getInput() throws IOException {
        return Files.lines(
                        Path.of(InputUtils.getInputPath("2022/day4/input.txt")))
                .map((line) -> {
                    final String[] elves = line.split(",");
                    final List<Assignment> assignments = new ArrayList<>();

                    for (final String elf: elves) {
                        final String[] sections = elf.split("-");

                        assignments.add(
                                Assignment.of(
                                        Integer.parseInt(sections[0]),
                                        Integer.parseInt(sections[1])));
                    }

                    return new GroupAssignment(assignments);
                })
                .toList();
    }

    public static void main(final String[] args) throws IOException {
        final Stopwatch sw = Stopwatch.createStarted();

        final var result = new Day4().apply(Day4.getInput());

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));

        Preconditions.checkArgument(result == 852);
    }

    @Override
    public Long apply(final List<GroupAssignment> assignments) {
        return assignments
                .stream()
                .filter(GroupAssignment::areOverlapping)
                .count();
    }

    record GroupAssignment(List<Assignment> assignments) {

        boolean areOverlapping() {
            final Assignment assignment1 = this.assignments.get(0);
            final Assignment assignment2 = this.assignments.get(1);

            return assignment1.sections()
                    .intersects(assignment2.sections());
        }
    }

    record Assignment(int start, int end, BitSet sections) {
        static Assignment of(final int start, final int end) {
            final var sections = IntStream.range(start, end + 1)
                    .boxed()
                    .collect(CustomCollectors.toBitSet());
            return new Assignment(start, end, sections);
        }
    }
}

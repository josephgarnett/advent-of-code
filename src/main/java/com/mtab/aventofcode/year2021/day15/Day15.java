package com.mtab.aventofcode.year2021.day15;

import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.models.InputGridLoader;
import com.mtab.aventofcode.models.grid.IntGrid;
import com.mtab.aventofcode.models.grid.IntPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Day15 implements
        InputGridLoader<IntPoint, IntGrid>,
        Supplier<Integer> {

    private final IntGrid grid;

    private Day15(final String resourcePath) {
        this.grid = this.getInput(resourcePath);
    }

    @Override
    public IntPoint parseElement(String e, int x, int y) {
        return new IntPoint(Integer.parseInt(e), x, y);
    }

    @Override
    public IntGrid createGrid(List<IntPoint> points) {
        return new IntGrid(points);
    }

    @Override
    public Integer get() {
        final List<IntPoint> visited = new ArrayList<>();
        visited.add(this.grid.getPointAt(0, 0));
        final List<Path> paths = this.pathfinder(visited.get(0), visited);

        return paths
                .stream()
                .filter(Path::isComplete)
                .mapToInt(Path::riskIndex)
                .min()
                .orElseThrow(RuntimeException::new);
    }

    // TODO: need to know grid size for bottom right position
    // TODO: create Path class records visited nodes and if the path completes

    // rules if there is a neighbour value 1 its worth following
    // otherwise lookahead 2 and sum
    // prefer not to go upwards
    private List<Path> pathfinder(final IntPoint start, final List<IntPoint> visited) {
        visited.add(start);
        if (start == this.grid.getPointAt(9, 9)) {
            return List.of(new Path(visited, true));
        }

        final List<Path> result = new ArrayList<>();
        final List<IntPoint> neighbours = this.grid.getCardinalNeighbours(start);

        // remove N from neighbours

        for (final IntPoint p : neighbours) {
            if (visited.contains(p)) {
                continue;
            }

            if (p.getValue().get() == 1) {
                result.addAll(this.pathfinder(p, new ArrayList<>(visited)));
            }
        }

        // of points which have not been visited or are 1 - lookahead 2 and sum numbers
        // if cannot proceed in same axis look round the corner

        return result;
    }

    public static void main(final String... args) {
        final var sw = Stopwatch.createStarted();
        final long result = new Day15("2021/day15/test.txt").get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    private static class Path {
        private final List<IntPoint> points;
        private final boolean isComplete;

        public Path(final List<IntPoint> points, final boolean isComplete) {
            this.points = List.copyOf(points);
            this.isComplete = isComplete;
        }

        public boolean isComplete() {
            return this.isComplete;
        }

        public List<IntPoint> getPoints() {
            return this.points;
        }

        public int riskIndex() {
            return this.points
                    .stream()
                    .mapToInt(p -> p.getValue().get())
                    .sum();
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("points", this.getPoints())
                    .add("isComplete", this.isComplete)
                    .toString();
        }
    }
}

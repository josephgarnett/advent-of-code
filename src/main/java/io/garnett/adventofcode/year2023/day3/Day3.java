package io.garnett.adventofcode.year2023.day3;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day3 implements Function<Day3.Schematic, Long> {
    @Override
    public Long apply(
            @NonNull final Schematic schematic) {
        return this.part2(schematic);
    }

    private long part2(
            @NonNull final Schematic schematic) {
        final List<SchematicPoint> gears = schematic.points()
                .stream()
                .filter(SchematicPoint::isGear)
                .toList();

        return gears
                .stream()
                .map(g -> schematic.regions()
                        .stream()
                        .filter(r -> r.isInRange(g))
                        .toList())
                .filter(l -> l.size() == 2)
                .mapToLong(l -> l.get(0).getValue() * l.get(1).getValue())
                .sum();
    }

    private long part1(
            @NonNull final Schematic schematic) {
        final List<SchematicPoint> symbols = schematic.points()
                .stream()
                .filter(SchematicPoint::isSymbol)
                .toList();

        return schematic.regions()
                .stream()
                .filter(r -> r.isValid(symbols))
                .mapToLong(SchematicRegion::getValue)
                .sum();
    }

    private static Schematic getInput() throws IOException {
        final AtomicInteger rowTracker = new AtomicInteger(0);
        return InputUtils.readLines("2023/day3/input.txt")
                .map(line -> {
                    final int row = rowTracker.getAndIncrement();
                    final List<SchematicPoint> rowPoints = new ArrayList<>();
                    final String[] parts = line.split("");
                    final List<SchematicRegion> regions = new ArrayList<>();

                    List<SchematicPoint> region = new ArrayList<>();

                    for (int i = 0; i < parts.length; i++) {
                        final String value = parts[i];
                        final SchematicPoint p = new SchematicPoint(value, i, row);

                        if (!".".equals(value)) {
                            rowPoints.add(p);
                        }

                        if (StringUtils.isNumeric(value)) {
                            region.add(p);

                            if (i == parts.length - 1) {
                                regions.add(new SchematicRegion(List.copyOf(region)));
                            }
                        } else {
                            if (!region.isEmpty()) {
                                regions.add(new SchematicRegion(List.copyOf(region)));
                            }

                            region = new ArrayList<>();
                        }
                    }

                    return new Schematic(regions, rowPoints);
                })
                .reduce(new Schematic(List.of(), List.of()), Schematic::combine);
    }
    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day3",
                i -> {
                    final Schematic input = Day3.getInput();
                    return new Day3()
                            .apply(input);
                });

        Preconditions.checkArgument(result == 84900879);
    }

    record SchematicPoint(String value, int x, int y) {
        boolean isSymbol() {
            return !StringUtils.isNumeric(this.value());
        }
        boolean isGear() {
            return "*".equals(this.value());
        }
    }

    @Getter
    static class SchematicRegion {
        private final List<SchematicPoint> points;
        private final int minX;
        private final int maxX;
        private final int minY;
        private final int maxY;

        public SchematicRegion(
                @NonNull final List<SchematicPoint> points) {
            this.points = points;
            this.minX = this.points
                    .stream()
                    .mapToInt(SchematicPoint::x)
                    .min()
                    .orElse(0) - 1;
            this.maxX = this.points
                    .stream()
                    .mapToInt(SchematicPoint::x)
                    .max()
                    .orElse(0) + 1;
            this.minY = this.points
                    .stream()
                    .mapToInt(SchematicPoint::y)
                    .min()
                    .orElse(0) - 1;
            this.maxY = this.points
                    .stream()
                    .mapToInt(SchematicPoint::y)
                    .max()
                    .orElse(0) + 1;
        }
        public int getValue() {
            return Integer.parseInt(this.getPoints()
                    .stream()
                    .map(SchematicPoint::value)
                    .collect(Collectors.joining("")));
        }

        public boolean isInRange(@NonNull final SchematicPoint p) {
            final int minX = this.getMinX();
            final int maxX = this.getMaxX();
            final int minY = this.getMinY();
            final int maxY = this.getMaxY();
            return p.y() >= minY
                    && p.y() <= maxY
                    && p.x() >= minX
                    && p.x() <= maxX;
        }

        public boolean isValid(List<SchematicPoint> points) {
            return points
                    .stream()
                    .anyMatch(this::isInRange);
        }
    }
    public record Schematic(List<SchematicRegion> regions, List<SchematicPoint> points) {

        public Schematic combine(@NonNull Schematic other) {
            final var regions = new ArrayList<SchematicRegion>();
            final var points = new ArrayList<SchematicPoint>();

            regions.addAll(this.regions());
            points.addAll(this.points());

            if (!other.regions().isEmpty()) {
                regions.addAll(other.regions());
            }

            if (!other.points().isEmpty()) {
                points.addAll(other.points());
            }

            return new Schematic(regions, points);
        }
    }
}

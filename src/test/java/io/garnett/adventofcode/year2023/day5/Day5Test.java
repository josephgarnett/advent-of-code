package io.garnett.adventofcode.year2023.day5;


import org.junit.Test;
import org.junit.Assert;

import java.util.List;
import java.util.Optional;

public class Day5Test {
    @Test
    public void seedToBuckets_simple() {
        final Day5.Seed seed = new Day5.Seed(List.of(
                new Day5.Bucket(79, 14)));
        final List<Day5.Range> ranges = List.of(
                new Day5.Range(
                        new Day5.Bucket(98, 2),
                        new Day5.Bucket(50, 2)),
                new Day5.Range(
                        new Day5.Bucket(50, 48),
                        new Day5.Bucket(52, 48)));

        final var result = seed.toBuckets(ranges);

        Assert.assertEquals(
                result,
                List.of(new Day5.Bucket(81, 14)));
    }

    @Test
    public void seedToBuckets_simple2() {
        final Day5.Seed seed = new Day5.Seed(List.of(
                new Day5.Bucket(55, 13)));
        final List<Day5.Range> ranges = List.of(
                new Day5.Range(
                        new Day5.Bucket(98, 2),
                        new Day5.Bucket(50, 2)),
                new Day5.Range(
                        new Day5.Bucket(50, 48),
                        new Day5.Bucket(52, 48)));

        final var result = seed.toBuckets(ranges);

        Assert.assertEquals(
                result,
                List.of(new Day5.Bucket(57, 13)));
    }

    @Test
    public void seedToBuckets_light_to_temp() {
        final Day5.Seed seed = new Day5.Seed(List.of(
                new Day5.Bucket(74, 14)));
        final List<Day5.Range> ranges = List.of(
                new Day5.Range(
                        new Day5.Bucket(77, 23),
                        new Day5.Bucket(45, 23)),
                new Day5.Range(
                        new Day5.Bucket(45, 19),
                        new Day5.Bucket(81, 19)),
                new Day5.Range(
                        new Day5.Bucket(64, 13),
                        new Day5.Bucket(68, 13)));

        final var result = seed.toBuckets(ranges);

        Assert.assertEquals(
                List.of(
                        new Day5.Bucket(45, 11),
                        new Day5.Bucket(78, 3)),
                result);
    }

    @Test
    public void seedToBuckets_temp_to_humidity() {
        final Day5.Seed seed = new Day5.Seed(List.of(
                new Day5.Bucket(74, 14)));
        final List<Day5.Range> ranges = List.of(
                new Day5.Range(
                        new Day5.Bucket(60, 1),
                        new Day5.Bucket(0, 1)),
                new Day5.Range(
                        new Day5.Bucket(0, 69),
                        new Day5.Bucket(1, 69)));

        final var result = seed.toBuckets(ranges);

        Assert.assertEquals(
                seed.buckets(),
                result);
    }

    @Test
    public void seedToBuckets_humidity_to_location() {
        final Day5.Seed seed = new Day5.Seed(List.of(
                new Day5.Bucket(81, 11),
                new Day5.Bucket(78, 3)));
        final List<Day5.Range> ranges = List.of(
                new Day5.Range(
                        new Day5.Bucket(56, 37),
                        new Day5.Bucket(60, 37)),
                new Day5.Range(
                        new Day5.Bucket(93, 4),
                        new Day5.Bucket(56, 4)));

        final var result = seed.toBuckets(ranges);
        Assert.assertEquals(
                List.of(
                        new Day5.Bucket(85, 11),
                        new Day5.Bucket(82, 3)),
                result);
    }

    @Test
    public void seedToBuckets_fertilizer_to_water() {
        final Day5.Seed seed = new Day5.Seed(List.of(
                new Day5.Bucket(57, 13)));
        final List<Day5.Range> ranges = List.of(
                new Day5.Range(
                        new Day5.Bucket(53, 8),
                        new Day5.Bucket(49, 8)),
                new Day5.Range(
                        new Day5.Bucket(11, 42),
                        new Day5.Bucket(0, 42)),
                new Day5.Range(
                        new Day5.Bucket(0, 7),
                        new Day5.Bucket(42, 7)),
                new Day5.Range(
                        new Day5.Bucket(7, 4),
                        new Day5.Bucket(57, 4)));

        final var result = seed.toBuckets(ranges);
        Assert.assertEquals(
                List.of(
                        new Day5.Bucket(53, 4),
                        new Day5.Bucket(61, 9)),
                result);
    }

    @Test
    public void seedToBuckets_soil_to_fertilizer() {
        final Day5.Seed seed = new Day5.Seed(List.of(
                new Day5.Bucket(81, 14)));
        final List<Day5.Range> ranges = List.of(
                new Day5.Range(
                        new Day5.Bucket(15, 37),
                        new Day5.Bucket(0, 37)),
                new Day5.Range(
                        new Day5.Bucket(52, 2),
                        new Day5.Bucket(37, 2)),
                new Day5.Range(
                        new Day5.Bucket(0, 15),
                        new Day5.Bucket(39, 15)));

        final var result = seed.toBuckets(ranges);
        Assert.assertEquals(
                List.of(new Day5.Bucket(81, 14)),
                result);
    }

    @Test
    public void rangeScaling_inRange() {
        final Day5.Range range = new Day5.Range(
                new Day5.Bucket(100, 10),
                new Day5.Bucket(50, 10));
        final Day5.Bucket input = new Day5.Bucket(100, 5);

        final var result = range.apply(input);
        Assert.assertEquals(
                List.of(new Day5.Bucket(50, 5)),
                result.get());
    }

    @Test
    public void rangeScaling_lessThanRange() {
        final Day5.Range range = new Day5.Range(
                new Day5.Bucket(100, 10),
                new Day5.Bucket(50, 10));
        final Day5.Bucket input = new Day5.Bucket(0, 10);

        final var result = range.apply(input);
        Assert.assertEquals(
                Optional.empty(),
                result);
    }

    @Test
    public void rangeScaling_greaterThanRange() {
        final Day5.Range range = new Day5.Range(
                new Day5.Bucket(100, 10),
                new Day5.Bucket(50, 10));
        final Day5.Bucket input = new Day5.Bucket(150, 10);

        final var result = range.apply(input);
        Assert.assertEquals(
                Optional.empty(),
                result);
    }

    @Test
    public void rangeScaling_partial() {
        final Day5.Range range = new Day5.Range(
                new Day5.Bucket(100, 10),
                new Day5.Bucket(50, 10));
        final Day5.Bucket input = new Day5.Bucket(95, 20);

        final var result = range.apply(input);
        Assert.assertEquals(
                List.of(
                        new Day5.Bucket(50, 10),
                        new Day5.Bucket(95, 5),
                        new Day5.Bucket(110, 5)),
                result.get());
    }

    @Test
    public void rangeScaling_partialLessThan() {
        final Day5.Range range = new Day5.Range(
                new Day5.Bucket(100, 10),
                new Day5.Bucket(50, 10));
        final Day5.Bucket input = new Day5.Bucket(95, 10);

        final var result = range.apply(input);
        Assert.assertEquals(
                List.of(
                        new Day5.Bucket(50, 5),
                        new Day5.Bucket(95, 5)),
                result.get());
    }

    @Test
    public void rangeScaling_partialGreaterThan() {
        final Day5.Range range = new Day5.Range(
                new Day5.Bucket(100, 10),
                new Day5.Bucket(50, 10));
        final Day5.Bucket input = new Day5.Bucket(105, 10);

        final var result = range.apply(input);
        Assert.assertEquals(
                List.of(
                        new Day5.Bucket(55, 5),
                        new Day5.Bucket(110, 5)),
                result.get());
    }
}

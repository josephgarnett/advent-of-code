package com.mtab.adventofcode.year2023.day5;

import com.google.common.base.Preconditions;
import com.mtab.adventofcode.Application;
import com.mtab.adventofcode.utils.InputUtils;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Day5 implements Function<Day5.Almanac, Long> {


    @Override
    public Long apply(
            @NonNull final Almanac almanac) {
        // TODO use seed object as an iterator
        // store only last value
        // maybe has time constrain
        final var seedIterator = almanac.getSeeds().iterator();
        long min = Long.MAX_VALUE;

        while(seedIterator.hasNext()) {
            final Seed seed = seedIterator.next();
            final var i = seed.getIterator();
            while (i.hasNext()) {
                final long location = almanac.getSeedLocation(i.next());
                if (location < min) {
                    min = location;
                }
            }
        }
        return min;
    }

    private static Almanac getInput() throws IOException {
        final Almanac.AlmanacBuilder builder = Almanac.builder();

        final Iterator<String> iterator = InputUtils.readLines("2023/day5/input.txt")
                .iterator();

        final Supplier<List<Range>> getRanges = () -> {
            final List<Range> ranges = new ArrayList<>();
            String next;
            while (iterator.hasNext() && StringUtils.isNotBlank(next = iterator.next())) {
                final String[] parts = next.split("\\s+");
                ranges.add(Range.from(parts[1], parts[0], parts[2]));
            }

            return ranges;
        };

        while(iterator.hasNext()) {
            final String l = iterator.next();

            if (StringUtils.startsWithIgnoreCase(l, "seeds:")) {
                final String[] raw = l
                        .replace("seeds: ", "")
                        .split("\\s+");

                final List<Seed> seeds = new ArrayList<>();

                for (int i = 0; i < raw.length; i+=2) {
                    final long start = Long.parseLong(raw[i]);
                    final long length = Long.parseLong(raw[i + 1]);

                    seeds.add(new Seed(start, length));
                }

                builder.seeds(seeds);
                continue;
            }

            if (StringUtils.startsWithIgnoreCase(l, "seed-to-soil map")) {
                builder.seedToSoil(new GardeningMap(getRanges.get()));
            } else if (StringUtils.startsWithIgnoreCase(l, "soil-to-fertilizer map")) {
                builder.soilToFertilizer(new GardeningMap(getRanges.get()));
            } else if (StringUtils.startsWithIgnoreCase(l, "fertilizer-to-water map")) {
                builder.fertilizerToWater(new GardeningMap(getRanges.get()));
            } else if (StringUtils.startsWithIgnoreCase(l, "water-to-light map")) {
                builder.waterToLight(new GardeningMap(getRanges.get()));
            } else if (StringUtils.startsWithIgnoreCase(l, "light-to-temperature map")) {
                builder.lightToTemperature(new GardeningMap(getRanges.get()));
            } else if (StringUtils.startsWithIgnoreCase(l, "temperature-to-humidity map")) {
                builder.temperatureToHumidity(new GardeningMap(getRanges.get()));
            } else if (StringUtils.startsWithIgnoreCase(l, "humidity-to-location map")) {
                builder.humidityToLocation(new GardeningMap(getRanges.get()));
            }
        }

        return builder.build();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day5",
                () -> new Day5()
                        .apply(Day5.getInput()),
                1);

        Preconditions.checkArgument(result == 157211394);
    }


    @Value
    @Builder
    public static class Almanac {
        List<Seed> seeds;
        GardeningMap seedToSoil;
        GardeningMap soilToFertilizer;
        GardeningMap fertilizerToWater;
        GardeningMap waterToLight;
        GardeningMap lightToTemperature;
        GardeningMap temperatureToHumidity;
        GardeningMap humidityToLocation;

        public long getSeedLocation(@NonNull final long seed) {
            final long soil = this.seedToSoil.transformer().apply(seed);
            final long fertilizer = this.soilToFertilizer.transformer().apply(soil);
            final long water = this.fertilizerToWater.transformer().apply(fertilizer);
            final long light = this.waterToLight.transformer().apply(water);
            final long temperature = this.lightToTemperature.transformer().apply(light);
            final long humidity = this.temperatureToHumidity.transformer().apply(temperature);

            return this.humidityToLocation.transformer().apply(humidity);
        }
    }

    public record Seed(long start, long length) {
        Iterator<Long> getIterator() {
            long start = this.start;
            long length = this.length;
            return new Iterator<Long>(){
                long value = start;
                @Override
                public boolean hasNext() {
                    return value >= start && value <= start + length;
                }

                @Override
                public Long next() {
                    return ++value;
                }
            };
        }
    }

    @Builder
    record Range(long source, long target, long length) {
        static Range from(final String source, final String target, final String length) {
            return new Range(
                    Long.parseLong(source),
                    Long.parseLong(target),
                    Long.parseLong(length));
        }
    }

    @Builder
    record GardeningMap(List<Range> ranges) {
        Function<Long, Long> transformer() {
            return (seed) -> this.ranges
                    .stream()
                    .filter(r -> seed >= r.source && seed <= r.source + r.length)
                    .findFirst()
                    .map(range -> range.target + (seed - range.source))
                    .orElse(seed);
        }
    }
}

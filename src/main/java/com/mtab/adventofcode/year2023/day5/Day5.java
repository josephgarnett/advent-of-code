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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Day5 implements Function<Day5.Almanac, Long> {


    @Override
    public Long apply(
            @NonNull final Almanac almanac) {
        return almanac.getSeeds()
                .stream()
                .mapToLong(almanac::getSeedLocation)
                .min()
                .orElseThrow();
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
                builder.seeds(Arrays.stream(l
                                .replace("seeds: ", "")
                                .split("\\s+"))
                                .map(Long::parseLong)
                        .toList());
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

        Preconditions.checkArgument(result == 1);
    }


    @Value
    @Builder
    public static class Almanac {
        List<Long> seeds;
        GardeningMap seedToSoil;
        GardeningMap soilToFertilizer;
        GardeningMap fertilizerToWater;
        GardeningMap waterToLight;
        GardeningMap lightToTemperature;
        GardeningMap temperatureToHumidity;
        GardeningMap humidityToLocation;

        public long getSeedLocation(long seed) {
            /* return Stream.of(
                    this.seedToSoil,
                    this.soilToFertilizer,
                    this.fertilizerToWater,
                    this.waterToLight,
                    this.lightToTemperature,
                    this.temperatureToHumidity,
                    this.humidityToLocation)
                    .reduce(seed, (s, mapper) -> {

                    }) */
            final long soil = this.seedToSoil.transformer().apply(seed);
            final long fertilizer = this.soilToFertilizer.transformer().apply(soil);
            final long water = this.fertilizerToWater.transformer().apply(fertilizer);
            final long light = this.waterToLight.transformer().apply(water);
            final long temperature = this.lightToTemperature.transformer().apply(light);
            final long humidity = this.temperatureToHumidity.transformer().apply(temperature);

            return this.humidityToLocation.transformer().apply(humidity);
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
            return (input) -> this.ranges
                    .stream()
                    .filter(r -> input >= r.source && input <= r.source + r.length)
                    .findFirst()
                    .map(range -> range.target + (input - range.source))
                    .orElse(input);
        }
    }
}

package io.garnett.adventofcode.year2023.day5;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Day5 implements Function<Day5.Almanac, Long> {


    @Override
    public Long apply(
            @NonNull final Almanac almanac) {
        return almanac.getSeeds()
                .stream()
                .map(almanac::getSeedLocation)
                .mapToLong(Seed::getMinValue)
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
                final String[] raw = l
                        .replace("seeds: ", "")
                        .split("\\s+");

                final List<Seed> seeds = new ArrayList<>();

                for (int i = 0; i < raw.length; i += 2) {
                    final long start = Long.parseLong(raw[i]);
                    final long length = Long.parseLong(raw[i + 1]);

                    seeds.add(new Seed(List.of(new Bucket(start, length))));
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
                        .apply(Day5.getInput()));

        Preconditions.checkArgument(result == 50855035);
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

        public Seed getSeedLocation(
                @NonNull final Seed seed) {
            final Seed soil = this.seedToSoil.transformer().apply(seed);
            final Seed fertilizer = this.soilToFertilizer.transformer().apply(soil);
            final Seed water = this.fertilizerToWater.transformer().apply(fertilizer);
            final Seed light = this.waterToLight.transformer().apply(water);
            final Seed temperature = this.lightToTemperature.transformer().apply(light);
            final Seed humidity = this.temperatureToHumidity.transformer().apply(temperature);

            return this.humidityToLocation.transformer().apply(humidity);
        }
    }

    public record Seed(List<Bucket> buckets) {
        public long getMinValue() {
            return this.buckets
                    .stream()
                    .mapToLong(Bucket::start)
                    .min()
                    .orElseThrow();
        }

        public List<Bucket> toBuckets(final List<Range> ranges) {
            final List<Bucket> buckets = new ArrayList<>(this.buckets());
            final List<Bucket> result = new ArrayList<>();

            while (!buckets.isEmpty()) {
                final Bucket bucket = buckets.get(0);
                boolean isMapped = false;

                for (final Range range: ranges) {
                    final Optional<List<Bucket>> mapped = range.apply(bucket);

                    if (mapped.isPresent()) {
                        final List<Bucket> r = mapped.get();
                        buckets.remove(bucket);
                        result.add(r.get(0));
                        buckets.addAll(r.subList(1, r.size()));
                        isMapped = true;
                        break;
                    }
                }

                if (!isMapped) {
                    buckets.remove(bucket);
                    result.add(bucket);
                }
            }

            result.addAll(buckets);

            return result;
        }
    }

    public record Bucket(long start, long length) {
        public long end() {
            return this.start() + this.length();
        }
    }

    @Builder
    public record Range(Bucket source, Bucket target) implements Function<Bucket, Optional<List<Bucket>>> {
        static Range from(final String source, final String target, final String length) {
            final long s = Long.parseLong(source);
            final long t = Long.parseLong(target);
            final long l = Long.parseLong(length);
            return new Range(
                    new Bucket(s, l),
                    new Bucket(t, l));
        }

        @Override
        public Optional<List<Bucket>> apply(
                @NonNull final Bucket bucket) {
            final long offset = this.source().start() - this.target().start();
            final long scaledStart = bucket.start() - offset;
            final long scaledEnd = bucket.end() - offset;

            if (scaledStart > this.target().end() - 1
                || scaledEnd - 1 < this.target().start()) {
                return Optional.empty();
            }

            final long start = Math.max(this.target().start(), scaledStart);
            final long length = Math.min(this.target().end(), scaledEnd) - start;
            final List<Bucket> result = new ArrayList<>();
            final Bucket scaledBucket = new Bucket(start, length);

            result.add(scaledBucket);

            if (scaledStart < start) {
                result.add(new Bucket(bucket.start(), start - scaledStart));
            }

            if (scaledEnd > start + length) {
                final long remainder = scaledEnd - (start + length);
                result.add(new Bucket(bucket.end() - remainder, remainder));
            }

            return Optional.of(result);
        }
    }

    @Builder
    record GardeningMap(List<Range> ranges) {
        Function<Seed, Seed> transformer() {
            return (seed) -> new Seed(seed.toBuckets(this.ranges()));
        }
    }
}

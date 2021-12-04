package com.mtab.aventofcode.year2021.day3;

import com.mtab.aventofcode.models.IInputLoader;

import java.util.BitSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 implements
        IInputLoader<BitSet>,
        Supplier<Long[]> {
    private static final String INPUT_PATH = "2021/day3/input.txt";
    private static final int LENGTH = 12;

    private final List<BitSet> input;

    Day3() {
        this.input = this.getInput(Day3.INPUT_PATH);
    }

    @Override
    public Long[] get() {
        final BitSet[] bitSets = new BitSet[LENGTH];

        for (int i = 0; i < LENGTH; ++i) {
            bitSets[i] = new BitSet(this.input.size());
        }

        for (int i = 0; i < this.input.size(); ++i) {
            final BitSet bitSet = this.input.get(i);
            for (int j = 0; j < LENGTH; ++j) {
                bitSets[j].set(i, bitSet.get(j));
            }
        }

        final StringBuilder gammaRate = new StringBuilder();
        final StringBuilder epsilonRate = new StringBuilder();

        for (int i = 0; i < LENGTH; ++i) {
            final boolean isSet = bitSets[i].cardinality() > (this.input.size()) / 2;

            gammaRate.append(isSet ? '1' : '0');
            epsilonRate.append(!isSet ? '1' : '0');
        }

        final long o2Rate = this.getO2Rate(bitSets);
        final long co2Rate = this.getCO2Rate(bitSets);

        return new Long[]{
                Long.parseLong(gammaRate.toString(), 2) * Long.parseLong(epsilonRate.toString(), 2),
                o2Rate * co2Rate
        };
    }

    private long getO2Rate(final BitSet[] bitsets) {
        return rateReducer(
                bitsets[0],
                this.input,
                true,
                0);
    }

    private long getCO2Rate(final BitSet[] bitsets) {
        return rateReducer(
                bitsets[0],
                this.input,
                false,
                0);
    }

    private long rateReducer(
            final BitSet filterSet,
            final List<BitSet> targetSets,
            final boolean targetValueMostCommon,
            final int index
    ) {
        final float threshold = targetSets.size() / (float)2;
        final boolean isSet = filterSet.cardinality() == threshold ?
                targetValueMostCommon :
                targetValueMostCommon ?
                        filterSet.cardinality() > threshold :
                        filterSet.cardinality() < threshold;
        final List<BitSet> filteredTargets = targetSets
                .stream()
                .filter(bitset -> bitset.get(index) == isSet)
                .collect(Collectors.toList());

        if (filteredTargets.size() == 1) {
            final StringBuilder buffer = new StringBuilder(LENGTH);
            IntStream.range(0, LENGTH)
                    .mapToObj(i -> filteredTargets.get(0).get(i) ? '1' : '0')
                    .forEach(buffer::append);

            return Long.parseLong(buffer.toString(), 2);
        }

        final BitSet nextFilterSet = new BitSet(filteredTargets.size());

        for (int i = 0; i < filteredTargets.size(); ++i) {
            nextFilterSet.set(i, filteredTargets.get(i).get(index + 1));
        }

        return this.rateReducer(
                nextFilterSet,
                filteredTargets,
                targetValueMostCommon,
                index + 1);
    }

    @Override
    public BitSet parseLine(final String line, final int index) {
        final BitSet bitSet = new BitSet(LENGTH);

        for (int i = 0; i < LENGTH; ++i) {
            bitSet.set(i, '1' == line.trim().charAt(i));
        }

        return bitSet;
    }

    public static void main(final String[] args) {
        final Long[] result = new Day3().get();

        assert result[0] == 3320834;

        System.out.println(result[0]);
        System.out.println(result[1]);
    }

}

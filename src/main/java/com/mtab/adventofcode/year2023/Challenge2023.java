package com.mtab.adventofcode.year2023;

import com.google.common.base.Stopwatch;
import com.mtab.adventofcode.year2023.day1.Day1;
import com.mtab.adventofcode.year2023.day2.Day2;
import com.mtab.adventofcode.year2023.day3.Day3;
import com.mtab.adventofcode.year2023.day4.Day4;
import com.mtab.adventofcode.year2023.day6.Day6;

import java.util.concurrent.TimeUnit;

import static org.fusesource.jansi.Ansi.ansi;

public class Challenge2023 {
    public static void main(final String[] args) throws Exception {
        final Stopwatch sw = Stopwatch.createStarted();

        Day1.main(args);
        Day2.main(args);
        Day3.main(args);
        Day4.main(args);
        Day6.main(args);

        System.out.print(
                ansi()
                        .fgYellow()
                        .a(String.format(
                                "\uD83C\uDF89\uD83C\uDF89 All challenges run, execution time: %dms%n",
                                sw.elapsed(TimeUnit.MILLISECONDS)))
                        .reset());
    }
}

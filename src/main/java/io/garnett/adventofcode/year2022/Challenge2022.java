package io.garnett.adventofcode.year2022;

import com.google.common.base.Stopwatch;
import io.garnett.adventofcode.year2022.day1.Day1;
import io.garnett.adventofcode.year2022.day10.Day10;
import io.garnett.adventofcode.year2022.day11.Day11;
import io.garnett.adventofcode.year2022.day2.Day2;
import io.garnett.adventofcode.year2022.day3.Day3;
import io.garnett.adventofcode.year2022.day4.Day4;
import io.garnett.adventofcode.year2022.day5.Day5;
import io.garnett.adventofcode.year2022.day6.Day6;
import io.garnett.adventofcode.year2022.day7.Day7;
import io.garnett.adventofcode.year2022.day8.Day8;
import io.garnett.adventofcode.year2022.day9.Day9;

import java.util.concurrent.TimeUnit;

import static org.fusesource.jansi.Ansi.ansi;

public class Challenge2022 {
    public static void main(final String[] args) throws Exception {
        final Stopwatch sw = Stopwatch.createStarted();

        Day1.main(args);
        Day2.main(args);
        Day3.main(args);
        Day4.main(args);
        Day5.main(args);
        Day6.main(args);
        Day7.main(args);
        Day8.main(args);
        Day9.main(args);
        Day10.main(args);
        Day11.main(args);

        System.out.print(
                ansi()
                .fgYellow()
                .a(String.format(
                        "\uD83C\uDF89\uD83C\uDF89 All challenges run, execution time: %dms%n",
                        sw.elapsed(TimeUnit.MILLISECONDS)))
                .reset());
    }
}

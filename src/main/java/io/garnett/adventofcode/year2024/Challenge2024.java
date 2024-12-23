package io.garnett.adventofcode.year2024;

import com.google.common.base.Stopwatch;
import io.garnett.adventofcode.year2024.day3.Day3;
import io.garnett.adventofcode.year2024.day1.Day1;
import io.garnett.adventofcode.year2024.day2.Day2;
import io.garnett.adventofcode.year2024.day4.Day4;
import io.garnett.adventofcode.year2024.day5.Day5;

import java.util.concurrent.TimeUnit;

import static org.fusesource.jansi.Ansi.ansi;

public class Challenge2024 {
    public static void main(final String[] args) {
        final Stopwatch sw = Stopwatch.createStarted();

        Day1.main(args);
        Day2.main(args);
        Day3.main(args);
        Day4.main(args);
        Day5.main(args);

        System.out.print(
                ansi()
                        .fgYellow()
                        .a(String.format(
                                "\uD83C\uDF89\uD83C\uDF89 All challenges run, execution time: %dms%n",
                                sw.elapsed(TimeUnit.MILLISECONDS)))
                        .reset());
    }
}

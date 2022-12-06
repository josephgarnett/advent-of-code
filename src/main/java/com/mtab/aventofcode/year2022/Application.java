package com.mtab.aventofcode.year2022;

import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.TaskUtils;
import com.mtab.aventofcode.year2022.day1.Day1;
import com.mtab.aventofcode.year2022.day2.Day2;
import com.mtab.aventofcode.year2022.day3.Day3;
import com.mtab.aventofcode.year2022.day4.Day4;
import com.mtab.aventofcode.year2022.day5.Day5;
import com.mtab.aventofcode.year2022.day6.Day6;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(final String[] args) throws Exception {
        final Stopwatch sw = Stopwatch.createStarted();

        Day1.main(args);
        Day2.main(args);
        Day3.main(args);
        Day4.main(args);
        Day5.main(args);
        Day6.main(args);

        System.out.printf("All tests run, execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }

    public static <T> T challenge(
            final String baseResourcePath,
            final Callable<T> task) throws Exception {
        try {
            System.out.println(TaskUtils.getDescription(baseResourcePath + "/readme.txt"));
        } catch (Exception e) {
            System.out.println("--------Unknown Task--------");
        }

        final Stopwatch sw = Stopwatch.createStarted();

        final var result = task.call();

        System.out.println(result);
        System.out.println("============================================");
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
        System.out.println("============================================\n\n");

        return result;
    }
}

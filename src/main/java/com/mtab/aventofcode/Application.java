package com.mtab.aventofcode;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.TaskUtils;
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.fusesource.jansi.Ansi.ansi;

public class Application {
    private static final int DEFAULT_TEST_RUNS = 500;
    public static void main(String... args) {
        AnsiConsole.systemInstall();
    }

    public static <T> T challenge(
            final String baseResourcePath,
            final Callable<T> task) throws Exception {
        return Application.challenge(
                baseResourcePath,
                task,
                DEFAULT_TEST_RUNS);
    }

    public static <T> T challenge(
            final String baseResourcePath,
            final Callable<T> task,
            final int tests) throws Exception {
        try {
            System.out.println(
                    ansi().fgBrightBlue()
                            .a(TaskUtils.getDescription(baseResourcePath + "/readme.txt"))
                            .reset());
        } catch (Exception e) {
            System.out.println(ansi()
                    .fgYellow()
                    .a("--------Unknown Task--------")
                    .reset());
        }

        final List<Long> times = new ArrayList<>(tests);
        final var result = task.call();

        for (int i = 1; i < tests; ++i) {
            final Stopwatch sw = Stopwatch.createStarted();
            Preconditions.checkArgument(task.call().equals(result));
            times.add(sw.elapsed(TimeUnit.MILLISECONDS));
        }

        System.out.println(result);
        System.out.println("============================================");
        System.out.print(ansi()
                .fgBrightGreen()
                .a(String.format("Average execution time over %d runs: %.4fms%n",
                        tests,
                        times.stream().mapToLong(t -> t).average().orElseThrow()))
                .reset());
        System.out.println("============================================\n\n");

        return result;
    }
}

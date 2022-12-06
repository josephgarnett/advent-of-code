package com.mtab.aventofcode;

import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.TaskUtils;
import org.fusesource.jansi.AnsiConsole;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.fusesource.jansi.Ansi.ansi;

public class Application {
    public static void main(String... args) {
        AnsiConsole.systemInstall();
    }

    public static <T> T challenge(
            final String baseResourcePath,
            final Callable<T> task) throws Exception {
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

        final Stopwatch sw = Stopwatch.createStarted();

        final var result = task.call();

        System.out.println(result);
        System.out.println("============================================");
        System.out.print(ansi()
                .fgBrightGreen()
                .a(String.format("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS)))
                .reset());
        System.out.println("============================================\n\n");

        return result;
    }
}

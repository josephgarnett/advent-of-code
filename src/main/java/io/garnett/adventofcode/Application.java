package io.garnett.adventofcode;

import com.google.common.base.Stopwatch;
import io.garnett.adventofcode.utils.TaskUtils;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.AnsiConsole;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static org.fusesource.jansi.Ansi.ansi;

public class Application {
    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }
    private static final int DEFAULT_TEST_RUNS = 500;
    public static void main(String... args) {
        AnsiConsole.systemInstall();
    }

    public static <T> T challenge(
            final String baseResourcePath,
            final Callable<T> task) {
        return Application.challenge(
                baseResourcePath,
                task,
                DEFAULT_TEST_RUNS);
    }

    public static <T> T challenge(
            final String baseResourcePath,
            final Callable<T> task,
            final int tests) {
        return Application.challenge(
                baseResourcePath,
                (i) -> task.call(),
                tests);
    }

    public static <T> T challenge(
            final String baseResourcePath,
            final CheckedFunction<Integer, T> task) {
        return Application.challenge(
                baseResourcePath,
                task,
                DEFAULT_TEST_RUNS);
    }

    public static <T> T challenge(
            final String baseResourcePath,
            final CheckedFunction<Integer, T> task,
            final int tests) {
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

        final AtomicReference<T> result = new AtomicReference<>();
        final AtomicInteger i = new AtomicInteger(0);
        final double average = IntStream.range(0, tests)
                .mapToLong(u -> {
                    final Stopwatch sw = Stopwatch.createStarted();
                    try {
                        result.set(task.apply(u));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    return sw.elapsed(TimeUnit.MICROSECONDS);
                })
                .peek(u -> {
                    if (StringUtils.isEmpty(System.getenv("CI"))) {
                        System.out.printf("\r %.0f%%", (float)i.incrementAndGet() / (float)tests * 100);
                    }
                })
                .average()
                .orElseThrow();

        System.out.print("\r");
        System.out.println(result);
        System.out.println("============================================================");
        System.out.print(ansi()
                .fgBrightGreen()
                .a(String.format("Average execution time over %d runs: %.2fμs%n",
                        tests,
                        average))
                .reset());
        System.out.println("============================================================\n\n");

        return result.get();
    }
}

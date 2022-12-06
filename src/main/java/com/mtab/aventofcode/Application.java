package com.mtab.aventofcode;

import com.google.common.base.Stopwatch;
import com.mtab.aventofcode.utils.TaskUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String... args) {

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

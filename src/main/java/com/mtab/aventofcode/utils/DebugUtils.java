package com.mtab.aventofcode.utils;

import java.util.List;

public class DebugUtils {
    public static  <T> void printGrid(final List<List<T>> grid) {
        System.out.println();
        System.out.println(grid.toString()
                .replace("], ", "]\n")
                .replace("[", "")
                .replace("]", "")
                .replace("-1", "@"));
        System.out.println();
    }
}

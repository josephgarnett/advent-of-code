package io.garnett.adventofcode.year2021.day13;

import com.google.common.base.Stopwatch;
import io.garnett.adventofcode.models.InputLoader;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Day13 implements InputLoader<Object[]>, Supplier<Integer> {

    private final Paper paper;
    private final List<Paper.FoldInstruction> folds;

    private Day13(final String resourcePath) {
        final Object[] input = this.getInput(resourcePath);

        this.paper = (Paper)input[0];
        this.folds = (List<Paper.FoldInstruction>)input[1];
    }
    @Override
    public Integer get() {
        Paper p = this.paper;

        for (final Paper.FoldInstruction f: this.folds) {
            p = p.fold(f);
        }

        System.out.println("");
        System.out.println(p.print());

        return p.countPoints();
    }

    @Override
    public Object[] transformResource(BufferedReader br) throws IOException {
        final List<Paper.Point> points = new ArrayList<>();
        final List<Paper.FoldInstruction> folds = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null && !StringUtils.isEmpty(line)) {
            String[] coord = line.split(",");
            points.add(
                    new Paper.Point(
                            true,
                            new Point(
                                    Integer.parseInt(coord[0]),
                                    Integer.parseInt(coord[1]))));
        }

        final Paper paper = new Paper(points);

        while ((line = br.readLine()) != null && !StringUtils.isEmpty(line)) {
            final String[] values = line.replace("fold along ", "").split("=");

            if (StringUtils.equals(values[0], "x")) {
                folds.add(
                        new Paper.FoldInstruction(
                                Paper.FoldAxis.VERTICAL,
                                new Point(Integer.parseInt(values[1]), 0)));
            } else {
                folds.add(
                        new Paper.FoldInstruction(
                                Paper.FoldAxis.HORIZONTAL,
                                new Point(0, Integer.parseInt(values[1]))));
            }
        }

        return new Object[]{ paper, folds };
    }

    public static void main(final String... args) {
        final Stopwatch sw = Stopwatch.createStarted();
        final int result = new Day13("2021/day13/input.txt").get();

        System.out.println(result);
        System.out.printf("Execution time: %dms%n", sw.elapsed(TimeUnit.MILLISECONDS));
    }
}

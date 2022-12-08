package com.mtab.aventofcode.year2022.day8;

import com.google.common.base.Preconditions;
import com.mtab.aventofcode.Application;
import com.mtab.aventofcode.utils.Direction;
import com.mtab.aventofcode.utils.InputUtils;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Day8 implements Function<Day8.Forest, Long> {

    public static Forest getInput() throws IOException {
        final AtomicInteger x = new AtomicInteger(0);
        final AtomicInteger y = new AtomicInteger(-1);
        final List<Tree> trees = Files.lines(
                Path.of(InputUtils.getInputPath("2022/day8/input.txt")))
                .peek(l -> {
                    x.set(0);
                    y.incrementAndGet();
                })
                .flatMap(line -> Arrays.stream(line.split(""))
                        .map(t -> Tree.of(Integer.parseInt(t), x.getAndIncrement(), y.get())))
                .toList();

        return new Forest(trees, x.decrementAndGet(), y.get());
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2022/day8",
                () -> new Day8().apply(Day8.getInput()),
                1);

        Preconditions.checkArgument(result == 1);
    }

    @Override
    public Long apply(final Forest forest) {
        return forest.countVisibleTrees();
    }

    record Tree(int height, Point2D position) {
        static Tree of(final int height, final int x, final int y) {
            return new Tree(height, new Point2D.Double(x, y));
        }

    }

    record Forest(List<Tree> trees, int maxX, int maxY) {
        Tree[][] getTreeMap() {
            final Tree[][] result = new Tree[this.maxY + 1][this.maxX + 1];

            this.trees.forEach(t -> {
                final var pos = t.position();

                result[(int) pos.getY()][(int) pos.getX()] = t;
            });

            return result;
        }

        Point2D getNextTreePosition(final Point2D point, final Direction direction) {
            switch(direction) {
                case EAST -> {
                    return new Point2D.Double(point.getX() + 1, point.getY());
                }
                case WEST -> {
                    return new Point2D.Double(point.getX() - 1, point.getY());
                }
                case SOUTH -> {
                    return new Point2D.Double(point.getX(), point.getY() + 1);
                }
                case NORTH -> {
                    return new Point2D.Double(point.getX(), point.getY() - 1);
                }
                default -> throw new IllegalStateException();
            }
        }

        Optional<Tree> getTreeFromMap(final Tree[][] map, final int x, final int y) {
            try {
                return Optional.of(map[y][x]);
            } catch(ArrayIndexOutOfBoundsException e) {
                return Optional.empty();
            }
        }

        BiFunction<Tree, Direction, Boolean> createVisibilityChecker() {
            final var map = this.getTreeMap();

            return this.createVisibilityChecker(map);
        }

        BiFunction<Tree, Direction, Boolean> createVisibilityChecker(final Tree[][] map) {
            return (final Tree tree, final Direction direction) -> {
                final Point2D nextPos = this.getNextTreePosition(tree.position(), direction);

                return this.getTreeFromMap(map, (int) nextPos.getX(), (int) nextPos.getY())
                        .map(nextTree -> {
                            if (nextTree.height() >= tree.height()) {
                                return false;
                            }

                            return this.createVisibilityChecker(map)
                                    .apply(new Tree(tree.height(), nextPos), direction);
                        })
                        .orElse(true);
            };
        }

        long countVisibleTrees() {
            final var isTreeVisible = this.createVisibilityChecker();

            return this.trees
                    .stream()
                    .filter(t -> (
                            isTreeVisible.apply(t, Direction.NORTH)
                            || isTreeVisible.apply(t, Direction.EAST)
                            || isTreeVisible.apply(t, Direction.SOUTH)
                            || isTreeVisible.apply(t, Direction.WEST)))
                    .count();
        }
    }
}

package com.mtab.aventofcode.year2021.day12;

import java.util.*;

public class CaveSystem {
    private static String START_CAVE = "start";
    private static String END_CAVE = "end";

    private final Map<String, Cave> caves;

    private int pathCount = 0;

    public CaveSystem() {
        this.caves = new HashMap<>();
    }

    public void map() {
        this.map(this.getCave(START_CAVE), this.caves.get(END_CAVE), new HashSet<>());
    }
    private void map(
            final Cave start,
            final Cave destination,
            final Set<Cave> visited) {
        visited.add(start);
        if (start == destination) {
            pathCount++;

            return;
        }

        for (final Cave c: start.getConnections()) {
            if (visited.contains(c) && c.isSmall()) {
                continue;
            }
            this.map(c, destination, new HashSet<>(visited));
        }
    }

    public int getPathCount() {
        return this.pathCount;
    }

    public CaveSystem addAllCaves(final List<Cave> caves) {
        for (final Cave c: caves ) {
            this.caves.put(c.getName(), c);
        }

        return this;
    }

    public CaveSystem addCave(final Cave c) {
        this.caves.put(c.getName(), c);

        return this;
    }

    public boolean hasCave(final String caveName) {
        return this.caves.containsKey(caveName);
    }

    public Cave getCave(final String caveName) {
        return this.caves.get(caveName);
    }

    public void connectCaves(final Cave a, final Cave b) {
        a.addConnection(b);
    }
}

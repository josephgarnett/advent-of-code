package io.garnett.adventofcode.year2021.day12;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CaveSystem {
    private static final String START_CAVE = "start";
    private static final String END_CAVE = "end";

    private final Map<String, Cave> caves;

    private int pathCount = 0;

    public CaveSystem() {
        this.caves = new HashMap<>();
    }

    public void map() {
        this.map(this.getCave(START_CAVE), this.caves.get(END_CAVE), new LinkedHashMap<>());
    }
    private void map(
            final Cave start,
            final Cave destination,
            final HashMap<Cave, Integer> visited) {
        this.updateVisited(start, visited);
        if (start == destination) {
            pathCount++;

            return;
        }

        for (final Cave c: start.getConnections()) {
            if (!this.canVisit(c, visited)) {
                continue;
            }
            this.map(c, destination, new LinkedHashMap<>(visited));
        }
    }

    private void updateVisited(final Cave c, final HashMap<Cave, Integer> visited) {
        visited.computeIfPresent(c, (cave, count) -> count + 1);
        visited.putIfAbsent(c, 1);
    }

    private boolean canVisit(final Cave c, final HashMap<Cave, Integer> visited) {
        if (StringUtils.equals(c.getName(), START_CAVE)) {
            return false;
        }

        if (visited.containsKey(c) && c.isSmall()) {
            return visited.entrySet()
                    .stream()
                    .noneMatch(entry -> entry.getKey().isSmall() && entry.getValue() > 1);
        }

        return true;
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

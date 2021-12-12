package com.mtab.aventofcode.year2021.day12;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Cave {
    private final String name;
    private final CaveSize size;

    private final Set<Cave> connections;

    public Cave(final String name) {
        this.name = name;
        this.size = StringUtils.isAllLowerCase(name) ? CaveSize.SMALL : CaveSize.LARGE;
        this.connections = new HashSet<>();
    }

    public String getName() {
        return this.name;
    }

    public boolean isSmall() {
        return this.size == CaveSize.SMALL;
    }

    public Cave addConnection(final Cave c) {
        if (this.connections.contains(c)) {
            return this;
        }

        this.connections.add(c);
        c.addConnection(this);

        return this;
    }

    public Set<Cave> getConnections() {
        return this.connections;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.size);
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }

        if (that instanceof Cave) {
            return StringUtils.equals(this.getName(), ((Cave) that).getName());
        }

        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", this.name)
                .add("size", this.size)
                .add("connections", this.connections.size())
                .toString();
    }


    private enum CaveSize { SMALL, LARGE }
}

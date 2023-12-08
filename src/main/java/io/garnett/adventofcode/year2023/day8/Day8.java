package io.garnett.adventofcode.year2023.day8;

import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import io.garnett.adventofcode.utils.LowestCommonMultiple;
import lombok.Builder;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class Day8 implements Function<Day8.Context, Long> {

    @Override
    public Long apply(
            @NonNull final Context context) {
        final var instructions = context.instructions();
        final var nodes = context.nodes();
        final List<Long> paths = nodes.values()
                .stream()
                .filter(Node::isStartNode)
                .map(t -> this.getPathLength(
                        t,
                        new ArrayDeque<>(instructions),
                        nodes))
                .toList();

        return LowestCommonMultiple.lcm(paths);
    }

    private long getPathLength(
            @NonNull final Node start,
            @NonNull final Deque<Instruction> instructions,
            @NonNull final Map<String, Node> nodes) {
        final AtomicReference<Node> currentNode = new AtomicReference<>(start);
        final AtomicInteger path = new AtomicInteger(0);

        while(!currentNode.get().isTerminalNode()) {
            final Instruction instruction = instructions.poll();

            final Node node = currentNode.get();
            final Node next = instruction == Instruction.L
                    ? nodes.get(node.fork().getLeft())
                    : nodes.get(node.fork().getRight());


            currentNode.set(next);
            path.incrementAndGet();
            instructions.offerLast(instruction);
        }

        return path.get();
    }

    private static Context getInput() throws IOException {
        final var context = Context.builder();
        final Map<String, Node> nodes = new HashMap<>();
        final Iterator<String> lines = InputUtils.readLines("2023/day8/input.txt")
                .iterator();

        if (lines.hasNext()) {
            context.instructions(
                    new ArrayDeque<>(Arrays.stream(
                            lines.next().split(""))
                    .map(Instruction::valueOf)
                    .toList()));
        }

        while(lines.hasNext()) {
            final String line = lines.next();

            if (StringUtils.isEmpty(line)) {
                continue;
            }

            final String[] parts = line.split("\\s+=\\s+");
            final String id = parts[0];
            final String[] routes = parts[1]
                    .replace("(", "")
                    .replace(")", "")
                    .split(",\\s+");

            nodes.put(id, new Node(id, new ImmutablePair<>(routes[0], routes[1])));
        }

        return context
                .nodes(nodes)
                .build();
    }

    public static void main(final String[] args) throws Exception {
        final var result = Application.challenge(
                "2023/day8",
                () -> new Day8()
                        .apply(Day8.getInput()));

        Preconditions.checkArgument(result == 15726453850399L);
    }

    @Builder
    public record Context(Deque<Instruction> instructions, Map<String, Node> nodes){}

    public enum Instruction {
        L,
        R
    }

    public record Node(String id, Pair<String, String> fork){
        public boolean isStartNode() {
            return StringUtils.endsWith(this.id(), "A");
        }

        public boolean isTerminalNode() {
            return StringUtils.endsWith(this.id(), "Z");
        }
    }
}

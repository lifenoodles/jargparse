package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.List;

/**
 * An ArgumentCounter that enforces a fixed argument count
 *
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
class FixedCounter implements ArgumentCounter {
    private final int minArgumentCount;
    private final int maxArgumentCount;

    public FixedCounter(final int minArgumentCount,
            final int maxArgumentCount) {
        this.minArgumentCount = minArgumentCount;
        this.maxArgumentCount = maxArgumentCount;
    }

    @Override
    public int minimumArgumentCount() {
        return minArgumentCount;
    }

    @Override
    public int maximumArgumentCount() {
        return maxArgumentCount;
    }

    @Override
    public String formatLabels(final List<String> argumentLabels) {
        assert (!argumentLabels.isEmpty());
        StringBuilder builder = new StringBuilder();
        List<String> labelList = new ArrayList<>(argumentLabels);
        while (labelList.size() < minimumArgumentCount()) {
            labelList.add(labelList.get(0));
        }
        for (int i = 0; i < minimumArgumentCount(); ++i) {
            builder.append("%s ");
        }
        return String.format(builder.toString(),
                labelList.stream().limit(minimumArgumentCount())
                        .toArray()).trim();
    }
}

package org.lifenoodles.jargparse;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public class ZeroOrMoreCounter implements ArgumentCounter {
    @Override
    public int minimumArgumentCount() {
        return 0;
    }

    @Override
    public int maximumArgumentCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String formatLabels(final List<String> argumentLabels) {
        assert (!argumentLabels.isEmpty());
        return String.format("[%s ...]", argumentLabels.get(0));
    }
}

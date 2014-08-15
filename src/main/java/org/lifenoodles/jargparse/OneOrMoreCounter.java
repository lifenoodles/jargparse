package org.lifenoodles.jargparse;

import java.util.List;

/**
 * Implements an ArgumentCounter that allows for one or more arguments
 *
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
class OneOrMoreCounter implements ArgumentCounter {
    @Override
    public int minimumArgumentCount() {
        return 1;
    }

    @Override
    public int maximumArgumentCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String formatLabels(final List<String> argumentLabels) {
        assert (!argumentLabels.isEmpty());
        if (argumentLabels.size() == 1) {
            argumentLabels.add(argumentLabels.get(0));
        }
        return String.format("%s [%s ...]", argumentLabels.get(0),
                argumentLabels.get(1));
    }
}

package org.lifenoodles.jargparse;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public class ZeroOrMoreParser implements OptionParser {
    @Override
    public boolean isCountCorrect(final List<String> arguments) {
        return true;
    }

    @Override
    public int expectedOptionCount() {
        return 0;
    }

    @Override
    public String helpSummary(final List<String> argumentLabels) {
        assert (!argumentLabels.isEmpty());
        return String.format("[%s ...]", argumentLabels.get(0));
    }
}

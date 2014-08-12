package org.lifenoodles.jargparse.parsers;

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
    public String helpSummary(final String argumentLabel) {
        return String.format("[%s ...]", argumentLabel);
    }
}

package org.lifenoodles.jargparse.parsers;

import org.lifenoodles.jargparse.Utility;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public class OneOrMoreParser implements OptionParser {
    @Override
    public boolean isCountCorrect(final List<String> arguments) {
        return Utility.argumentCount(arguments) >= 1;
    }

    @Override
    public int expectedOptionCount() {
        return 1;
    }

    @Override
    public String helpSummary(final String argumentLabel) {
        return String.format("%s [%s ...]", argumentLabel, argumentLabel);
    }
}

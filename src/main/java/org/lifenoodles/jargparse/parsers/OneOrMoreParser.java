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
    public String helpSummary(final List<String> argumentLabels) {
        assert(!argumentLabels.isEmpty());
        if (argumentLabels.size() == 1) {
            argumentLabels.add(argumentLabels.get(0));
        }
        return String.format("%s [%s ...]", argumentLabels.get(0),
                argumentLabels.get(1));
    }
}

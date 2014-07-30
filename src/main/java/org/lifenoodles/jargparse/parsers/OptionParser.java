package org.lifenoodles.jargparse.parsers;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public interface OptionParser {
    public abstract boolean isCountCorrect(final List<String> arguments);
    public abstract List<String> extractArguments(final List<String> arguments);
    public abstract List<String> restOfArguments(final List<String> arguments);
}

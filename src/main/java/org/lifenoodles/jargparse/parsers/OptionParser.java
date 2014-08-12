package org.lifenoodles.jargparse.parsers;

import org.lifenoodles.jargparse.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public interface OptionParser {
    public abstract boolean isCountCorrect(final List<String> arguments);

    public abstract int expectedOptionCount();

    public default List<String> extractArguments(final List<String> arguments) {
        return arguments.stream().limit(Utility.argumentCount(arguments))
                .collect(Collectors.toList());
    }

    public default List<String> restOfArguments(final List<String> arguments) {
        final List<String> rest = new ArrayList<>();
        for (int i = Utility.argumentCount(arguments); i < arguments.size();
             ++i) {
            rest.add(arguments.get(i));
        }
        return rest;
    }

    public abstract String helpSummary(String argumentLabel);
}

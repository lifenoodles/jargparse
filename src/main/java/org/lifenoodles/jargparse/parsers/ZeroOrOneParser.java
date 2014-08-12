package org.lifenoodles.jargparse.parsers;

import org.lifenoodles.jargparse.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public class ZeroOrOneParser implements OptionParser {
    @Override
    public boolean isCountCorrect(final List<String> arguments) {
        return Utility.argumentCount(arguments) <= 1;
    }

    @Override
    public int expectedOptionCount() {
        return 0;
    }

    @Override
    public List<String> extractArguments(final List<String> arguments) {
        return arguments.stream()
                .limit(Math.min(1, Utility.argumentCount(arguments)))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> restOfArguments(final List<String> arguments) {
        final List<String> rest = new ArrayList<>();
        for (int i = Math.min(1, Utility.argumentCount(arguments));
                i < arguments.size(); ++i) {
            rest.add(arguments.get(i));
        }
        return rest;
    }

    @Override
    public String helpSummary(final String argumentLabel) {
        return String.format("[%s]", argumentLabel);
    }
}

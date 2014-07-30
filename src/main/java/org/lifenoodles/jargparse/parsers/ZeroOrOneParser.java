package org.lifenoodles.jargparse.parsers;

import org.lifenoodles.jargparse.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public class ZeroOrOneParser implements OptionParser {
    @Override
    public boolean isCountCorrect(final List<String> arguments) {
        return extractArguments(arguments).size() <= 1;
    }

    @Override
    public List<String> extractArguments(final List<String> arguments) {
        final List<String> extracted = new ArrayList<>();
        if (arguments.size() > 0 && Utility.isOption(arguments.get(0))) {
            extracted.add(arguments.get(0));
        }
        return extracted;
    }

    @Override
    public List<String> restOfArguments(final List<String> arguments) {
        final List<String> rest = new ArrayList<>();
        for (int i = extractArguments(arguments).size();
                i < arguments.size(); ++i) {
            rest.add(arguments.get(i));
        }
        return rest;
    }
}

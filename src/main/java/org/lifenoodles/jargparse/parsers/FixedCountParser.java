package org.lifenoodles.jargparse.parsers;

import org.lifenoodles.jargparse.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public class FixedCountParser implements OptionParser {
    private final int argumentCount;

    public FixedCountParser(final int argumentCount) {
        this.argumentCount = argumentCount;
    }

    @Override
    public boolean isCountCorrect(final List<String> arguments) {
        return extractArguments(arguments).size() == argumentCount;
    }

    @Override
    public List<String> extractArguments(final List<String> arguments) {
        final List<String> extracted = new ArrayList<>();
        for (int i = 0; i < argumentCount && i < arguments.size(); ++i) {
            if (Utility.isOption(arguments.get(i))) {
                break;
            }
            extracted.add(arguments.get(i));
        }
        return extracted;
    }

    @Override
    public List<String> restOfArguments(final List<String> arguments) {
        final List<String> rest = new ArrayList<>();
        for (int i = argumentCount; i < arguments.size(); ++i) {
            rest.add(arguments.get(i));
        }
        return rest;
    }
}

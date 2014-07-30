package org.lifenoodles.jargparse.parsers;

import org.lifenoodles.jargparse.Utility;

import java.util.ArrayList;
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
    public List<String> extractArguments(final List<String> arguments) {
        final List<String> extracted = new ArrayList<>();
        for (String argument : arguments) {
            if (Utility.isOption(argument)) {
                break;
            }
            extracted.add(argument);
        }
        return extracted;
    }

    @Override
    public List<String> restOfArguments(final List<String> arguments) {
        final List<String> rest = new ArrayList<>();
        for (int i = extractArguments(arguments).size(); i < arguments.size();
                ++i) {
            rest.add(arguments.get(i));
        }
        return rest;
    }
}

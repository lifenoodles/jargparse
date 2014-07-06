package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses an array of strings looking for specified patterns,
 * contains methods to register options as well as validate input
 *
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class ArgumentParser {
    private final List<Option> options = new ArrayList<>();

    /**
     * Determines whether or not the arguments that have been parsed  so far are
     * well formed. An unused ArgumentParser will always return true
     * @return boolean indicating if parsed args are well formed
     */
    public boolean isWellFormed() {
        return options.stream().allMatch(Option::isWellFormed);
    }

    /**
     * Parse the provided arguments using any rules that have been registered
     * @param arguments the array of arguments
     * @return this
     */
    public ArgumentParser parse(final String ... arguments)
            throws ParseException {
        if (arguments.length == 0) {
            throw new ParseException("Unable to correctly parse the arguments");
        }

        return this;
    }
}

package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.parsers.OptionParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 7/22/14.
 */

class OptionValidator {
    private final String description;
    private final List<String> names;
    private final OptionParser optionParser;
    private final Predicate<String> predicate;

    public OptionValidator(final List<String> names,
            final String description,
            final OptionParser optionParser,
            final Predicate<String> predicate) {
        this.names = new ArrayList<>(names);
        this.description = description;
        this.optionParser = optionParser;
        this.predicate = predicate;
    }

    public String getName() {
        return names.get(0);
    }

    /**
     * @return return the full list of names of this option
     */
    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    /**
     * @return the description of this option
     */
    public String getDescription() {
        return description;
    }
}

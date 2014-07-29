package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 7/22/14.
 */

class OptionValidator {

    private final int fixedCount;
    private final ArgumentCount argumentCount;
    private final String description;
    private final Predicate<String> predicate;
    private final List<String> names;

    public OptionValidator(final String description,
            final int fixedCount,
            final ArgumentCount argumentCount,
            final Predicate<String> predicate,
            final String name,
            final String ... names) {
        this.description = description;
        this.fixedCount = fixedCount;
        this.argumentCount = argumentCount;
        this.predicate = predicate;
        this.names = new ArrayList<>();
        this.names.addAll(Arrays.asList(names));
        this.names.add(0, name);
    }

    /**
     * @return argument count of this option
     */
    public int getFixedCount() {
        return fixedCount;
    }

    public ArgumentCount getArgumentCount() {
        return argumentCount;
    }

    /**
     * @return the canonical name of this option
     */
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

    /**
     * Determines if this option is well formed
     *
     * @return a boolean indicating if this argument is well formed
     */
    public boolean isArgumentLegal(final String argument) {
        return getFixedCount() > 0 && predicate.test(argument);
    }

}

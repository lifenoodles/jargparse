package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an argument that can be parsed
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
abstract class OptionValidator {
    private final Set<String> names;
    private final String description;

    public OptionValidator(final String description,
                           final String name, final String... otherNames) {
        this.description = description;
        this.names = Arrays.stream(otherNames).collect(Collectors.toSet());
        this.names.add(name);
    }

    public String getDescription() {
        return description;
    }

    /**
     * Determines if this name identifies this option
     * @param name the name to check
     * @return true if this name identifies this option
     */
    public List<String> getNames(final String name) {
        return names.stream().collect(Collectors.toList());
    }

    /**
     * Determines if this option takes an argument
     * @return true if the option takes an argument
     */
    public abstract boolean takesArgument();

    /**
     * Determines if this option is well formed
     * @return a boolean indicating if this argument is well formed
     */
    public abstract boolean isArgumentLegal(final String argument);
}

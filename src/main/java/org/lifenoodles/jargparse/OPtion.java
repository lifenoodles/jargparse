package org.lifenoodles.jargparse;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an argument that can be parsed
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
abstract class Option {
    private final Set<String> names;

    public Option(final String ... names) {
        this.names = Arrays.stream(names).collect(Collectors.toSet());
    }

    /**
     * Determines if this option takes an argument
     * @return true if the option takes an argument
     */
    public boolean hasArgument() {
        return true;
    }

    /**
     * Determines if this name identifies this option
     * @param name the name to check
     * @return true if this name identifies this option
     */
    public boolean hasName(final String name) {
        return names.contains(name);
    }

    /**
     * Determines if this option is well formed
     * @return a boolean indicating if this argument is well formed
     */
    public abstract boolean isWellFormed();
}

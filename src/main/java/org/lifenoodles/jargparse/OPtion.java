package org.lifenoodles.jargparse;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an argument that can be parsed
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
abstract class Option {
    private final Set<String> names;
    private final String description;
    private Optional<String> argument;

    public Option(final String description,
                  final String name, final String ... otherNames) {
        this.description = description;
        this.names = Arrays.stream(otherNames).collect(Collectors.toSet());
        this.names.add(name);
    }

    public Optional<String> getArgument() {
        return argument;
    }

    public Option setArgument(final String argument) {
        this.argument = Optional.of(argument);
        return this;
    }

    public String getDescription() {
        return description;
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
     * Determines if this option takes an argument
     * @return true if the option takes an argument
     */
    public abstract boolean takesArgument();

    /**
     * Determines if this option is well formed
     * @return a boolean indicating if this argument is well formed
     */
    public abstract boolean isWellFormed();
}

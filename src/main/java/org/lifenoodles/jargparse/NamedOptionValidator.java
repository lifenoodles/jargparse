package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an argument that can be parsed
 *
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
abstract class NamedOptionValidator implements OptionValidator {
    private final String name;
    private final Set<String> aliases;
    private final String description;

    public NamedOptionValidator(final String description,
            final String name, final String... otherNames) {
        this.description = description;
        this.name = name;
        this.aliases = Arrays.stream(otherNames).collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}

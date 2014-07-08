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
    private final Set<String> aliases;
    private final String name;
    private final String description;

    public NamedOptionValidator(final String description,
            final String name, final String... aliases) {
        this.description = description;
        this.name = name;
        this.aliases = Arrays.stream(aliases).collect(Collectors.toSet());
    }

    public Set<String> getAliases() {
        return new HashSet<>(aliases);
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}

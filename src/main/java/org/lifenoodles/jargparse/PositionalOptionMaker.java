package org.lifenoodles.jargparse;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 7/22/14.
 */

public class PositionalOptionMaker {
    private final String name;
    private final List<String> aliases;
    private boolean nOptionalArguments = false;
    private int argumentCount = 1;
    private int optionalArgumentCount = 0;
    private String description = "";
    private Predicate<String> predicate = x -> true;

    PositionalOptionMaker(final String name) {
        this.name = name;
        aliases = new LinkedList<>();
    }

    public PositionalOptionMaker alias(final String ... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public PositionalOptionMaker arguments(final int argumentCount) {
        this.argumentCount = argumentCount;
        return this;
    }

    public PositionalOptionMaker optionalArguments(final int argumentCount) {
        this.optionalArgumentCount = argumentCount;
        return this;
    }

    public PositionalOptionMaker optionalArguments() {
        this.nOptionalArguments = true;
        return this;
    }

    public PositionalOptionMaker description(final String description) {
        this.description = description;
        return this;
    }

    public PositionalOptionMaker matches(final Predicate<String> predicate) {
        this.predicate = predicate;
        return this;
    }

    public PositionalOptionValidator make() {
        return new PositionalOptionValidator(description, argumentCount,
                optionalArgumentCount, nOptionalArguments, predicate, name,
                aliases.toArray(new String[aliases.size()]));
    }
}

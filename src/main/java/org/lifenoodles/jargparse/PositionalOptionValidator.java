package org.lifenoodles.jargparse;

import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 7/22/14.
 */

public class PositionalOptionValidator extends OptionValidator {
    private final boolean nOptionalArguments;
    private final int optionalArgumentCount;

    public PositionalOptionValidator(final String description,
            final int argumentCount,
            final int optionalArgumentCount,
            boolean nOptionalArguments,
            final Predicate<String> predicate,
            final String name,
            final String... names) {
        super(description, argumentCount, predicate, name, names);
        this.optionalArgumentCount = optionalArgumentCount;
        this.nOptionalArguments = nOptionalArguments;
    }

    public int getOptionalArgumentCount() {
        return optionalArgumentCount;
    }

    public boolean nOptionalArguments() {
        return nOptionalArguments;
    }
}

package org.lifenoodles.jargparse;

import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
class StringOptionValidator extends NamedOptionValidator {
    private final Predicate<String> predicate;

    public StringOptionValidator(final Predicate<String> predicate,
            final String description,
            final String name, final String... names) {
        super(description, name, names);
        this.predicate = predicate;
    }

    @Override
    public boolean takesArgument() {
        return true;
    }

    @Override
    public boolean isArgumentLegal(final String argument) {
        return predicate.test(argument);
    }
}

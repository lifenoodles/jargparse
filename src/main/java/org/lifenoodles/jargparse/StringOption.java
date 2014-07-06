package org.lifenoodles.jargparse;

import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
class StringOption extends Option {
    private final Predicate<String> predicate;
    public StringOption(final Predicate<String> predicate,
                        final String description,
                        final String name, final String ... names) {
        super(description, name, names);
        this.predicate = predicate;
    }

    @Override
    public boolean takesArgument() {
        return true;
    }

    @Override
    public boolean isWellFormed() {
        return getArgument().isPresent() &&
                predicate.test(getArgument().get());
    }
}

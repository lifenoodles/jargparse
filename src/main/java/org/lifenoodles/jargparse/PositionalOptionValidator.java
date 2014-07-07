package org.lifenoodles.jargparse;

import java.util.function.Predicate;

/**
 * Created by donagh on 7/7/14.
 */
public class PositionalOptionValidator implements OptionValidator {
    private final Predicate<String> predicate;
    private final String description;

    public PositionalOptionValidator(final Predicate<String> predicate,
            final String description) {
        this.predicate = predicate;
        this.description = description;
    }

    @Override
    public boolean takesArgument() {
        return true;
    }

    @Override
    public boolean isArgumentLegal(final String argument) {
        return predicate.test(argument);
    }

    @Override
    public String getDescription() {
        return description;
    }
}

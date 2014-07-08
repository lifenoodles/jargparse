package org.lifenoodles.jargparse;

import java.util.function.Predicate;

/**
 * Created by donagh on 7/7/14.
 */
public class PositionalOptionValidator implements OptionValidator {
    private final String name;
    private final String description;
    private final int position;
    private final Predicate<String> predicate;

    public PositionalOptionValidator(final int position,
            final Predicate<String> predicate, final String description,
            final String name) {
        this.name = name;
        this.description = description;
        this.position = position;
        this.predicate = predicate;
    }

    @Override
    public boolean isArgumentLegal(final String argument) {
        return predicate.test(argument);
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean takesArgument() {
        return true;
    }
}

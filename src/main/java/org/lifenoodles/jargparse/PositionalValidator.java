package org.lifenoodles.jargparse;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 8/21/14.
 */
public class PositionalValidator extends Validator {
    /**
     * @param names           the list of names this validator has
     * @param description     a text description of the option
     * @param argumentCounter the counter to use for counting arguments
     * @param predicate       a predicate that arguments must match to be legal
     * @param argumentLabels  a list of argument labels for description
     */
    public PositionalValidator(final List<String> names,
            final String description,
            final ArgumentCounter argumentCounter,
            final Predicate<String> predicate,
            final List<String> argumentLabels) {
        super(names, description, argumentCounter, predicate, argumentLabels);
    }
}

package org.lifenoodles.jargparse;

import java.util.function.Predicate;

/**
 * Created by donagh on 7/7/14.
 */
public class PositionalOptionValidator extends StringOptionValidator {
    public PositionalOptionValidator(final Predicate<String> predicate,
            final String description,
            final String name) {
        super(predicate, description, name);
    }
}

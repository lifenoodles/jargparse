package org.lifenoodles.jargparse;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Donagh Hatton
 *         created on 7/22/14.
 */

class OptionalValidator extends OptionValidator {
    public OptionalValidator(final List<String> names,
            final String description,
            final ArgumentCounter optionParser,
            final Predicate<String> predicate,
            final List<String> prefixes,
            final List<String> argumentLabels) {
        super(names, description, optionParser, predicate, prefixes,
                argumentLabels);

    }

    public boolean isArgumentCountCorrect(final List<String> arguments) {
        return super.isArgumentCountCorrect(Utility.dropN(1, arguments));
    }
}

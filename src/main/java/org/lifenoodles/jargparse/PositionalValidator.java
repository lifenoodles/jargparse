package org.lifenoodles.jargparse;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Donagh Hatton
 *         created on 7/22/14.
 */

class PositionalValidator extends OptionValidator {
    public PositionalValidator(final String name,
            final String description,
            final ArgumentCounter optionParser,
            final Predicate<String> predicate,
            final List<String> optionPrefixes,
            final List<String> argumentLabels) {
        super(Stream.of(name).collect(Collectors.toList()),
                description, optionParser, predicate, optionPrefixes,
                argumentLabels);
        if (isOption(name)) {
            throw new IllegalArgumentException(String.format(
                    "Illegal name: %s, positional argument begins with a " +
                            "prefix string", name));
        }
    }

    @Override
    public String helpFormat() {
        return String.format("%s", formatLabels());
    }
}

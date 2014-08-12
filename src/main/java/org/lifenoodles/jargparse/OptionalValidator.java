package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.parsers.OptionParser;

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
            final OptionParser optionParser,
            final Predicate<String> predicate,
            final List<String> prefixes) {
        super(names, description, optionParser, predicate, prefixes);
        List<String> badNames = names.stream()
                .filter(x -> !isOption(x)).collect(Collectors.toList());
        if (badNames.size() > 0) {
            throw new IllegalArgumentException(String.format(
                    "Illegal name: %s, optional arguments must begin with a " +
                            "prefix string",
                    badNames.get(0)));
        }
    }

    public boolean isArgumentCountCorrect(final List<String> arguments) {
        return super.isArgumentCountCorrect(Utility.dropN(1, arguments));
    }

    public List<String> extractArguments(final List<String> arguments) {
        return super.extractArguments(Utility.dropN(1, arguments));
    }

    public List<String> restOfArguments(final List<String> arguments) {
        return super.restOfArguments(Utility.dropN(1, arguments));
    }
}

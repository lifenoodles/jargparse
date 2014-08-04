package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.parsers.OptionParser;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 7/22/14.
 */

class OptionalValidator extends OptionValidator {
    public OptionalValidator(final List<String> names,
            final String description,
            final OptionParser optionParser,
            final Predicate<String> predicate) {
        super(names, description, optionParser, predicate);
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

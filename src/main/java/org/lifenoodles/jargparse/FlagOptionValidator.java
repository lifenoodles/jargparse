package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
class FlagOptionValidator extends NamedOptionValidator {
    public FlagOptionValidator(final String description,
            final String name, final String... names) {
        super(description, name, names);
    }

    @Override
    public boolean takesArgument() {
        return false;
    }

    @Override
    public boolean isArgumentLegal(String argument) {
        return false;
    }
}

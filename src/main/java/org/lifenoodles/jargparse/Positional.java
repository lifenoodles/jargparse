package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 8/21/14.
 */
public class Positional extends OptionMaker<Positional> {
    protected Positional(final String... names) {
        super(names);
        arguments(1);
    }

    /**
     * Create a positional option maker
     *
     * @param name name of this option
     * @return this
     */
    public static Positional of(final String name) {
        return new Positional(name);
    }

    /**
     * Instantiate the validator represented by this maker object. This method
     * must be invoked to finalise construction of the validator
     *
     * @return a new validator with the properties specified by this maker
     */
    PositionalValidator make() {
        return new PositionalValidator(getNames(), getDescription(),
                getArgumentCounter(), getPredicate(), getArgumentLabels());
    }
}

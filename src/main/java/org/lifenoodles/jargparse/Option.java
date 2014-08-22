package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 8/21/14.
 */
public class Option extends OptionMaker<Option> {
    private boolean isRequired = false;
    private boolean isHelper = false;

    protected Option(final String... names) {
        super(names);
    }

    /**
     * Create an option maker identified by the given names
     *
     * @param name the main name of the option
     * @param aliases an optional list of aliases
     * @return this
     */
    public static Option of(final String name, final String... aliases) {
        final String[] names = new String[aliases.length + 1];
        names[0] = name;
        System.arraycopy(aliases, 0, names, 1, aliases.length);
        return new Option(names);
    }

    /**
     * Mark this option as required. Parsing will fail if it is not included
     * unless an option marked as a helper is included
     *
     * @return this
     */
    public Option required() {
        isRequired = true;
        return this;
    }

    /**
     * Mark this option as a helper. If a helper option is present required
     * arguments will not be taken into account
     *
     * @return this
     */
    public Option helper() {
        isHelper = true;
        return this;
    }

    /**
     * Instantiate the validator represented by this maker object. This method
     * must be invoked to finalise construction of the validator
     *
     * @return a new validator with the properties specified by this maker
     */
    OptionValidator make() {
        return new OptionValidator(getNames(), getDescription(),
                getArgumentCounter(), getPredicate(), getArgumentLabels(),
                isHelper, isRequired);
    }
}

package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Maker class for OptionalValidators, optionalValidators have an alias option
 *
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
class OptionalMaker extends OptionMaker<OptionalMaker> {
    private final List<String> aliases = new ArrayList<>();

    protected OptionalMaker(final String name) {
        super(name);
    }

    /**
     * Set aliases for this option, typically these aliases will be long form
     * versions of the option name. e.g. "--test" instead of "-t". Any of these
     * aliases will be recognised by the parser as belonging to this option
     *
     * @param names the list of names to use as aliases
     * @return this
     */
    public OptionalMaker alias(final String... names) {
        aliases.addAll(Arrays.asList(names));
        return this;
    }

    @Override
    public OptionValidator make() {
        List<String> names = new ArrayList<>(aliases);
        names.add(0, getName());
        return new OptionValidator(names, getDescription(),
                getArgumentCounter(), getPredicate(), getArgumentLabels());
    }
}

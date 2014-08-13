package org.lifenoodles.jargparse;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
class PositionalMaker extends OptionMaker<PositionalMaker> {
    protected PositionalMaker(final String name,
            final List<String> optionPrefixes) {
        super(name, optionPrefixes);
    }

    @Override
    public PositionalValidator make() {
        return new PositionalValidator(getName(), getDescription(),
                getOptionParser(), getPredicate(), getOptionPrefixes(),
                getArgumentLabels());
    }
}

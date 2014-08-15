package org.lifenoodles.jargparse;

/**
 * Maker for positional arguments, positional arguments lack an alias option
 *
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
class PositionalMaker extends OptionMaker<PositionalMaker> {
    protected PositionalMaker(final String name) {
        super(name);
    }

    @Override
    public PositionalValidator make() {
        return new PositionalValidator(getName(), getDescription(),
                getArgumentCounter(), getPredicate(), getArgumentLabels());
    }
}

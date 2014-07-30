package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
class PositionalMaker extends Option<PositionalMaker> {
    protected PositionalMaker(final String name) {
        super(name);
    }

    @Override
    public PositionalValidator make() {
        return new PositionalValidator(getName(), getDescription(),
                getOptionParser(), getPredicate());
    }
}

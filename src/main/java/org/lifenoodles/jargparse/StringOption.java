package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
class StringOption extends Option {
    public StringOption(final String name, final String ... names) {
        super(name, names);
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}

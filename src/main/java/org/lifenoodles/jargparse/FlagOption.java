package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class FlagOption extends Option {
    public FlagOption(final String name, final String ... names) {
        super(name, name, names);
    }

    @Override
    public boolean hasArgument() {
        return false;
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}

package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class FlagOption extends Option {
    public FlagOption(final String description,
                      final String name, final String ... names) {
        super(description, name, names);
    }

    @Override
    public boolean takesArgument() {
        return false;
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}

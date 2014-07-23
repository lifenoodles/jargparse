package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */

public class Option {
    public static OptionMaker optional(final String name) {
        return new OptionMaker(name);
    }

    public static PositionalOptionMaker positional(final String name) {
        return new PositionalOptionMaker(name);
    }
}

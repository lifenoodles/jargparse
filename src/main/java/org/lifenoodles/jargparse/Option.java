package org.lifenoodles.jargparse;

/**
 * Created by Donagh Hatton on 7/8/14.
 */
public class Option {
    public static FlagOptionMaker flag(final String name) {
        return new FlagOptionMaker(name);
    }

    public static StringOptionMaker string(final String name) {
        return new StringOptionMaker(name);
    }

    public static PositionalOptionMaker positional(final String name,
            final int position) {
        return new PositionalOptionMaker(name, position);
    }
}

package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 8/15/14.
 */
public class Argument {
    /**
     * Gets a maker object for constructing an optional argument validator
     *
     * @param name the name of the option
     * @return the maker object
     */
    public static OptionalMaker optional(String name) {
        return new OptionalMaker(name);
    }

    /**
     * Gets a maker object for constructing a positional argument validator
     *
     * @param name the name of the argument
     * @return the maker object
     */
    public static PositionalMaker positional(String name) {
        return new PositionalMaker(name);
    }
}

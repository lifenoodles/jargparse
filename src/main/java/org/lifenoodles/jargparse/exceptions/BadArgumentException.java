package org.lifenoodles.jargparse.exceptions;

/**
 * Exception to represent the situation where an argument fails to match the
 * predicate of the validator
 *
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class BadArgumentException extends Exception {
    public final String argument;
    public final String option;

    public BadArgumentException(final String option, final String argument) {
        super(String.format("%s is not a valid argument for option %s",
                argument, option));
        this.argument = argument;
        this.option = option;
    }
}


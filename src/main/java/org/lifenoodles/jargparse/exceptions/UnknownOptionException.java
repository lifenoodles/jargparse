package org.lifenoodles.jargparse.exceptions;

/**
 * Exception to represent a situation where an unknown option is encountered
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class UnknownOptionException extends Exception {
    public final String option;

    public UnknownOptionException(final String option) {
        super(String.format("Unrecognised option %s", option));
        this.option = option;
    }
}


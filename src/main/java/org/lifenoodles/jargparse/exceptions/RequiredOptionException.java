package org.lifenoodles.jargparse.exceptions;

/**
 * Exception to represent a situation where a required option is omitted
 *
 * @author Donagh Hatton
 *         created on 8/20/14.
 */
public class RequiredOptionException extends Exception {
    public final String option;

    public RequiredOptionException(final String option) {
        this.option = option;
    }
}

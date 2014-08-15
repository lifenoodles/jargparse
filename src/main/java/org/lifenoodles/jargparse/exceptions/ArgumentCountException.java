package org.lifenoodles.jargparse.exceptions;

/**
 * Exception to represent the situation where an incorrect count of arguments
 * was received for a validator
 *
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class ArgumentCountException extends Exception {
    public final int expected;
    public final int received;
    public final String option;

    public ArgumentCountException(final String option, final int expected,
            final int received) {
        super(String.format(
                "Bad argument count for option %s: expected %d, found %d",
                option, expected, received));
        this.option = option;
        this.expected = expected;
        this.received = received;
    }
}


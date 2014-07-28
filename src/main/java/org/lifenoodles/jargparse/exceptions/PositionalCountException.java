package org.lifenoodles.jargparse.exceptions;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class PositionalCountException extends Exception {
    public final int expected;
    public final int received;

    public PositionalCountException(final int expected, final int received) {
        super(String.format(
                "Bad argument count: expected %d, found %d",
                expected, received));
        this.expected = expected;
        this.received = received;
    }
}


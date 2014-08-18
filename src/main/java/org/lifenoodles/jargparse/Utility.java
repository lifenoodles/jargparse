package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Provides some utility functions for testing
 *
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
final class Utility {
    /**
     * Drop n items from a list
     *
     * @param n number of items to drop
     * @param xs list of items
     * @param <T> type of item in the list
     * @return the new list
     */
    public static <T> List<T> dropN(final int n, final List<T> xs) {
        return xs.stream().skip(n).collect(Collectors.toList());
    }

    /**
     * Easy list construction from variable args parameters
     *
     * @param ts the list of args to put into a list
     * @return a list made up of the elements of ts
     */
    @SafeVarargs
    public static <T> List<T> listOf(T... ts) {
        return Arrays.stream(ts).collect(Collectors.toList());
    }
}

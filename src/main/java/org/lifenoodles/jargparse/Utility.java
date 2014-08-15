package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Determine if both lists are the same
     *
     * @param xs first list
     * @param ys second list
     * @param <T> type of the lists
     * @return true if all elements in each list are .equal() to each other
     */
    public static <T> boolean sameLists(List<T> xs, List<T> ys) {
        Iterator<T> x = xs.iterator(), y = ys.iterator();
        while (x.hasNext() && y.hasNext()) {
            if (!x.next().equals(y.next())) {
                return false;
            }
        }
        return !(x.hasNext() || y.hasNext());
    }
}

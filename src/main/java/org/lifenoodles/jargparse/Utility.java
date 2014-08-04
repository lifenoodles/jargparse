package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public final class Utility {
    public static final String OPTION_PREFIX = "-";

    public static boolean isOption(String option) {
        return option.startsWith(OPTION_PREFIX);
    }

    public static int argumentCount(List<String> arguments) {
        return firstArgumentIndex(arguments).orElse(arguments.size());
    }

    public static Optional<Integer> firstArgumentIndex(List<String> arguments) {
        for (int i = 0; i < arguments.size(); ++i) {
            if (isOption(arguments.get(i))) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public static List<String> listOf(String ... strings) {
        return Arrays.stream(strings).collect(Collectors.toList());
    }

    public static <T> boolean sameLists(List<T> xs, List<T> ys) {
        Iterator<T> x = xs.iterator(), y = ys.iterator();
        while (x.hasNext() && y.hasNext()) {
            if (!x.next().equals(y.next())) {
                return false;
            }
        }
        return !(x.hasNext() || y.hasNext());
    }

    public static <T> List<T> dropN(final int n, final List<T> xs) {
        List<T> rest = new ArrayList<T>();
        for (int i = n; i < xs.size(); ++i) {
            rest.add(xs.get(i));
        }
        return rest;
    }
}

package org.lifenoodles.jargparse;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public final class Utility {
    public static boolean isOption(String option) {
        return option.startsWith("-");
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
}

package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public final class Utility {
    public static boolean isOption(String option) {
        return option.startsWith("-");
    }
}

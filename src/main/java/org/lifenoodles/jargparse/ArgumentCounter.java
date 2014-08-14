package org.lifenoodles.jargparse;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public interface ArgumentCounter {
    /**
     * @return the minimum count of arguments that are required for this counter
     */
    public abstract int minimumArgumentCount();

    /**
     * @return the count of options allowed by this counter
     */
    public abstract int maximumArgumentCount();

    /**
     * Format the given labels according to the rules of this counter
     * @param argumentLabels the labels to format
     * @return a formatted string
     */
    public abstract String formatLabels(List<String> argumentLabels);
}

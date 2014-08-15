package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A validator for command line options, reports on legality of arguments
 * as well as allowing extraction of arguments from a given list
 *
 * @author Donagh Hatton
 *         created on 7/22/14.
 */
class OptionValidator {
    private final String description;
    private final List<String> names;
    private final ArgumentCounter argumentCounter;
    private final Predicate<String> predicate;
    private final List<String> argumentLabels;

    /**
     * @param names           the list of names this validator has
     * @param description     a text description of the option
     * @param argumentCounter the counter to use for counting arguments
     * @param predicate       a predicate that arguments must match to be legal
     * @param argumentLabels  a list of argument labels for description
     */
    public OptionValidator(final List<String> names,
            final String description,
            final ArgumentCounter argumentCounter,
            final Predicate<String> predicate,
            final List<String> argumentLabels) {
        this.names = new ArrayList<>(names);
        this.description = description;
        this.argumentCounter = argumentCounter;
        this.predicate = predicate;
        this.argumentLabels = new ArrayList<>(argumentLabels);
        if (this.argumentLabels.isEmpty()) {
            this.argumentLabels.add(getName().toUpperCase());
        }
    }

    /**
     * @return the description of this option
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the name of this option
     */
    public String getName() {
        assert (!names.isEmpty());
        return names.get(0);
    }

    /**
     * @return return the full list of names of this option
     */
    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    /**
     * Determine if this argument is legal
     *
     * @param argument argument to check
     * @return true if the argument is legal
     */
    public boolean isArgumentLegal(final String argument) {
        return predicate.test(argument);
    }

    /**
     * Construct a summary for printing purposes
     *
     * @return the help summary for this validator
     */
    public String helpFormat() {
        return String.format("%s %s", getName(), formatLabels()).trim();
    }

    /**
     * @return the maximum number of arguments this validator can accept
     */
    public int maximumArgumentCount() {
        return argumentCounter.maximumArgumentCount();
    }

    /**
     * @return the minimum number of arguments this counter can accept
     */
    public int minimumArgumentCount() {
        return argumentCounter.minimumArgumentCount();
    }

    /**
     * format the argument labels according to the parser used
     *
     * @return the formatted labels
     */
    protected String formatLabels() {
        return String.format("%s", argumentCounter.formatLabels(argumentLabels));
    }
}

package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A validator for command line options, reports on legality of arguments
 * as well as allowing extraction of arguments from a given list
 *
 * @author Donagh Hatton
 *         created on 7/22/14.
 */
abstract class OptionValidator {
    private final String description;
    private final List<String> names;
    private final ArgumentCounter argumentCounter;
    private final Predicate<String> predicate;
    private final List<String> prefixes;
    private final List<String> argumentLabels;

    /**
     * @param names          the list of names this validator has
     * @param description    a text description of the option
     * @param argumentCounter   the counter to use for counting arguments
     * @param predicate      a predicate that arguments must match to be legal
     * @param prefixes       a list of legal prefixes for option names
     * @param argumentLabels a list of argument labels for description
     */
    public OptionValidator(final List<String> names,
            final String description,
            final ArgumentCounter argumentCounter,
            final Predicate<String> predicate,
            final List<String> prefixes,
            final List<String> argumentLabels) {
        this.names = new ArrayList<>(names);
        this.description = description;
        this.argumentCounter = argumentCounter;
        this.predicate = predicate;
        this.prefixes = new ArrayList<>(prefixes);
        this.argumentLabels = new ArrayList<>(argumentLabels);
        if (this.argumentLabels.isEmpty()) {
            this.argumentLabels.add(stripPrefix(getName()).toUpperCase());
        }
    }

    /**
     * Get a list of arguments to this validator that are illegel
     *
     * @param arguments the list of arguements
     * @return a list of illegal arguments
     */
    public List<String> getBadArguments(List<String> arguments) {
        return arguments.stream().limit(argumentCounter.maximumArgumentCount())
                .filter(predicate.negate()).collect(Collectors.toList());
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
     * Determines if the list of arguments contains enough arguments
     *
     * @param arguments the list of arguments
     * @return true if there are enough arguments in the list
     */
    public boolean isArgumentCountCorrect(final List<String> arguments) {
        return argumentCounter.minimumArgumentCount() <= arguments.size() &&
                arguments.size() <= argumentCounter.maximumArgumentCount();
    }

    /**
     * Determine if this list of arguments is legal for this validator
     *
     * @param arguments the list of arguments
     * @return true if this validator can parse each argument
     */
    public boolean isArgumentListLegal(final List<String> arguments) {
        return getBadArguments(arguments).isEmpty();
    }

    /**
     * Gets the expected minimum number of arguments for this validator
     *
     * @return the minimum expected argument count
     */
    public int argumentCount() {
        return argumentCounter.maximumArgumentCount();
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
     * Strips the longest possible prefix from the given string
     *
     * @param name the string to strip
     * @return name with the prefix removed
     */
    private String stripPrefix(final String name) {
        return prefixes.stream()
                .filter(name::startsWith)
                .map(x -> name.replaceFirst(x, ""))
                .max((x, y) -> y.length() - x.length()).orElse(name);
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

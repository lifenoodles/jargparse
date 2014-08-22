package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Set of options parsed from arguments
 *
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */
public class OptionSet {
    public final Map<String, List<String>> optionMap = new HashMap<>();

    protected OptionSet() {
    }

    /**
     * Gets the first argument passed to this option
     *
     * @param option name of the option to check
     * @return String if the option exists and at least 1 argument was passed
     * to it, empty otherwise
     */
    public Optional<String> getArgument(String option) {
        return Optional.ofNullable(optionMap.get(option))
                .filter(x -> x.size() > 0).map(x -> x.get(0));
    }

    /**
     * Get the list of arguments passed to this option
     *
     * @param option name of the option to check
     * @return a list of arguments to this option, empty list if the option
     * was not parsed or had no arguments
     */
    public List<String> getArguments(String option) {
        return Optional.ofNullable(optionMap.get(option))
                .orElseGet(ArrayList::new);
    }

    /**
     * Determine if the named option was parsed
     *
     * @param name name of the option to check for
     * @return true if this option was parsed
     */
    public boolean contains(String name) {
        return optionMap.containsKey(name);
    }

    /**
     * Add a validator and list of arguments to this opionSet
     *
     * @param validator the validator used for this list of arguments
     * @param arguments the list of arguments parsed for this validator
     */
    protected void addOption(Validator validator, List<String> arguments) {
        assert (validator.getNames().stream()
                .noneMatch(optionMap::containsKey));
        optionMap.putAll(validator.getNames().stream()
                .collect(Collectors.toMap(x -> x, x -> arguments)));
    }
}

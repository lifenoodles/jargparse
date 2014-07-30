package org.lifenoodles.jargparse;


import java.util.*;

/**
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */

public class OptionSet {
    public final Set<String> flagSet;
    public final Map<String, List<String>> namesToArguments;

    OptionSet() {
        flagSet = new HashSet<>();
        namesToArguments = new HashMap<>();
    }

    public boolean isOptionPresent(String name) {
        return flagSet.contains(name) || namesToArguments.containsKey(name);
    }

    public Optional<String> getArgument(String option) {
        return Optional.ofNullable(namesToArguments.get(option)).map(
                x -> x.get(0));
    }

    public Optional<List<String>> getArguments(String option) {
        return Optional.ofNullable(namesToArguments.get(option));
    }

    public void addOption(OptionValidator validator, List<String> arguments) {
        assert(validator.getNames().stream().noneMatch(flagSet::contains));
        flagSet.addAll(validator.getNames());
    }

    public void addOption(PositionalValidator validator,
            List<String> arguments) {
        assert(validator.getNames().stream().noneMatch(
                namesToArguments::containsKey));
        validator.getNames().forEach(
                x -> namesToArguments.put(x, arguments));
    }
}

package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */

public class OptionSet {
    public final Set<String> flagSet;
    public final Map<String, String> namesToOptions;
    public final List<String> positionalArguments;

    public OptionSet() {
        flagSet = new HashSet<>();
        namesToOptions = new HashMap<>();
        positionalArguments = new LinkedList<>();
    }

    public boolean isArgumentPresent(String name) {
        return flagSet.contains(name) ||
                namesToOptions.containsKey(name);
    }

    public OptionSet addFlagOption(FlagOptionValidator validator) {
        flagSet.addAll(validator.getAliases());
        return this;
    }

    public OptionSet addStringOption(StringOptionValidator validator,
            String argument) {
        namesToOptions.putAll(validator.getAliases().stream().collect(
                Collectors.toMap(x -> x, y -> argument)));
        return this;
    }

    public void addOption(OptionValidator validator, List<String> arguments) {
    }

    public void addPositionalArgument(PositionalOptionValidator validator, List<String> arguments) {

    }
}

package org.lifenoodles.jargparse;

import java.util.*;

/**
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */

public class OptionSet {
    public final Set<String> flagSet;
    public final Map<String, OptionValidator> namesToValidators;
    public final List<String> positionalArguments;

    public OptionSet() {
        flagSet = new HashSet<>();
        namesToValidators = new HashMap<>();
        positionalArguments = new LinkedList<>();
    }

    public boolean isArgumentPresent(String name) {
        return flagSet.contains(name) || namesToValidators.containsKey(name);
    }

    public void addOption(OptionValidator validator, List<String> arguments) {
    }
}

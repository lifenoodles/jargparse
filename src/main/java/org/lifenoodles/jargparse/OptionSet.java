package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */

public class OptionSet {
    public final Map<String, List<String>> optionMap = new HashMap<>();

    public boolean contains(String name) {
        return optionMap.containsKey(name);
    }

    public Optional<String> getArgument(String option) {
        return Optional.ofNullable(optionMap.get(option))
                .filter(x -> x.size() > 0).map(x -> x.get(0));
    }

    public List<String> getArguments(String option) {
        return Optional.ofNullable(optionMap.get(option))
                .orElseGet(ArrayList::new);
    }

    public void addOption(OptionValidator validator, List<String> arguments) {
        assert (validator.getNames().stream()
                .noneMatch(optionMap::containsKey));
        optionMap.putAll(validator.getNames().stream()
                .collect(Collectors.toMap(x -> x, x -> arguments)));
    }
}

package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Parses an array of strings looking for specified patterns,
 * contains methods to register options as well as validate input
 *
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */

public class ArgumentParser {
    private final Map<String, OptionValidator> namesToValidators;
    private final Map<String, List<String>> namesToArguments;
    private final Set<String> flagSet;
    private final List<String> positionalArguments;
    private final List<PositionalOptionValidator> positionalValidators;
    private final List<String> unrecognisedOptions;

    public ArgumentParser() {
        namesToValidators = new HashMap<>();
        namesToArguments = new HashMap<>();
        flagSet = new HashSet<>();
        positionalArguments = new ArrayList<>();
        positionalValidators = new ArrayList<>();
        unrecognisedOptions = new ArrayList<>();
    }

    public boolean isOptionPresent(final String option) {
        return resolveName(option).map(flagSet::contains).orElse(false) ||
                resolveName(option).map(namesToArguments::containsKey).orElse(false);
    }

    /**
     * Determines whether or not the arguments that have been parsed so far are
     * well formed. An unused ArgumentParser will always return true unless
     * there are positional arguments as well.
     *
     * @return true if args are are well formed
     */
    public boolean isWellFormed() {
        final boolean isLegal = namesToArguments.keySet().stream()
                .map(k -> namesToArguments.get(k).stream()
                        .allMatch(arg -> namesToValidators.get(k)
                                .isArgumentLegal(arg)))
                .allMatch(x -> x);
        final boolean isCorrectPositionalCount = positionalArgumentsExpected()
                == positionalArgumentsParsed();
        return isLegal && isCorrectPositionalCount;
    }

    public List<String> getUnrecognisedOptions() {
        return unrecognisedOptions.stream().collect(Collectors.toList());
    }

    public List<OptionValuePair> getBadOptionValuePairs() {
        return namesToArguments.keySet().stream()
                .flatMap(n -> namesToArguments.get(n).stream()
                        .filter(arg -> !namesToValidators.get(n)
                                .isArgumentLegal(arg))
                        .map(arg -> new OptionValuePair(n, arg)))
                .collect(Collectors.toList());
    }

    public Optional<String> getArgument(final String option) {
        return Optional.ofNullable(namesToArguments.get(option))
                .map(x -> x.get(0));
    }

    public List<String> getArguments(final String option) {
        return Optional.ofNullable(namesToArguments.get(option))
                .orElseGet(ArrayList::new);
    }

    public Optional<String> getPositionalArgument(final int index) {
        return index < positionalArguments.size() ?
                Optional.of(positionalArguments.get(index)) :
                Optional.empty();
    }

    public ArgumentParser addOption(final OptionValidator validator) {
        final Set<String> names = new HashSet<>(validator.getNames());
        names.retainAll(namesToValidators.keySet());
        if (names.size() > 0) {
            throw new IllegalArgumentException(
                    String.format("Name: %s already used for an option",
                            names.stream().findAny().get()));
        }
        names.forEach(x -> namesToValidators.put(x, validator));
        return this;
    }

    public ArgumentParser addOption(final PositionalOptionValidator validator) {
        positionalValidators.add(validator);
        return this;
    }

    /**
     * Parse the provided arguments using any rules that have been registered
     *
     * @param arguments the array of arguments
     * @return this
     */
    public ArgumentParser parse(final String... arguments) {
        for (int i = 0; i < arguments.length - positionalArgumentsExpected(); ++i) {
            Optional<NamedOptionValidator> validator =
                    getValidatorFromName(arguments[i]);
            if (validator.isPresent() && validator.get().getArgumentCount()) {
                addNameArgumentEntry(validator.get().getName(), arguments[i + 1]);
                ++i;
            } else if (validator.isPresent()) {
                flagSet.add(validator.get().getName());
            } else {
                unrecognisedOptions.add(arguments[i]);
            }
        }
        // read positional arguments
        for (int i = Math.max(arguments.length - positionalArgumentsExpected(), 0);
                i < arguments.length; ++i) {
           positionalArguments.add(arguments[i]);
        }
        return this;
    }

    public int positionalArgumentsParsed() {
        return positionalArguments.size();
    }

    public int positionalArgumentsExpected() {
        return positionalValidators.size();
    }

    private Optional<String> resolveName(final String name) {
        return Optional.ofNullable(namesToValidators.get(name))
                .map(OptionValidator::getName);
    }
}

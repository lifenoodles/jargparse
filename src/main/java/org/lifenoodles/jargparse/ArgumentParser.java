package org.lifenoodles.jargparse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Predicate;

/**
 * Parses an array of strings looking for specified patterns,
 * contains methods to register options as well as validate input
 *
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class ArgumentParser {
    private final Map<String, NamedOptionValidator> namesToValidators;
    private final Map<String, String> namesToArguments;
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


    /**
     * Determines whether or not the arguments that have been parsed so far are
     * well formed. An unused ArgumentParser will always return true unless
     * there are positional arguments as well.
     *
     * @return true if args are are well formed
     */
    public boolean isWellFormed() {
        final boolean isLegal = namesToArguments.keySet().stream().allMatch(k ->
                namesToValidators.get(k).isArgumentLegal(namesToArguments.get(k)));
        final boolean isCorrectPositionalCount = positionalArgumentsExpected()
                == positionalArgumentsParsed();
        return isLegal && isCorrectPositionalCount;
    }

    public List<String> getUnrecognisedOptions() {
        return unrecognisedOptions.stream().collect(Collectors.toList());
    }

    public List<OptionValuePair> getBadOptionValuePairs() {
        return namesToArguments.keySet().stream()
                .filter(k -> !namesToValidators.get(k)
                        .isArgumentLegal(namesToArguments.get(k)))
                .map(x -> new OptionValuePair(x, namesToArguments.get(x)))
                .collect(Collectors.toList());
    }

    public ArgumentParser addFlagOption(final String description,
            final String name, final String... names) {
        final FlagOptionValidator validator = new FlagOptionValidator(
                description, name, names);
        return addOption(validator, name, names);
    }

    public ArgumentParser addStringOption(final String description,
            final String name, final String... names) {
        return addStringOption(x -> true, description, name, names);
    }

    public ArgumentParser addStringOption(final Predicate<String> predicate,
            final String description, final String name,
            final String... names) {
        final StringOptionValidator validator = new StringOptionValidator(
                predicate, description, name, names);
        return addOption(validator, name, names);
    }

    public ArgumentParser addPositionalArgument(final String description) {
        return addPositionalArgument(x -> true, description);
    }

    public ArgumentParser addPositionalArgument(
            final Predicate<String> predicate, final String description) {
        final PositionalOptionValidator validator =
                new PositionalOptionValidator(predicate, description);
        return this;
    }

    /**
     * Parse the provided arguments using any rules that have been registered
     *
     * @param arguments the array of arguments
     * @return this
     */
    public ArgumentParser parse(final String... arguments) {
        clear();
        for (int i = 0; i < arguments.length - positionalArgumentsExpected(); ++i) {
            Optional<NamedOptionValidator> validator =
                    getValidatorFromName(arguments[i]);
            if (validator.isPresent() && validator.get().takesArgument()) {
                namesToArguments.put(
                        validator.get().getName(), arguments[i + 1]);
                ++i;
            } else if (validator.isPresent()) {
                flagSet.add(validator.get().getName());
            } else {
                unrecognisedOptions.add(arguments[i]);
            }
        }
        // read positional arguments
        for (int i = arguments.length - positionalArgumentsExpected();
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

    private ArgumentParser addOption(final NamedOptionValidator validator,
            final String name, final String... names) {
        final List<String> nameList = Arrays.asList(names);
        nameList.add(name);
        final List<String> existingNameList = findExistingNames(nameList);
        if (existingNameList.size() > 0) {
            throw new IllegalArgumentException(
                    String.format("Name: %s already used for an option",
                            existingNameList.get(0)));
        }
        nameList.forEach(x -> namesToValidators.put(x, validator));
        return this;
    }

    private void clear() {
        namesToArguments.clear();
        flagSet.clear();
        positionalArguments.clear();
        unrecognisedOptions.clear();
    }

    private List<String> findExistingNames(final List<String> names) {
        return names.stream().filter(namesToValidators::containsKey)
                .collect(Collectors.toList());
    }

    private Optional<NamedOptionValidator> getValidatorFromName(final String name) {
        if (namesToValidators.containsKey(name)) {
            return Optional.of(namesToValidators.get(name));
        } else {
            return Optional.empty();
        }
    }
}

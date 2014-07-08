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
        final boolean isLegal = namesToArguments.entrySet().stream()
                .map(e -> e.getValue().stream()
                        .allMatch(arg -> namesToValidators.get(e.getKey())
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
        return namesToArguments.entrySet().stream()
                .flatMap(e -> e.getValue().stream()
                        .filter((String arg) -> !namesToValidators.get(
                                e.getKey()).isArgumentLegal(arg))
                        .map(arg -> new OptionValuePair(e.getKey(), arg)))
                .collect(Collectors.toList());
    }

    public Optional<String> getArgument(final String option) {
        return Optional.ofNullable(
                namesToArguments.get(option)).map(x -> x.get(0));
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

    public ArgumentParser addPositionalOption(final String description) {
        return addPositionalOption(x -> true, description);
    }

    public ArgumentParser addPositionalOption(
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

    private Optional<NamedOptionValidator> getValidatorFromName(final String name) {
        return Optional.ofNullable(namesToValidators.get(name));
    }

    private void addNameArgumentEntry(final String name,
            final String argument) {
        if (!namesToArguments.containsKey(name)) {
            namesToArguments.put(name, new ArrayList<>());
        }
        namesToArguments.get(name).add(argument);
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

    private Optional<String> resolveName(final String name) {
        return getValidatorFromName(name).map(NamedOptionValidator::getName);
    }

    private List<String> findExistingNames(final List<String> names) {
        return names.stream().filter(namesToValidators::containsKey)
                .collect(Collectors.toList());
    }
}

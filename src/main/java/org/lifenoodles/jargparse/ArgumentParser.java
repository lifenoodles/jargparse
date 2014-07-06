package org.lifenoodles.jargparse;

import sun.security.pkcs11.wrapper.Functions;

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
    private final Map<String, OptionValidator> validatorMap;
    private final Map<String, String> argumentMap;

    public ArgumentParser() {
        validatorMap = new HashMap<>();
        argumentMap = new HashMap<>();
    }


    /**
     * Determines whether or not the arguments that have been parsed  so far are
     * well formed. An unused ArgumentParser will always return true
     * @return boolean indicating if parsed args are well formed
     */
    public boolean isWellFormed() {
        return argumentMap.keySet().stream().allMatch(k ->
            validatorMap.get(k).isArgumentLegal(argumentMap.get(k)));
    }

    public ArgumentParser addFlagOption(final String description,
                                          final String name,
                                          final String ... names) {
        final FlagOptionValidator validator = new FlagOptionValidator(
                description, name, names);
        return addOption(validator, name, names);
    }

    public ArgumentParser addStringOption(final String description,
                                          final String name,
                                          final String ... names) {
        final StringOptionValidator validator = new StringOptionValidator(
                x -> true, description, name, names);
        return addOption(validator, name, names);
    }

    public ArgumentParser addPredicateOption(final Predicate<String> predicate,
                                             final String description,
                                             final String name,
                                             final String ... names) {
        final StringOptionValidator validator = new StringOptionValidator(
                predicate, description, name, names);
        return addOption(validator, name, names);
    }

    /**
     * Parse the provided arguments using any rules that have been registered
     * @param arguments the array of arguments
     * @return this
     */
    public ArgumentParser parse(final String ... arguments)
            throws ParseException {
        if (arguments.length == 0) {
            throw new ParseException("Unable to correctly parse the arguments");
        }

        return this;
    }

    private List<String> findExistingNames(final List<String> names) {
        return names.stream().filter(validatorMap::containsKey)
                .collect(Collectors.toList());
    }

    private ArgumentParser addOption(final OptionValidator validator,
                                     final String name,
                                     final String ... names) {
        final List<String> nameList = Arrays.asList(names);
        nameList.add(name);
        final List<String> existingNameList = findExistingNames(nameList);
        if (existingNameList.size() > 0) {
            throw new IllegalArgumentException(
                    String.format("Name: %s already used for an option",
                            existingNameList.get(0)));
        }
        nameList.forEach(x -> validatorMap.put(x, validator));
        return this;
    }
}

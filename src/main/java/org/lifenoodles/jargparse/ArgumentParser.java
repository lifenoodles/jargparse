package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;
import org.lifenoodles.jargparse.exceptions.UnknownOptionException;

import java.util.*;

/**
 * Parses an array of strings looking for specified patterns,
 * contains methods to register options as well as validate input
 *
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */

public class ArgumentParser {
    private final Map<String, OptionValidator> optionalValidators =
            new HashMap<String, OptionValidator>();
    private final List<PositionalValidator> positionalValidators =
            new ArrayList<PositionalValidator>();

    public ArgumentParser addOption(final OptionValidator validator) {
        final Set<String> names = new HashSet<>(validator.getNames());
        final Set<String> duplicateNames = new HashSet<>(names);
        duplicateNames.retainAll(optionalValidators.keySet());
        if (duplicateNames.size() > 0) {
            throw new IllegalArgumentException(
                    String.format("Name: %s already used for an option",
                            duplicateNames.stream().findAny().get()));
        }
        names.forEach(x -> optionalValidators.put(x, validator));
        return this;
    }

    public ArgumentParser addOption(final PositionalValidator validator) {
        addOption((OptionValidator) validator);
        positionalValidators.add(validator);
        return this;
    }

    /**
     * Parse the provided arguments using any rules that have been registered
     *
     * @param options the array of arguments
     * @return this
     */
    public OptionSet parse(final String ... options) throws
            ArgumentCountException, UnknownOptionException,
            BadArgumentException {
        OptionSet optionSet = new OptionSet();
        List<String> optionList = Arrays.asList(options);
        while (optionList.size() > 0) {
            final String optionName = optionList.get(0);
            if (Utility.isOption(optionName)) {
                if (!optionalValidators.containsKey(optionName)) {
                    throw new UnknownOptionException(optionName);
                }
                final OptionValidator validator =
                        optionalValidators.get(optionName);
                optionSet = addToOptionSet(validator, optionList, optionSet);
                optionList = validator.restOfArguments(optionList);
            } else {
                break;
            }
        }

        for (PositionalValidator validator : positionalValidators) {
            optionSet = addToOptionSet(validator, optionList, optionSet);
            optionList = validator.restOfArguments(optionList);
        }
        return optionSet;
    }

    private OptionSet addToOptionSet(final OptionValidator validator,
            final List<String> optionList, OptionSet optionSet) throws
            ArgumentCountException, BadArgumentException {
        if (!validator.isArgumentCountCorrect(optionList)) {
            throw new ArgumentCountException(validator.getName(),
                validator.expectedOptionCount(),
                validator.extractArguments(optionList).size());
        }
        if (!validator.isArgumentListLegal(optionList)) {
            throw new BadArgumentException(validator.getName(),
                    validator.getBadArguments(optionList).get(0));
        }
        optionSet.addOption(validator,
                validator.extractArguments(optionList));
        return optionSet;
    }

    private Optional<String> resolveName(final String name) {
        return Optional.ofNullable(optionalValidators.get(name))
                .map(OptionValidator::getName);
    }

    private Optional<OptionValidator> findValidator(String name) {
        return resolveName(name).map(optionalValidators::get);
    }
}

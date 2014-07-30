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

    private final Map<String, OptionValidator> optionalValidators;

    public ArgumentParser() {
        optionalValidators = new HashMap<>();
    }

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

/*    public ArgumentParser addOption(final PositionalOptionValidator validator) {
        // if this validator contains optional parameters, make sure it's legal
        if ((validator.nOptionalArguments() ||
                validator.getOptionalArgumentCount() > 0) &&
                positionalValidators.stream().anyMatch(
                        x -> x.nOptionalArguments() ||
                                x.getOptionalArgumentCount() > 0)) {
            throw new IllegalArgumentException("Only the last positional" +
                    "option may contain optional parameters");
        }
        positionalValidators.add(validator);
        return this;
    }*/

    /**
     * Parse the provided arguments using any rules that have been registered
     *
     * @param options the array of arguments
     * @return this
     */
    public OptionSet parse(final String ... options) throws
            ArgumentCountException, UnknownOptionException, BadArgumentException {
//        int index = 0;
//        // handling optional arguments
//        final OptionSet optionSet = new OptionSet();
//        while (index < options.length) {
//            if (!options[index].startsWith("-")) {
//                break;
//            }
//            final String opt = options[index++];
//            if (!findValidator(opt).isPresent()) {
//                throw new UnknownOptionException(opt);
//            }
//            final OptionValidator validator = findValidator(opt).get();
//            final List<String> arguments = new ArrayList<>();
//            for (int i = 0; i < validator.getFixedCount(); ++i) {
//                if (index >= options.length) {
//                    throw new ArgumentCountException(opt,
//                            validator.getFixedCount(), i + 1);
//                }
//                final String nextArg = options[index++];
//                if (!validator.isArgumentLegal(nextArg)) {
//                    throw new BadArgumentException(opt, nextArg);
//                }
//                arguments.add(nextArg);
//            }
//            optionSet.addOption(validator, arguments);
//        }

        // handling positional arguments
//        for (PositionalOptionValidator validator : positionalValidators) {
//            final List<String> arguments = new ArrayList<>();
//            for (int i = 0; i < validator.getFixedCount(); ++i) {
//                if (index >= options.length) {
//                    throw new ArgumentCountException(validator.getName(),
//                            validator.getFixedCount(), i + 1);
//                }
//                final String nextArg = options[index++];
//                if (!validator.isArgumentLegal(nextArg)) {
//                    throw new BadArgumentException(validator.getName(),
//                            nextArg);
//                }
//                arguments.add(nextArg);
//            }
//            optionSet.addOption(validator, arguments);
//        }
//
//        if (index >= options.length) {
//            throw new ArgumentCountException("none", 0, options.length - index);
//        }
//        return optionSet;
        return null;
    }

    private Optional<String> resolveName(final String name) {
        return Optional.ofNullable(optionalValidators.get(name))
                .map(OptionValidator::getName);
    }

    private Optional<OptionValidator> findValidator(String name) {
        return resolveName(name).map(optionalValidators::get);
    }
}

package org.lifenoodles.jargparse;

import javax.swing.text.Position;
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
    private final List<PositionalOptionValidator> positionalValidators;

    public ArgumentParser() {
        optionalValidators = new HashMap<>();
        positionalValidators = new ArrayList<>();
        addOption(Option.optional("-h").alias("--help")
                .description("Display this message").make());
    }

    public ArgumentParser addOption(final OptionValidator validator) {
        final Set<String> names = new HashSet<>(validator.getNames());
        names.retainAll(optionalValidators.keySet());
        if (names.size() > 0) {
            throw new IllegalArgumentException(
                    String.format("Name: %s already used for an option",
                            names.stream().findAny().get()));
        }
        names.forEach(x -> optionalValidators.put(x, validator));
        return this;
    }

    public ArgumentParser addOption(final PositionalOptionValidator validator) {
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
    }

    /**
     * Parse the provided arguments using any rules that have been registered
     *
     * @param options the array of arguments
     * @return this
     */
    public OptionSet parse(final String ... options) {
        final Iterator<String> it = Arrays.asList(options).iterator();
        // handling optional arguments
        final OptionSet optionSet = new OptionSet();
        while (it.hasNext()) {
            final String arg = it.next();
            if (!arg.startsWith("-")) {
                break;
            }
            if (!findValidator(arg).isPresent()) {
                handleUnrecognised(arg);
            }
            final OptionValidator validator = findValidator(arg).get();
            final List<String> arguments = new ArrayList<>();
            for (int i = 0; i < validator.getArgumentCount(); ++i) {
                if (!it.hasNext()) {
                    handleIncorrectCount(arg, validator.getArgumentCount(),
                            i + 1);
                }
                final String nextArg = it.next();
                arguments.add(nextArg);
            }
            optionSet.addOption(validator, arguments);
        }

        // handling positional arguments
        for (PositionalOptionValidator validator : positionalValidators) {
            final List<String> arguments = new ArrayList<>();
            for (int i = 0; i < validator.getArgumentCount(); ++i) {
                if (!it.hasNext()) {
                    handleIncorrectPositionalCount(
                            validator.getArgumentCount(), i + 1);
                }
                final String nextArg = it.next();
                arguments.add(nextArg);
            }
            optionSet.addPositionalArgument(validator, arguments);
        }

        return optionSet;
    }

    private void handleIncorrectPositionalCount(final int argumentsExpected,
            final int argumentsSeen) {
        System.out.printf("Application expects %d arguments, but only saw " +
                        "%d, try --help for more info\n",
            argumentsExpected, argumentsSeen);
        System.exit(1);
    }

    private void handleIncorrectCount(final String arg,
            final int argumentsExpected, final int argumentsSeen) {
        System.out.printf("%s expects %d parameters, but only saw %d, " +
                "try --help for more info\n",
            arg, argumentsExpected, argumentsSeen);
        System.exit(1);
    }

    private void handleUnrecognised(String arg) {
        System.out.printf("Unrecognised option: %s, " +
                "try --help for more info\n", arg);
        System.exit(1);
    }

    private Optional<String> resolveName(final String name) {
        return Optional.ofNullable(optionalValidators.get(name))
                .map(OptionValidator::getName);
    }

    private Optional<OptionValidator> findValidator(String name) {
        return resolveName(name).map(optionalValidators::get);
    }
}

package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;
import org.lifenoodles.jargparse.exceptions.UnknownOptionException;

import java.util.*;

/**
 * Parses an array of strings looking for specified patterns, contains methods
 * to register options as well as validate input. If application name is not
 * set it defaults to "AppName". If prefixes are not set the only recognised
 * prefixes are "-" and "--"
 *
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class ArgumentParser {
    private final Map<String, OptionalValidator> optionalValidators =
            new HashMap<>();
    private final List<PositionalValidator> positionalValidators =
            new ArrayList<>();
    private final List<String> optionPrefixes = new ArrayList<>();
    private String applicationName = "AppName";

    public ArgumentParser() {
        this.optionPrefixes.add("-");
        this.optionPrefixes.add("--");
    }

    /**
     * Gets a formatted help message for use as a standard response in the
     * event of bad inputs. The HelpfulArgumentParser invokes this automatically
     *
     * @return formatted help text
     */
    public String getHelpText() {
        StringBuilder builder = new StringBuilder(getUsageMessage());
        builder.append(System.lineSeparator()).append(System.lineSeparator());
        builder.append("positional arguments:").append(System.lineSeparator());
        for (PositionalValidator validator : positionalValidators) {
            builder.append(" ").append(validator.getName());
            builder.append(System.lineSeparator()).append("\t")
                    .append(validator.getDescription())
                    .append(System.lineSeparator());
        }
        builder.append(System.lineSeparator()).append("optional arguments:")
                .append(System.lineSeparator());
        for (OptionValidator validator :
                new HashSet<>(optionalValidators.values())) {
            builder.append(" ").append(validator.getName());
            builder.append(" ").append(validator.formatLabels());
            for (String alias : Utility.dropN(1, validator.getNames())) {
                builder.append(", ").append(alias);
                builder.append(" ").append(validator.formatLabels());
            }
            builder.append(System.lineSeparator()).append("\t")
                    .append(validator.getDescription())
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    /**
     * Gets a formatted string as a standard usage message.
     *
     * @return a formatted usage message
     */
    public String getUsageMessage() {
        StringBuilder builder = new StringBuilder("usage: ").
                append(applicationName);
        for (OptionValidator validator :
                new HashSet<>(optionalValidators.values())) {
            builder.append(" [").append(validator.helpFormat()).append("]");
        }
        for (PositionalValidator validator : positionalValidators) {
            builder.append(" ").append(validator.helpFormat());
        }
        return builder.toString();
    }

    /**
     * Sets the application name, used for generating usage message
     *
     * @param applicationName the name to use in the usage message
     */
    public void setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * Sets the prefixes recognised by this parser, defaults if this method is
     * not called are "-" and "--"
     *
     * @param optionPrefixes array of prefixes
     */
    public void setPrefixes(String... optionPrefixes) {
        this.optionPrefixes.clear();
        this.optionPrefixes.addAll(Arrays.asList(optionPrefixes));
    }

    /**
     * Register an option with this parser
     *
     * @param optional an optional maker from the optional() method
     * @return this
     */
    public ArgumentParser addOption(final OptionalMaker optional) {
        return addOption(optional.make());
    }

    /**
     * Register a positional argument with this parser
     *
     * @param positional a positional maker from the positional() method
     * @return this
     */
    public ArgumentParser addOption(final PositionalMaker positional) {

        return addOption(positional.make());
    }

    /**
     * Gets a maker object for constructing an optional argument validator, this
     * maker should be passed to the parser it was constructed from
     *
     * @param name the name of the option
     * @return the maker object
     */
    public OptionalMaker optional(String name) {
        return new OptionalMaker(name, optionPrefixes);
    }

    /**
     * Parse the provided arguments using any rules that have been registered
     *
     * @param options the array of arguments
     * @return an OptionSet containing all of the parsed methods
     * @throws ArgumentCountException if the count of arguments is incorrect
     * @throws UnknownOptionException if there exist unknown options
     * @throws BadArgumentException if any arguments to an option are illegal
     */
    public OptionSet parse(final String... options) throws
            ArgumentCountException,
            UnknownOptionException,
            BadArgumentException {
        StateParser parser = new StateParser(optionalValidators,
                positionalValidators, options);
        while (!parser.isDone()) {
            parser.execute();
        }
        return parser.optionSet;
    }

    /**
     * Gets a maker object for constructing a positional argument validator,
     * this maker should be passed to the parser it was constructed from
     *
     * @param name the name of the argument
     * @return the maker object
     */
    public PositionalMaker positional(String name) {
        return new PositionalMaker(name, optionPrefixes);
    }

    /**
     * Add option to the correct data structures for its type.
     *
     * @param validator the validator to add
     * @return this
     * @throws IllegalArgumentException if the option name is already in use
     */
    private ArgumentParser addOption(final OptionalValidator validator) {
        if (validator.getNames().stream().filter(x -> !isOption(x))
                .count() > 0) {
            throw new IllegalArgumentException(String.format(
                    "Illegal name: %s, optional arguments must begin with a " +
                            "prefix string",
                    validator.getNames().stream().filter(x -> !isOption(x))
                            .findAny()));
        }
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

    /**
     * Add option to the correct data structures for its type.
     *
     * @param validator the validator to add
     * @return this
     * @throws IllegalArgumentException if the option name is already in use
     */
    private ArgumentParser addOption(final PositionalValidator validator) {
        if (isOption(validator.getName())) {
            throw new IllegalArgumentException(String.format(
                    "Illegal name: %s, positional argument begins with a " +
                            "prefix string", validator.getName()));
        }
        if (positionalValidators.stream()
                .anyMatch(x -> x.getName().equals(validator.getName()))) {
            throw new IllegalArgumentException(
                    String.format("Name: %s already used for an option",
                            validator.getName()));
        }
        positionalValidators.add(validator);
        return this;
    }

    /**
     * determine if the given String is a legal optional name
     *
     * @param option name of the option
     * @return true if the name is a legal option name
     */
    protected boolean isOption(String option) {
        return optionPrefixes.stream().anyMatch(option::startsWith);
    }

    private class StateParser {
        public final OptionSet optionSet = new OptionSet();
        private final Map<String, OptionalValidator> optionalValidators;
        private final List<PositionalValidator> positionalValidators;
        private final Iterator<PositionalValidator> positionalIterator;
        private String optionName;
        private State currentState = State.READ_OPTION;
        private OptionValidator validator;
        private List<String> arguments;
        private List<String> parsedArguments = new ArrayList<>();

        public StateParser(Map<String, OptionalValidator> optionalValidators,
                List<PositionalValidator> positionalValidators,
                String ... arguments) {
            this.optionalValidators = new HashMap<>(optionalValidators);
            this.positionalValidators = new ArrayList<>(positionalValidators);
            this.positionalIterator = positionalValidators.iterator();
            this.arguments = new LinkedList<>(Arrays.asList(arguments));
        }

        public boolean isDone() {
            return currentState == State.DONE;
        }

        public void execute() throws UnknownOptionException,
                ArgumentCountException, BadArgumentException {
            switch (currentState) {
                case READ_OPTION: readOption(); break;
                case READ_ARGUMENT: readArgument(); break;
                case DONE: break;
            }
        }

        private void readOption() throws UnknownOptionException,
                ArgumentCountException {
            if (arguments.isEmpty()) {
                currentState = State.DONE;
                return;
            }
            if (ArgumentParser.this.isOption(arguments.get(0))) {
                if (!optionalValidators.containsKey(arguments.get(0))) {
                    throw new UnknownOptionException(arguments.get(0));
                }
                validator = ArgumentParser.this.optionalValidators
                        .get(arguments.get(0));
                optionName = arguments.get(0);
                arguments.remove(0);
            } else {
                if (!positionalIterator.hasNext()) {
                    throw new UnknownOptionException(arguments.get(0));
                }
                validator = positionalIterator.next();
                optionName = validator.getName();
            }
            currentState = State.READ_ARGUMENT;
            parsedArguments = new ArrayList<>();
        }

        private void readArgument() throws ArgumentCountException,
                BadArgumentException {
            if (arguments.isEmpty() ||
                    ArgumentParser.this.isOption(arguments.get(0)) ||
                    parsedArguments.size() >=
                            validator.maximumArgumentCount()) {
                if (parsedArguments.size() < validator.minimumArgumentCount()) {
                    throw new ArgumentCountException(optionName,
                            validator.minimumArgumentCount(),
                            parsedArguments.size());
                }
                optionSet.addOption(validator, parsedArguments);
                currentState = State.READ_OPTION;
            } else {
                if (!validator.isArgumentLegal(arguments.get(0))) {
                    throw new BadArgumentException(optionName,
                            arguments.get(0));
                }
                parsedArguments.add(arguments.get(0));
                arguments.remove(0);
            }
        }
    }

    private enum State {
        READ_OPTION, READ_ARGUMENT, DONE
    }
}

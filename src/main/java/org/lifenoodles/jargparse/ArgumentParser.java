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
        OptionSet optionSet = new OptionSet();
        List<String> positionalArguments = new ArrayList<>();
        HashMap<OptionalValidator, List<String>> optionalArguments
                = new HashMap<>();
        for (int i = 0; i < options.length; ++i) {
            final String arg = options[i];
            if (optionalValidators.containsKey(arg)) {
                List<String> arguments = new ArrayList<>();
                for (; i < options.length; ++i) {
                    if (!isOption(options[i])) {
                        arguments.add(options[i]);
                    } else {
                        break;
                    }
                }
                optionalArguments.put(optionalValidators.get(arg), arguments);
            }
        }

        return optionSet;
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
     * Insert the validator and extracted arguments into the given OptionSet
     *
     * @param validator the validator to add
     * @param arguments the list of arguments to consider
     * @param optionSet the optionSet to insert into
     * @return the modified optionSet
     * @throws ArgumentCountException if the argument count is incorrect
     * @throws BadArgumentException if any arguments do not match the validator
     */
    private OptionSet addToOptionSet(final OptionValidator validator,
            final List<String> arguments, OptionSet optionSet) throws
            ArgumentCountException, BadArgumentException {
        if (!validator.isArgumentCountCorrect(arguments)) {
            throw new ArgumentCountException(validator.getName(),
                    validator.argumentCount(),
                    validator.extractArguments(arguments).size());
        }
        if (!validator.isArgumentListLegal(arguments)) {
            throw new BadArgumentException(validator.getName(),
                    validator.getBadArguments(arguments).get(0));
        }
        optionSet.addOption(validator,
                validator.extractArguments(arguments));
        return optionSet;
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
}

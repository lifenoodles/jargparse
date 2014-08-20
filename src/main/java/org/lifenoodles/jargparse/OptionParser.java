package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;
import org.lifenoodles.jargparse.exceptions.RequiredOptionException;
import org.lifenoodles.jargparse.exceptions.UnknownOptionException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Parses an array of strings looking for specified patterns, contains methods
 * to register options as well as validate input. If application name is not
 * set it defaults to "AppName". If prefixes are not set the only recognised
 * prefixes are "-" and "--"
 *
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionParser {
    private final Map<String, OptionValidator> namesToValidators =
            new HashMap<>();
    private final List<OptionValidator> positionalValidators =
            new ArrayList<>();
    private final Set<OptionValidator> requiredValidators = new HashSet<>();
    private final Set<OptionValidator> helpValidators = new HashSet<>();
    private final List<String> optionPrefixes = new ArrayList<>();
    private String applicationName = "AppName";

    public OptionParser() {
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
        StringBuilder builder = new StringBuilder(getUsageText());
        builder.append(System.lineSeparator()).append(System.lineSeparator());
        builder.append("positional arguments:").append(System.lineSeparator());
        for (OptionValidator validator : positionalValidators) {
            builder.append(" ").append(validator.getName());
            builder.append(System.lineSeparator()).append("\t")
                    .append(validator.getDescription())
                    .append(System.lineSeparator());
        }
        builder.append(System.lineSeparator()).append("optional arguments:")
                .append(System.lineSeparator());
        for (OptionValidator validator :
                new HashSet<>(namesToValidators.values())) {
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
    public String getUsageText() {
        StringBuilder builder = new StringBuilder("usage: ").
                append(applicationName);
        for (OptionValidator validator :
                new HashSet<>(namesToValidators.values())) {
            builder.append(" [").append(validator.formatHelp()).append("]");
        }
        for (OptionValidator validator : positionalValidators) {
            builder.append(" ").append(validator.formatHelp());
        }
        return builder.toString();
    }

    /**
     * Sets the application name, used for generating usage message
     *
     * @param applicationName the name to use in the usage message
     */
    public OptionParser setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    /**
     * Sets the prefixes recognised by this parser, defaults if this method is
     * not called are "-" and "--"
     *
     * @param optionPrefixes array of prefixes
     */
    public OptionParser setPrefixes(String... optionPrefixes) {
        this.optionPrefixes.clear();
        this.optionPrefixes.addAll(Arrays.asList(optionPrefixes));
        return this;
    }

    public OptionParser addOption(Option maker) {
        OptionValidator validator = maker.make();
        if (validator.getNames().stream().allMatch(this::isOption)) {
            registerValidator(validator);
        } else if (validator.getNames().stream().noneMatch(this::isOption)) {
            if (validator.maximumArgumentCount() == 0) {
                throw new IllegalArgumentException(String.format(
                        "Positional option: %s must allow more than 0 " +
                                "arguments", validator.getName()));
            }
            if (validator.minimumArgumentCount() > 0) {
                validator = maker.required().make();
            }
            registerValidator(validator);
            positionalValidators.add(validator);
        } else {
            throw new IllegalArgumentException(String.format(
                    "All or none of the names of this argument must begin " +
                            "with one of the following: [ %s ]",
                    optionPrefixes.stream().collect(Collectors.joining(","))
            ));
        }
        if (validator.isRequired()) {
            requiredValidators.add(validator);
        }
        if (validator.isHelp()) {
            helpValidators.add(validator);
        }
        return this;
    }

    /**
     * Parse the provided arguments using any rules that have been registered
     *
     * @param options the array of arguments
     * @return an OptionSet containing all of the parsed methods
     * @throws ArgumentCountException if the count of arguments is incorrect
     * @throws UnknownOptionException if there exist unknown options
     * @throws BadArgumentException   if any arguments to an option are illegal
     */
    public OptionSet parse(final String... options) throws
            ArgumentCountException,
            UnknownOptionException,
            BadArgumentException,
            RequiredOptionException {
        StateParser parser = new StateParser(namesToValidators,
                positionalValidators, options);
        while (!parser.isDone()) {
            parser.execute();
        }
        // check parser results for sanity
        if (!parser.badArguments.isEmpty()) {
            final String optionName = parser.badArguments.keySet().stream()
                    .findFirst().get();
            throw new BadArgumentException(optionName,
                    parser.badArguments.get(optionName).get(0));
        }
        if (!parser.unrecognisedOptions.isEmpty()) {
            throw new UnknownOptionException(parser.unrecognisedOptions
                    .iterator().next());
        }
        List<String> badArgumentCounts =
                parser.namesToArgumentCounts.entrySet().stream()
                        .filter(x -> namesToValidators.get(x.getKey())
                                .minimumArgumentCount() > x.getValue())
                        .map(Map.Entry::getKey).collect(Collectors.toList());
        if (!badArgumentCounts.isEmpty()) {
            OptionValidator validator = namesToValidators.get(badArgumentCounts
                    .get(0));
            throw new ArgumentCountException(badArgumentCounts.get(0),
                    validator.minimumArgumentCount(),
                    parser.namesToArgumentCounts.get(badArgumentCounts.get(0)));
        }

        // check required options

        return parser.optionSet;
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

    /**
     * Add option to this parser
     *
     * @param validator the validator to add
     * @return this
     * @throws IllegalArgumentException if the option name is already in use
     */
    private OptionParser registerValidator(final OptionValidator validator) {
        final Set<String> names = new HashSet<>(validator.getNames());
        final Set<String> duplicateNames = new HashSet<>(names);
        duplicateNames.retainAll(namesToValidators.keySet());
        if (!duplicateNames.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Name: %s already used for an option",
                            duplicateNames.stream().findAny().get())
            );
        }
        namesToValidators.putAll(names.stream()
                .collect(Collectors.toMap(x -> x, x -> validator)));
        return this;
    }

    /**
     * Represents the state of the StateParser
     */
    private enum State {
        READ_OPTION, READ_ARGUMENT, DONE
    }

    /**
     * Implementation of a FSM to parse the given arguments
     */
    private class StateParser {
        // public data for reporting parser state
        public final OptionSet optionSet = new OptionSet();
        public final Set<String> unrecognisedOptions = new HashSet<>();
        public final Map<String, List<String>> badArguments = new HashMap<>();
        public final Map<String, Integer> namesToArgumentCounts
                = new HashMap<>();

        // data passed to parser
        private final Map<String, OptionValidator> namesToValidators;

        // state variables
        private final Iterator<OptionValidator> positionalIterator;
        private String optionName;
        private State currentState = State.READ_OPTION;
        private OptionValidator validator;
        private List<String> arguments;
        private List<String> parsedArguments = new ArrayList<>();

        public StateParser(final Map<String, OptionValidator> namesToValidators,
                final List<OptionValidator> positionalValidators,
                final String... arguments) {
            this.namesToValidators =
                    Collections.unmodifiableMap(namesToValidators);
            this.positionalIterator = positionalValidators.iterator();
            this.arguments = new LinkedList<>(Arrays.asList(arguments));
        }

        public boolean isDone() {
            return currentState == State.DONE;
        }

        public void execute() {
            switch (currentState) {
                case READ_OPTION:
                    readOption();
                    break;
                case READ_ARGUMENT:
                    readArgument();
                    break;
                case DONE:
                    break;
            }
        }

        private void readOption() {
            if (arguments.isEmpty()) {
                currentState = State.DONE;
                return;
            }
            if (OptionParser.this.isOption(arguments.get(0))) {
                if (!namesToValidators.containsKey(arguments.get(0))) {
                    unrecognisedOptions.add(arguments.get(0));
                    arguments.remove(0);
                    return;
                }
                validator = OptionParser.this.namesToValidators
                        .get(arguments.get(0));
                optionName = arguments.get(0);
                arguments.remove(0);
            } else {
                if (!positionalIterator.hasNext()) {
                    unrecognisedOptions.add(arguments.get(0));
                    arguments.remove(0);
                    return;
                }
                validator = positionalIterator.next();
                optionName = validator.getName();
            }
            currentState = State.READ_ARGUMENT;
            parsedArguments = new ArrayList<>();
        }

        private void readArgument() {
            assert (validator != null);
            if (arguments.isEmpty() ||
                    OptionParser.this.isOption(arguments.get(0)) ||
                    parsedArguments.size() >=
                            validator.maximumArgumentCount()) {
                namesToArgumentCounts.put(optionName, parsedArguments.size());
                optionSet.addOption(validator, parsedArguments);
                currentState = State.READ_OPTION;
            } else {
                if (!validator.isArgumentLegal(arguments.get(0))) {
                    if (!badArguments.containsKey(optionName)) {
                        badArguments.put(optionName, new ArrayList<>());
                    }
                    badArguments.get(optionName).add(arguments.get(0));
                    arguments.remove(0);
                    return;
                }
                parsedArguments.add(arguments.get(0));
                arguments.remove(0);
            }
        }
    }
}

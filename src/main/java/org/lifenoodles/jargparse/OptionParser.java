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
    private final Map<String, Validator> validators = new HashMap<>();
    private final Map<String, OptionValidator> optionValidators =
            new HashMap<>();
    private final List<PositionalValidator> positionalValidators =
            new ArrayList<>();
    private final String applicationName;

    public OptionParser() {
        this("AppName");
    }

    public OptionParser(final String applicationName) {
        this.applicationName = applicationName;
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
        for (Validator validator : positionalValidators) {
            builder.append(" ").append(validator.getName());
            builder.append(System.lineSeparator()).append("\t")
                    .append(validator.getDescription())
                    .append(System.lineSeparator());
        }
        builder.append(System.lineSeparator()).append("optional arguments:")
                .append(System.lineSeparator());
        for (Validator validator :
                new HashSet<>(optionValidators.values())) {
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
        for (Validator validator :
                new HashSet<>(optionValidators.values())) {
            builder.append(" [").append(validator.formatHelp()).append("]");
        }
        for (Validator validator : positionalValidators) {
            builder.append(" ").append(validator.formatHelp());
        }
        return builder.toString();
    }

    public OptionParser addOption(Option option) {
        OptionValidator validator = option.make();
        registerValidator(validator);
        validator.getNames().stream()
                .forEach(x -> optionValidators.put(x, validator));
        return this;
    }

    public OptionParser addOption(Positional option) {
        PositionalValidator validator = option.make();
        registerValidator(validator);
        if (validator.maximumArgumentCount() == 0) {
            throw new IllegalArgumentException(String.format(
                    "Positional option: %s must take at least 1 argument",
                    validator.getName()));
        }
        positionalValidators.add(validator);
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
        StateParser parser = new StateParser(positionalValidators,
                optionValidators, options);
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

        List<String> badArgumentCounts = parser.namesToArgumentCounts.entrySet()
                .stream().filter(x -> validators.get(x.getKey())
                        .minimumArgumentCount() > x.getValue())
                .map(Map.Entry::getKey).collect(Collectors.toList());
        // check for bad arguments
        if (!badArgumentCounts.isEmpty()) {
            final Validator v = optionValidators.get(badArgumentCounts.get(0));
            throw new ArgumentCountException(badArgumentCounts.get(0),
                    v.minimumArgumentCount(),
                    parser.namesToArgumentCounts.get(badArgumentCounts.get(0)));
        }
        // check required options
        if (optionValidators.values().stream()
                .filter(OptionValidator::isHelper)
                .map(OptionValidator::getName)
                .filter(parser.optionSet::contains).count() == 0) {
            Optional<OptionValidator> missing = optionValidators.values()
                    .stream().filter(OptionValidator::isRequired)
                    .filter(x -> !parser.optionSet.contains(x.getName()))
                    .findFirst();
            if (missing.isPresent()) {
                throw new RequiredOptionException(String.format(
                        "Required option %s missing", missing.get().getName()));
            }
        }
        return parser.optionSet;
    }

    private void registerValidator(Validator validator) {
        if (validator.getNames().stream().anyMatch(validators::containsKey)) {
            throw new IllegalArgumentException(String.format(
                    "name %s already registered with this parser",
                    validator.getName()));
        }
        validator.getNames().stream()
                .forEach(x -> validators.put(x, validator));
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

        // data passed to parser
        private final Map<String, Validator> namesToValidators;

        // state variables
        private final Iterator<PositionalValidator> positionalIterator;
        private final Map<String, Integer> namesToArgumentCounts
                = new HashMap<>();
        private List<String> arguments;
        private List<String> parsedArguments = new ArrayList<>();
        private String optionName;
        private State currentState = State.READ_OPTION;
        private Validator validator;

        public StateParser(final List<PositionalValidator> positionalValidators,
                final Map<String, OptionValidator> namesToValidators,
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
            if (namesToValidators.containsKey(arguments.get(0))) {
                if (!namesToValidators.containsKey(arguments.get(0))) {
                    unrecognisedOptions.add(arguments.get(0));
                    arguments.remove(0);
                    return;
                }
                validator = OptionParser.this.optionValidators
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
                    namesToValidators.containsKey(arguments.get(0)) ||
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

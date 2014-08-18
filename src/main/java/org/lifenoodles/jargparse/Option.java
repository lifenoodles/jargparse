package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Provides a fluent interface for creating validators. These validators should
 * be passed to a parser.
 *
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */
@SuppressWarnings("unchecked")
public final class Option {
    private final List<String> names = new ArrayList<>();
    private final List<String> argumentLabels = new ArrayList<>();
    private boolean required = false;
    private boolean help = false;
    private ArgumentCounter argumentCounter = new FixedCounter(0, 0);
    private String description = "";
    private Predicate<String> predicate = x -> true;

    protected Option(String name, String... aliases) {
        this.names.add(name);
        this.names.addAll(Arrays.asList(aliases));
    }

    public static Option of(final String name, final String... names) {
        return new Option(name, names);
    }

    /**
     * Set the argument count for this option
     *
     * @param argumentCount the number of arguments this option should expect
     * @param labels        the labels to use for usage messages for this option
     * @return this
     */
    public Option arguments(final int argumentCount,
            final String... labels) {
        return arguments(argumentCount, argumentCount, labels);
    }

    public Option arguments(final int lower, final int upper,
            String... labels) {
        this.argumentLabels.addAll(Arrays.asList(labels));
        this.argumentCounter = new FixedCounter(lower, upper);
        return this;
    }

    /**
     * Set the argument count for this option, recognised legal strings are:
     * "?" (Zero or one), "*" (Zero or more) and "+" (One or more)
     *
     * @param argumentCount A string that is one of: "*", "+" or "?"
     * @param labels        the labels to use for usage messages for this
     *                      option. The specifics of how these labels are
     *                      formatted or used will depend on the count. "?"
     *                      should be given 1 label, "*" should be given 1
     *                      label, "+" should be given 1 or 2 labels
     * @return this
     */
    public Option arguments(final String argumentCount,
            final String... labels) {
        this.argumentLabels.addAll(Arrays.asList(labels));
        switch (argumentCount) {
            case "?":
                argumentCounter = new ZeroOrOneCounter();
                break;
            case "+":
                argumentCounter = new OneOrMoreCounter();
                break;
            case "*":
                argumentCounter = new ZeroOrMoreCounter();
                break;
            default:
                throw new IllegalArgumentException(String.format(
                        "Unrecognised pattern string: %s", argumentCount));
        }
        return this;
    }

    /**
     * Set the description for this option, this will be used when generating
     * help text or usage text for the user
     *
     * @param description the description of this option
     * @return this
     */
    public Option description(final String description) {
        this.description = description;
        return this;
    }

    public Option helper() {
        this.help = true;
        return this;
    }

    /**
     * Give a predicate that arguments to this option must satisfy for parsing
     * to be successful. During parsing if this predicate fails, an exception
     * will be thrown with the relevant information
     *
     * @param predicate predicate to match, default is an always true function
     * @return this
     */
    public Option matches(final Predicate<String> predicate) {
        this.predicate = predicate;
        return this;
    }

    public Option matches(final String... strings) {
        return matches(Arrays.asList(strings));
    }

    public Option matches(final List<String> strings) {
        if (strings.isEmpty()) {
            return matches(x -> true);
        } else {
            return matches(strings::contains);
        }
    }

    public Option required() {
        this.required = true;
        return this;
    }

    /**
     * Instantiate the validator represented by this maker object. This method
     * must be invoked to finalise construction of the validator
     *
     * @return a new validator with the properties specified by this maker
     */
    protected OptionValidator make() {
        return new OptionValidator(names, description, argumentCounter,
                predicate, argumentLabels, required, help);
    }
}

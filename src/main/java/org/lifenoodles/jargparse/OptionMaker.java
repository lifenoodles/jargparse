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
public abstract class OptionMaker<T extends OptionMaker<T>> {
    private final String name;
    private final List<String> argumentLabels = new ArrayList<>();
    private ArgumentCounter argumentCounter = new FixedCounter(1);
    private String description = "";
    private Predicate<String> predicate = x -> true;

    protected OptionMaker(String name) {
        this.name = name;
    }

    /**
     * Set the argument count for this option
     *
     * @param argumentCount the number of arguments this option should expect
     * @param labels        the labels to use for usage messages for this option
     * @return this
     */
    public T arguments(final int argumentCount, final String... labels) {
        this.argumentLabels.addAll(Arrays.asList(labels));
        this.argumentCounter = new FixedCounter(argumentCount);
        return (T) this;
    }

    /**
     * Set the argument count for this option, recognised legal strings are:
     * "?" (Zero or one), "*" (Zero or more) and "+" (One or more)
     *
     * @param argumentCount A string that is one of: "*", "+" or "?"
     * @param labels        the labels to use for usage messages for this option.
     *                      the specifics of how these labels are formatted or used
     *                      will depend on the count. "?" should be given 1 label,
     *                      "*" should be given 1 label, "+" should be given 1 or 2
     *                      labels
     * @return this
     */
    public T arguments(final String argumentCount, final String... labels) {
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
        return (T) this;
    }

    /**
     * Set the description for this option, this will be used when generating
     * help text or usage text for the user
     *
     * @param description the description of this option
     * @return this
     */
    public T description(final String description) {
        this.description = description;
        return (T) this;
    }

    /**
     * Give a predicate that arguments to this option must satisfy for parsing
     * to be successful. During parsing if this predicate fails, an exception
     * will be thrown with the relevant information
     *
     * @param predicate predicate to match, default is an always true function
     * @return this
     */
    public T matches(final Predicate<String> predicate) {
        this.predicate = predicate;
        return (T) this;
    }

    /**
     * Instantiate the validator represented by this maker object. This method
     * must be invoked to finalise construction of the validator
     *
     * @return a new validator with the properties specified by this maker
     */
    public abstract OptionValidator make();

    protected String getName() {
        return name;
    }

    protected ArgumentCounter getArgumentCounter() {
        return argumentCounter;
    }

    protected List<String> getArgumentLabels() {
        return new ArrayList<>(argumentLabels);
    }

    protected String getDescription() {
        return description;
    }

    protected Predicate<String> getPredicate() {
        return predicate;
    }
}

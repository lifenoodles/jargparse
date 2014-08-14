package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */

@SuppressWarnings("unchecked")
abstract class OptionMaker<T extends OptionMaker<T>> {
    private final String name;
    private final List<String> argumentLabels = new ArrayList<>();
    private final List<String> optionPrefixes;
    private ArgumentCounter optionParser = new FixedCounter(1);
    private String description = "";
    private Predicate<String> predicate = x -> true;

    protected OptionMaker(String name, final List<String> optionPrefixes) {
        this.name = name;
        this.optionPrefixes = new ArrayList<>(optionPrefixes);
    }

    public T arguments(final int argumentCount, final String... labels) {
        this.argumentLabels.addAll(Arrays.asList(labels));
        this.optionParser = new FixedCounter(argumentCount);
        return (T) this;
    }

    public T arguments(final String argumentCount, final String... labels) {
        this.argumentLabels.addAll(Arrays.asList(labels));
        switch (argumentCount) {
            case "?":
                optionParser = new ZeroOrOneCounter();
                break;
            case "+":
                optionParser = new OneOrMoreCounter();
                break;
            case "*":
                optionParser = new ZeroOrMoreCounter();
                break;
            default:
                throw new IllegalArgumentException(String.format(
                        "Unrecognised pattern string: %s", argumentCount));
        }
        return (T) this;
    }

    public T description(final String description) {
        this.description = description;
        return (T) this;
    }

    public T matches(final Predicate<String> predicate) {
        this.predicate = predicate;
        return (T) this;
    }

    public abstract OptionValidator make();

    protected String getName() {
        return name;
    }

    protected ArgumentCounter getOptionParser() {
        return optionParser;
    }

    protected String getDescription() {
        return description;
    }

    protected Predicate<String> getPredicate() {
        return predicate;
    }

    protected List<String> getOptionPrefixes() {
        return new ArrayList<>(optionPrefixes);
    }

    protected List<String> getArgumentLabels() {
        return new ArrayList<>(argumentLabels);
    }
}

package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.parsers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */

@SuppressWarnings("unchecked")
public abstract class Option<T extends Option<T>> {
    private final String name;
    private OptionParser optionParser = new FixedCountParser(1);
    private String description = "";
    private Predicate<String> predicate = x -> true;
    private List<String> optionPrefixes;

    protected Option(String name, final List<String> optionPrefixes) {
        this.name = name;
        this.optionPrefixes = new ArrayList<>(optionPrefixes);
    }

    protected String getName() {
        return name;
    }

    protected OptionParser getOptionParser() {
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

    public T arguments(final int argumentCount) {
        this.optionParser = new FixedCountParser(argumentCount);
        return (T) this;
    }

    public T arguments(final String count) {
        switch (count) {
            case "?": optionParser = new ZeroOrOneParser(); break;
            case "+": optionParser = new OneOrMoreParser(); break;
            case "*": optionParser = new ZeroOrMoreParser(); break;
            default: throw new IllegalArgumentException(String.format(
                    "Unrecognised pattern string: %s", count));
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
}

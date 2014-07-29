package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 21/07/2014.
 */

public class Option {
    private final String name;
    private final List<String> aliases = new ArrayList<>();
    private int argumentCount = 1;
    private ArgumentCount count = ArgumentCount.FIXED;
    private String description = "";
    private Predicate<String> predicate = x -> true;

    private Option(String name) {
        this.name = name;
    }

    public static Option build(final String name) {
        return new Option(name);
    }

    public Option alias(final String ... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public Option arguments(final int argumentCount) {
        this.count = ArgumentCount.FIXED;
        this.argumentCount = argumentCount;
        return this;
    }

    public Option arguments(final ArgumentCount count) {
        this.count = count;
        return this;
    }

    public Option description(final String description) {
        this.description = description;
        return this;
    }

    public Option matches(final Predicate<String> predicate) {
        this.predicate = predicate;
        return this;
    }

    public OptionValidator make() {
        return new OptionValidator(description, argumentCount, predicate,
                name, aliases.toArray(new String[aliases.size()]));
    }
}

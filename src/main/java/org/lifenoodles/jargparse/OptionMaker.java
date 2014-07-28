package org.lifenoodles.jargparse;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Donagh Hatton
 *         created on 7/22/14.
 */

public class OptionMaker {
    private final String name;
    private final List<String> aliases;
    private int argumentCount = 0;
    private String description = "";
    private Predicate<String> predicate = x -> true;

    OptionMaker(final String name) {
        this.name = name;
        aliases = new LinkedList<>();
    }

    public OptionMaker alias(final String ... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public OptionMaker arguments(final int argumentCount) {
        this.argumentCount = argumentCount;
        return this;
    }

    public OptionMaker description(final String description) {
        this.description = description;
        return this;
    }

    public OptionMaker matches(final Predicate<String> predicate) {
        this.predicate = predicate;
        return this;
    }

    public OptionValidator make() {
        return new OptionValidator(description, argumentCount,
                predicate, name, aliases.toArray(new String[aliases.size()]));
    }
}

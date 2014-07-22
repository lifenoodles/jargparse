package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Donagh Hatton on 7/8/14.
 */
public class OptionMaker {
    private final String name;
    private final List<String> aliases;
    private String description = "";
    private Predicate<String> predicate = x -> true;

    OptionMaker(final String name) {
        this.name = name;
        aliases = new ArrayList<>();
    }

    public OptionMaker alias(final String ... aliases) {
        Arrays.stream(aliases).forEach(x -> this.aliases.add(x));
        return this;
    }

    public OptionMaker descripion(final String description) {
        this.description = description;
        return this;
    }

    public OptionMaker matches(final Predicate<String> predicate) {
        this.predicate = predicate;
        return this;
    }

    public StringOptionValidator make() {
        return new StringOptionValidator(predicate, description, name,
                (String[]) aliases.stream().toArray());
    }
}

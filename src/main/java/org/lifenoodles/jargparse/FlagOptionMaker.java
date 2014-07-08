package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Donagh Hatton on 7/8/14.
 */
public class FlagOptionMaker {
    private final String name;
    private final List<String> aliases;
    private String description;

    FlagOptionMaker(final String name) {
        this.name = name;
        aliases = new ArrayList<>();
    }

    public FlagOptionMaker alias(final String ... aliases) {
        Arrays.stream(aliases).forEach(x -> this.aliases.add(x));
        return this;
    }

    public FlagOptionMaker descripion(final String description) {
        this.description = description;
        return this;
    }

    public FlagOptionValidator make() {
        return new FlagOptionValidator(description, name,
                (String[]) aliases.stream().toArray());
    }
}

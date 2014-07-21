package org.lifenoodles.jargparse;

import java.util.function.Predicate;

/**
 * Created by Donagh Hatton on 7/8/14.
 */
public class PositionalOptionMaker {
    private final String name;
    private String description = "";
    private Predicate<String> predicate = x -> true;


    PositionalOptionMaker(final String name) {
        this.name = name;
    }

    public PositionalOptionMaker description(final String description) {
        this.description = description;
        return this;
    }

    public PositionalOptionMaker matching(final Predicate<String> predicate) {
        this.predicate = predicate;
        return this;
    }

    public PositionalOptionValidator make() {
        return new PositionalOptionValidator(predicate, description, name);
    }
}

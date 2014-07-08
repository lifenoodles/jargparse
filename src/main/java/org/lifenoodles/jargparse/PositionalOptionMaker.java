package org.lifenoodles.jargparse;

import java.util.function.Predicate;

/**
 * Created by Donagh Hatton on 7/8/14.
 */
public class PositionalOptionMaker {
    private final String name;
    private final int position;
    private String description = "";
    private Predicate<String> predicate = x -> true;


    PositionalOptionMaker(final String name, final int position) {
        this.name = name;
        this.position = position;
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
        return new PositionalOptionValidator(position, predicate, description, name);
    }
}

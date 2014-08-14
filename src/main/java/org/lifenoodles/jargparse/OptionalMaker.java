package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public class OptionalMaker extends OptionMaker<OptionalMaker> {
    private final List<String> aliases = new ArrayList<>();

    protected OptionalMaker(final String name,
            final List<String> optionPrefixes) {
        super(name, optionPrefixes);
    }

    public OptionalMaker alias(final String... names) {
        aliases.addAll(Arrays.asList(names));
        return this;
    }

    @Override
    public OptionalValidator make() {
        List<String> names = new ArrayList<>(aliases);
        names.add(0, getName());
        return new OptionalValidator(names, getDescription(), getOptionParser(),
                getPredicate(), getOptionPrefixes(), getArgumentLabels());
    }
}

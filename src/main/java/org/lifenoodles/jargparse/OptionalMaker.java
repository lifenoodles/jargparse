package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public class OptionalMaker extends Option<OptionalMaker> {
    private final List<String> aliases = new ArrayList<String>();

    protected OptionalMaker(final String name) {
        super(name);
    }

    public OptionalMaker alias(final String ... names) {
        aliases.addAll(Arrays.asList(names));
        return this;
    }

    @Override
    public OptionValidator make() {
        List<String> names = new ArrayList<String>(aliases);
        names.add(0, getName());
        return new OptionValidator(names, getDescription(), getOptionParser(),
                getPredicate());
    }
}

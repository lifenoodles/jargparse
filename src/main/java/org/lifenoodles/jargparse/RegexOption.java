package org.lifenoodles.jargparse;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
class RegexOption extends Option {
    private final String regex;
    public RegexOption(final String regex, final String name,
                        final String ... names) {
        super(name, names);
        this.regex = regex;
    }

    @Override
    public boolean isWellFormed() {
        return getArgument().matches(regex);
    }
}

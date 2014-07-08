package org.lifenoodles.jargparse;

/**
 * Created by Donagh Hatton on 7/7/14.
 */
class OptionValuePair {
    private final String option;
    private final String value;

    public OptionValuePair(final String option, final String value) {
        this.option = option;
        this.value = value;
    }

    public String getOption() {
        return option;
    }

    public String getValue() {
        return value;
    }
}

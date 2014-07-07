package org.lifenoodles.jargparse;

/**
 * Created by donagh on 7/7/14.
 */
interface OptionValidator {
    /**
     * Determines if this option takes an argument
     *
     * @return true if the option takes an argument
     */
    public abstract boolean takesArgument();

    /**
     * Determines if this option is well formed
     *
     * @return a boolean indicating if this argument is well formed
     */
    public abstract boolean isArgumentLegal(final String argument);

    public abstract String getDescription();
}

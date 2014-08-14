package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;
import org.lifenoodles.jargparse.exceptions.UnknownOptionException;

/**
 * @author Donagh Hatton
 *         created on 8/14/14.
 */
public class HelpfulArgumentParser extends ArgumentParser {
    public HelpfulArgumentParser() {
        addOption(optional("-h").alias("--help").arguments(0)
                .description("display this message and exit"));
    }

    public OptionSet parse(String... arguments) {
        try {
            OptionSet optionSet = super.parse(arguments);
            if (optionSet.contains("-h")) {
                System.err.print(getHelpText());
            }
            return optionSet;
        } catch (BadArgumentException e) {
            printUsage();
            System.err.println(String.format("Unrecognised argument for " +
                    "option %s : %s", e.option, e.argument));
        } catch (ArgumentCountException e) {
            printUsage();
            System.err.println(String.format("Bad argument count for " +
                            "option %s, expecting at least %d but received %d",
                    e.option, e.expected, e.received));
        } catch (UnknownOptionException e) {
            printUsage();
            System.err.println(String.format("Unknown option %s", e.option));
        }
        System.exit(1);
        assert (false);
        return null;
    }

    private void printUsage() {
        System.err.println(getUsage());
    }
}

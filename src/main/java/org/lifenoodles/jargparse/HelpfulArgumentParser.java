package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;
import org.lifenoodles.jargparse.exceptions.UnknownOptionException;

/**
 * Implements an ArgumentParser that automatically adds a help option with the
 * flags "-h" and "--h". If the application is called with this flag set, a
 * usage message is displayed and application exits. If the parsing fails for
 * some reason, the short usage message is displayed along with an appropriate
 * error message and the application exits.
 *
 * @author Donagh Hatton
 *         created on 8/14/14.
 */
public class HelpfulArgumentParser extends ArgumentParser {
    public HelpfulArgumentParser() {
        addOption(Argument.optional("-h").alias("--help").arguments(0)
                .description("display this message and exit").make());
    }

    @Override
    public HelpfulArgumentParser setApplicationName(
            final String applicationName) {
        return (HelpfulArgumentParser)
                super.setApplicationName(applicationName);
    }

    @Override
    public HelpfulArgumentParser setPrefixes(final String... optionPrefixes) {
        return (HelpfulArgumentParser) super.setPrefixes(optionPrefixes);
    }

    @Override
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

    /**
     * Print the usage message to standard err
     */
    private void printUsage() {
        System.err.println(getUsageText());
    }
}

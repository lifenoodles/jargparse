package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;
import org.lifenoodles.jargparse.exceptions.RequiredOptionException;
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
public class AutoOptionParser extends OptionParser {
    public AutoOptionParser() {
        this("AppName");
    }

    public AutoOptionParser(final String applicationName) {
        super(applicationName);
        add(Option.of("-h", "--help").helper()
                .description("Print this message and exit"));
    }

    @Override
    public AutoOptionParser add(Option option) {
        return (AutoOptionParser) super.add(option);
    }

    @Override
    public AutoOptionParser add(Positional option) {
        return (AutoOptionParser) super.add(option);
    }

    @Override
    public OptionSet parse(String... arguments) {
        try {
            OptionSet optionSet = super.parse(arguments);
            if (optionSet.contains("-h")) {
                System.out.print(getHelpText());
                System.exit(0);
            }
            return optionSet;
        } catch (BadArgumentException e) {
            printUsage();
            System.err.println(String.format("Illegal argument for " +
                    "option %s: %s", e.option, e.argument));
        } catch (ArgumentCountException e) {
            printUsage();
            System.err.println(String.format("Bad argument count for " +
                            "option %s, expecting at least %d but received %d",
                    e.option, e.expected, e.received));
        } catch (UnknownOptionException e) {
            printUsage();
            System.err.println(String.format("Unknown option %s", e.option));
        } catch (RequiredOptionException e) {
            printUsage();
            System.err.println(String.format("Required option %s missing",
                    e.option));
        }
        System.err.println();
        System.err.println("Run with -h or --help for more information");
        System.exit(1);
        assert false;
        return null;
    }

    /**
     * Print the usage message to standard err
     */
    private void printUsage() {
        System.err.println(getUsageText());
    }
}

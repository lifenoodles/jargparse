package org.lifenoodles.jargparse;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class ArgumentParserTest extends TestCase {
    public void testDuplicateOptionFails() {
        try {
            ArgumentParser parser = new ArgumentParser();
            parser.addOption(parser.optional("-f"))
                    .addOption(parser.optional("-f"));
            ArgumentParser nextParser = new ArgumentParser();
                nextParser.addOption(nextParser.positional("name"))
                        .addOption(nextParser.positional("name"));
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testDifferentOptionSucceeds() {
        try {
            ArgumentParser parser = new ArgumentParser();
            parser.addOption(parser.optional("-name"))
                    .addOption(parser.optional("-name2"))
                    .addOption(parser.positional("name3"));
        } catch (Exception e) {
            fail();
        }
    }

    public void testTooManyArgsFails() {
        final ArgumentParser parser = new ArgumentParser();
        parser.addOption(parser.optional("-t").arguments(0));
        try {
            parser.parse("-t", "string");
            fail();
        } catch (ArgumentCountException e) {
            //pass
        } catch (Exception e) {
            fail();
        }
    }

    public void testBadArgumentFails() {
        final ArgumentParser parser = new ArgumentParser();
        parser.addOption(parser.optional("-t").arguments(1)
                .matches(x -> x.length() == 3));
        try {
            parser.parse("-t", "abcd");
            fail();
        } catch (BadArgumentException e) {
            //pass
        } catch (Exception e) {
            fail();
        }
    }

    public void testGoodArgumentSucceeds() {
        final ArgumentParser parser = new ArgumentParser();
        parser.addOption(parser.optional("-t").arguments(1)
                .matches(x -> x.length() == 3));
        try {
            parser.parse("-t", "abc");
        } catch (Exception e) {
            fail();
        }
    }

    public void testSimpleParseSucceeds() {
        ArgumentParser parser = new ArgumentParser();
        parser.addOption(parser.optional("-f").arguments(0));
        try {
            parser.parse("-f");
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetUsage() {
        HelpfulArgumentParser parser = new HelpfulArgumentParser();
        parser.addOption(parser.optional("-f").alias("--flag")
            .description("this is a flag description").arguments(0));
        parser.addOption(parser.optional("-t").alias("--test")
                .description("this is a test description")
                .matches(x -> x.toUpperCase().equals(x))
                .arguments(1, "TEST"));
        parser.addOption(parser.positional("files")
                .arguments("*")
                .description("List of files to foo a bar with"));
        parser.parse("-t", "TEST");
    }
}

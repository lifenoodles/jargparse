package org.lifenoodles.jargparse;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;
import org.lifenoodles.jargparse.exceptions.UnknownOptionException;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class ArgumentParserTest extends TestCase {
    public void testDuplicateOptionFails() {
        try {
            OptionParser parser = new OptionParser();
            parser.addOption(Option.optional("-f").make())
                    .addOption(Option.optional("-f").make());
            OptionParser nextParser = new OptionParser();
                nextParser.addOption(Option.positional("name").make())
                        .addOption(Option.positional("name").make());
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testDifferentOptionSucceeds() {
        try {
            OptionParser parser = new OptionParser();
            parser.addOption(Option.optional("-name").make())
                    .addOption(Option.optional("-name2").make())
                    .addOption(Option.positional("name3").make());
        } catch (Exception e) {
            fail();
        }
    }

    public void testTooManyArgsFails() {
        final OptionParser parser = new OptionParser();
        parser.addOption(Option.optional("-t").arguments(0).make());
        try {
            parser.parse("-t", "string");
            fail();
        } catch (UnknownOptionException e) {
            //pass
        } catch (Exception e) {
            fail();
        }
    }

    public void testBadArgumentFails() {
        final OptionParser parser = new OptionParser();
        parser.addOption(Option.optional("-t").arguments(1)
                .matches(x -> x.length() == 3).make());
        try {
            parser.parse("-t", "abcd");
            fail();
        } catch (BadArgumentException e) {
            //pass
        } catch (Exception e) {
            fail();
        }
    }

    public void testTooFewArgumentsFails() {
        final OptionParser parser = new OptionParser();
        parser.addOption(Option.optional("-t").arguments(2).make())
                .addOption(Option.optional("-f").arguments(0).make());
        try {
            parser.parse("-t", "justone", "-f");
            fail();
        } catch (ArgumentCountException e) {
            //pass
        } catch (Exception e) {
            fail();
        }
    }

    public void testGoodArgumentSucceeds() {
        final OptionParser parser = new OptionParser();
        parser.addOption(Option.optional("-t").arguments(1)
                .matches(x -> x.length() == 3).make());
        try {
            parser.parse("-t", "abc");
        } catch (Exception e) {
            fail();
        }
    }

    public void testSimpleParseSucceeds() {
        OptionParser parser = new OptionParser();
        parser.addOption(Option.optional("-f").arguments(0).make());
        try {
            parser.parse("-f");
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetUsage() {
        HelpfulOptionParser parser = new HelpfulOptionParser();
        parser.setApplicationName("foo");
        parser.addOption(Option.optional("-f").alias("--flag")
            .description("this is a flag description").arguments(0).make());
        parser.addOption(Option.optional("-t").alias("--test")
                .description("this is a test description")
                .matches(x -> x.toUpperCase().equals(x))
                .arguments(1, "TEST").make());
        parser.addOption(Option.positional("files")
                .arguments("*")
                .description("List of files to foo a bar with").make());
    }
}

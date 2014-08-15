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
            ArgumentParser parser = new ArgumentParser();
            parser.addOption(Argument.optional("-f").make())
                    .addOption(Argument.optional("-f").make());
            ArgumentParser nextParser = new ArgumentParser();
                nextParser.addOption(Argument.positional("name").make())
                        .addOption(Argument.positional("name").make());
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testDifferentOptionSucceeds() {
        try {
            ArgumentParser parser = new ArgumentParser();
            parser.addOption(Argument.optional("-name").make())
                    .addOption(Argument.optional("-name2").make())
                    .addOption(Argument.positional("name3").make());
        } catch (Exception e) {
            fail();
        }
    }

    public void testTooManyArgsFails() {
        final ArgumentParser parser = new ArgumentParser();
        parser.addOption(Argument.optional("-t").arguments(0).make());
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
        final ArgumentParser parser = new ArgumentParser();
        parser.addOption(Argument.optional("-t").arguments(1)
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
        final ArgumentParser parser = new ArgumentParser();
        parser.addOption(Argument.optional("-t").arguments(2).make())
                .addOption(Argument.optional("-f").arguments(0).make());
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
        final ArgumentParser parser = new ArgumentParser();
        parser.addOption(Argument.optional("-t").arguments(1)
                .matches(x -> x.length() == 3).make());
        try {
            parser.parse("-t", "abc");
        } catch (Exception e) {
            fail();
        }
    }

    public void testSimpleParseSucceeds() {
        ArgumentParser parser = new ArgumentParser();
        parser.addOption(Argument.optional("-f").arguments(0).make());
        try {
            parser.parse("-f");
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetUsage() {
        HelpfulArgumentParser parser = new HelpfulArgumentParser();
        parser.setApplicationName("foo");
        parser.addOption(Argument.optional("-f").alias("--flag")
            .description("this is a flag description").arguments(0).make());
        parser.addOption(Argument.optional("-t").alias("--test")
                .description("this is a test description")
                .matches(x -> x.toUpperCase().equals(x))
                .arguments(1, "TEST").make());
        parser.addOption(Argument.positional("files")
                .arguments("*")
                .description("List of files to foo a bar with").make());
    }
}

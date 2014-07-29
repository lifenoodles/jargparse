package org.lifenoodles.jargparse;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;
import org.lifenoodles.jargparse.exceptions.UnknownOptionException;

import java.util.stream.Stream;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class ArgumentParserTest extends TestCase {
    public void testDuplicateOptionFails() {
        try {
            new ArgumentParser().addOption(
                    Option.optional("-f").make()).addOption(
                    Option.optional("-f").make());
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testTooManyArgsFails() {
        final ArgumentParser parser = new ArgumentParser()
                .addOption(Option.optional("-t").make());
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
        final ArgumentParser parser = new ArgumentParser()
                .addOption(Option.optional("-t").arguments(1)
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

    public void testGoodArgumentSucceeds() {
        final ArgumentParser parser = new ArgumentParser()
                .addOption(Option.optional("-t").arguments(1)
                        .matches(x -> x.length() == 3).make());
        try {
            parser.parse("-t", "abc");
        } catch (Exception e) {
            fail();
        }
    }

    public void testMultipleOptionalPositionalFails() {
        try {
            final ArgumentParser parser = new ArgumentParser();
            parser.addOption(Option.positional("first").optionalArguments(1)
                    .make());
            parser.addOption(Option.positional("second").optionalArguments(1)
                    .make());
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testSimpleParseSucceeds() {
        ArgumentParser parser = new ArgumentParser();
        parser.addOption(Option.optional("-f").make());
        try {
            parser.parse("-f");
        } catch (Exception e) {
            fail();
        }
    }
}

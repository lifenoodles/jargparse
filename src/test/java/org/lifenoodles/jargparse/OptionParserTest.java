package org.lifenoodles.jargparse;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.exceptions.ArgumentCountException;
import org.lifenoodles.jargparse.exceptions.BadArgumentException;
import org.lifenoodles.jargparse.exceptions.RequiredOptionException;
import org.lifenoodles.jargparse.exceptions.UnknownOptionException;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionParserTest extends TestCase {
    public void testBadArgumentFails() {
        final OptionParser parser = new OptionParser();
        parser.add(Option.of("-t").arguments(1)
                .matches(x -> x.length() == 3));
        try {
            parser.parse("-t", "dogs");
            fail();
        } catch (BadArgumentException e) {
            //pass
        } catch (Exception e) {
            fail();
        }
    }

    public void testDifferentOptionSucceeds() {
        try {
            new OptionParser()
                    .add(Option.of("name1"))
                    .add(Option.of("name2"))
                    .add(Positional.of("name3"));
        } catch (Exception e) {
            fail();
        }
    }

    public void testDuplicateOptionFails() {
        try {
            new OptionParser().add(Option.of("-t"))
                    .add(Positional.of("-t"));
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testDuplicateParse() {
        try {
            assertTrue(new OptionParser()
                    .add(Option.of("-t").arguments(1))
                    .parse("-t", "foo", "-t", "bar")
                    .get("-t").orElse("").equals("bar"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testGoodArgumentSucceeds() {
        final OptionParser parser = new OptionParser();
        parser.add(Option.of("-t").arguments(1)
                .matches(x -> x.length() == 3));
        try {
            parser.parse("-t", "abc");
        } catch (Exception e) {
            fail();
        }
    }

    public void testHelperAllowsNoPositional() {
        try {
            new OptionParser().add(Option.of("-h").helper())
                    .add(Positional.of("foo").arguments(2))
                    .parse("-h");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testHelperAllowsNoRequired() {
        try {
            new OptionParser().add(Option.of("-f").required())
                    .add(Option.of("-h").helper()).parse("-h");
        } catch (Exception e) {
            fail();
        }
    }

    public void testNoRequired() {
        try {
            new OptionParser().add(Option.of("-f").required()).parse();
            fail();
        } catch (RequiredOptionException e) {
            // pass
        } catch (Exception e) {
            fail();
        }
    }

    public void testPositionalDefaultRequired() {
        try {
            new OptionParser().add(Positional.of("foo").arguments(2))
                    .parse();
            fail();
        } catch (RequiredOptionException e) {
            assertTrue(e.option.equals("foo"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testPositionalNoArgs() {
        try {
            new OptionParser().add(
                    Positional.of("positional").arguments(0));
            fail();
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    public void testSimpleParseSucceeds() {
        OptionParser parser = new OptionParser();
        parser.add(Option.of("-f").arguments(0));
        try {
            parser.parse("-f");
        } catch (Exception e) {
            fail();
        }
    }

    public void testTooFewArgumentsFails() {
        final OptionParser parser = new OptionParser();
        parser.add(Option.of("-t").arguments(2))
                .add(Option.of("-f").arguments(0));
        try {
            parser.parse("-t", "just one", "-f");
            fail();
        } catch (ArgumentCountException e) {
            //pass
        } catch (Exception e) {
            fail();
        }
    }

    public void testTooManyArgsFails() {
        final OptionParser parser = new OptionParser();
        parser.add(Option.of("-t").arguments(0));
        try {
            parser.parse("-t", "string");
            fail();
        } catch (UnknownOptionException e) {
            //pass
        } catch (Exception e) {
            fail();
        }
    }
}

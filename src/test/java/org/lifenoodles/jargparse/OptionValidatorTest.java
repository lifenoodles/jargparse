package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionValidatorTest extends TestCase {
    public void testOptionalBadName() {
        try {
            OptionParser parser = new OptionParser();
            parser.addOption(Option.optional("bad").make());
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testPositionalBadName() {
        try {
            OptionParser parser = new OptionParser().setPrefixes("+");
            parser.addOption(Option.positional("+bad").make());
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testOptionKnowsName() {
        assertTrue(Option.optional("-t").alias("--test").make()
                .getName().equals("-t"));
        assertTrue(Option.positional("name").make().getName().
                equals("name"));
    }

    public void testOptionNamesLength() {
        assertTrue(Option.optional("-n").alias("-a", "-b", "-c")
                .make().getNames().size() == 4);
        assertTrue(Option.positional("t").make().getNames()
                .size() == 1);
    }

    public void testOptionLabels() {
        assertTrue(Option.optional("-t").arguments(0, "NONE").make()
                .helpFormat().equals("-t"));
        assertTrue(Option.optional("-t").arguments(1, "test").make()
                .helpFormat().equals("-t test"));
        assertTrue(Option.optional("-t").arguments(1).make()
                .helpFormat().equals("-t -T"));
        assertTrue(Option.optional("-t").arguments(2, "first", "second").make()
                .helpFormat().equals("-t first second"));
        assertTrue(Option.optional("-t").arguments(5, "first", "second").make()
                .helpFormat().equals("-t first second first first first"));
        assertTrue(Option.optional("-t").arguments("?", "opt").make()
                .helpFormat().equals("-t [opt]"));
        assertTrue(Option.optional("-t").arguments("*", "opt").make()
                .helpFormat().equals("-t [opt ...]"));
        assertTrue(Option.optional("-t").arguments("+", "opt").make()
                .helpFormat().equals("-t opt [opt ...]"));
        assertTrue(Option.optional("-t").arguments("+", "opt", "opts").make()
                .helpFormat().equals("-t opt [opts ...]"));
    }
}

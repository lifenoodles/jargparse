package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionValidatorTest extends TestCase {
    public void testOptionalBadName() {
        try {
            ArgumentParser parser = new ArgumentParser();
            parser.addOption(Argument.optional("bad").make());
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testPositionalBadName() {
        try {
            ArgumentParser parser = new ArgumentParser().setPrefixes("+");
            parser.addOption(Argument.positional("+bad").make());
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testOptionKnowsName() {
        assertTrue(Argument.optional("-t").alias("--test").make()
                .getName().equals("-t"));
        assertTrue(Argument.positional("name").make().getName().
                equals("name"));
    }

    public void testOptionNamesLength() {
        assertTrue(Argument.optional("-n").alias("-a", "-b", "-c")
                .make().getNames().size() == 4);
        assertTrue(Argument.positional("t").make().getNames()
                .size() == 1);
    }

    public void testOptionLabels() {
        assertTrue(Argument.optional("-t").arguments(0, "NONE").make()
                .helpFormat().equals("-t"));
        assertTrue(Argument.optional("-t").arguments(1, "test").make()
                .helpFormat().equals("-t test"));
        assertTrue(Argument.optional("-t").arguments(1).make()
                .helpFormat().equals("-t -T"));
        assertTrue(Argument.optional("-t").arguments(2, "first", "second").make()
                .helpFormat().equals("-t first second"));
        assertTrue(Argument.optional("-t").arguments(5, "first", "second").make()
                .helpFormat().equals("-t first second first first first"));
        assertTrue(Argument.optional("-t").arguments("?", "opt").make()
                .helpFormat().equals("-t [opt]"));
        assertTrue(Argument.optional("-t").arguments("*", "opt").make()
                .helpFormat().equals("-t [opt ...]"));
        assertTrue(Argument.optional("-t").arguments("+", "opt").make()
                .helpFormat().equals("-t opt [opt ...]"));
        assertTrue(Argument.optional("-t").arguments("+", "opt", "opts").make()
                .helpFormat().equals("-t opt [opts ...]"));
    }
}

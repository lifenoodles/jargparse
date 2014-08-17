package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionValidatorTest extends TestCase {
    public void testOptionBadName() {
        try {
            OptionParser parser = new OptionParser();
            parser.addOption(Option.of("ok", "--bad"));
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testOptionGoodName() {
        try {
            OptionParser parser = new OptionParser().setPrefixes("+");
            parser.addOption(Option.of("+ok"));
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    public void testOptionKnowsName() {
        assertTrue(Option.of("-t", "--test").make()
                .getName().equals("-t"));
        assertTrue(Option.of("name").make().getName().
                equals("name"));
    }

    public void testOptionNamesLength() {
        assertTrue(Option.of("-n", "-a", "-b", "-c")
                .make().getNames().size() == 4);
        assertTrue(Option.of("t").make().getNames()
                .size() == 1);
    }

    public void testOptionLabels() {
        assertTrue(Option.of("-t").arguments(0, "NONE").make()
                .helpFormat().equals("-t"));
        assertTrue(Option.of("-t").arguments(1, "test").make()
                .helpFormat().equals("-t test"));
        assertTrue(Option.of("-t").arguments(1).make()
                .helpFormat().equals("-t -T"));
        assertTrue(Option.of("-t").arguments(2, "first", "second").make()
                .helpFormat().equals("-t first second"));
        assertTrue(Option.of("-t").arguments(5, "first", "second").make()
                .helpFormat().equals("-t first second first first first"));
        assertTrue(Option.of("-t").arguments("?", "opt").make()
                .helpFormat().equals("-t [opt]"));
        assertTrue(Option.of("-t").arguments("*", "opt").make()
                .helpFormat().equals("-t [opt ...]"));
        assertTrue(Option.of("-t").arguments("+", "opt").make()
                .helpFormat().equals("-t opt [opt ...]"));
        assertTrue(Option.of("-t").arguments("+", "opt", "opts").make()
                .helpFormat().equals("-t opt [opts ...]"));
    }
}

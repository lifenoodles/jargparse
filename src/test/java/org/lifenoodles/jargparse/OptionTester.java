package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionTester extends TestCase {
    public void testOptionKnowsName() {
        assertTrue(Option.flag("-t").make().getName().equals("-t"));
        assertTrue(Option.string("-t").alias("--test").make().getName().equals("-t"));
        assertTrue(Option.positional("Name", 0).make().getName().equals("Name"));
    }

    public void testLegalArguments() {
        assertFalse(Option.flag("--name").make().isArgumentLegal("anything"));
        assertTrue(Option.string("--name").make().isArgumentLegal("anything"));
        assertTrue(Option.string("--name").matches(x -> x.length() == 2).make()
                .isArgumentLegal("ab"));
        assertFalse(Option.string("--name").matches(x -> x.length() == 2).make()
                .isArgumentLegal("abc"));
    }
}

package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionTester extends TestCase {
    public void testOptionKnowsName() {
        assertTrue(Option.optional("-t").alias("--test").make().getName()
                .equals("-t"));
        assertTrue(Option.positional("name").make().getName().
                equals("name"));
    }

    public void testLegalArguments() {
        assertTrue(Option.optional("--name").make()
                .isArgumentLegal("anything"));
        assertTrue(Option.optional("--name").matches(x -> x.length() == 2)
                .make().isArgumentLegal("ab"));
        assertFalse(Option.optional("--name").matches(x -> x.length() == 2)
                .make().isArgumentLegal("abc"));
    }
}

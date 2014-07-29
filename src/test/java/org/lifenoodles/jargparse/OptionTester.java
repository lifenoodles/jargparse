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

    public void testOptionNamesLength() {
        assertTrue(Option.optional("-n").alias("a", "b", "c").make()
                .getNames().size() == 4);
        assertTrue(Option.positional("-t").make().getNames().size() == 1);
    }

    public void testOptionalLegal() {
        assertFalse(Option.optional("-t").make().isArgumentLegal("anything"));
        assertTrue(Option.optional("-t").arguments(1).make()
                .isArgumentLegal("anything"));
        assertTrue(Option.optional("-t").arguments(1).matches(
                x -> x.equals("hi")).make().isArgumentLegal("hi"));
        assertFalse(Option.optional("-t").arguments(1).matches(
                x -> x.equals("hi")).make().isArgumentLegal("not hi"));
    }

    public void testOptionArgumentCount() {
        assertTrue(Option.positional("-t").make().getArgumentCount() == 1);
        assertTrue(Option.optional("-t").make().getArgumentCount() == 0);
        assertTrue(Option.optional("-t").arguments(1).make()
                .getArgumentCount() == 1);
        assertTrue(Option.optional("-t").arguments(3).make()
                .getArgumentCount() == 3);
    }
}

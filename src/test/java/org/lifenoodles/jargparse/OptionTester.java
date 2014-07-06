package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionTester extends TestCase {
    public void testOptionKnowsName() {
        final Option option = new FlagOption("-t", "--test");
        assertTrue(option.hasName("-t"));
        assertTrue(option.hasName("--test"));
    }

    public void testOptionHasArgument() {
        assertFalse(new FlagOption("-t").hasArgument());
        assertTrue(new StringOption("arg", "--flag").hasArgument());
    }

    public void testIsWellFormed() {
        assertTrue(new FlagOption("-t").isWellFormed());
        assertTrue(new StringOption("arg", "--flag").isWellFormed());
    }
}

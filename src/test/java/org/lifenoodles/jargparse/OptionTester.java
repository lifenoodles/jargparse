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
        assertFalse(new FlagOption("--name").hasArgument());
        assertTrue(new StringOption("arg", "--name").hasArgument());
        assertTrue(new RegexOption("regex", "arg", "--name").hasArgument());
    }

    public void testIsWellFormed() {
        assertTrue(new FlagOption("--name").isWellFormed());
        assertTrue(new StringOption("arg", "--name").isWellFormed());
        assertTrue(new RegexOption("regex", "arg", "--name")
                .setArgument("regex").isWellFormed());
    }
}

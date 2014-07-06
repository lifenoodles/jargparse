package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionTester extends TestCase {
    public void testOptionKnowsName() {
        final OptionValidator optionValidator = new FlagOptionValidator("-t", "--test");
        assertTrue(optionValidator.hasName("-t"));
        assertTrue(optionValidator.hasName("--test"));
    }

    public void testOptionHasArgument() {
        assertFalse(new FlagOptionValidator("--name").hasArgument());
        assertTrue(new StringOptionValidator("arg", "--name").hasArgument());
        assertTrue(new RegexOption("regex", "arg", "--name").hasArgument());
    }

    public void testIsWellFormed() {
        assertTrue(new FlagOptionValidator("--name").isLegal());
        assertTrue(new StringOptionValidator("arg", "--name").isLegal());
        assertTrue(new RegexOption("regex", "arg", "--name")
                .setArgument("regex").isWellFormed());
    }
}

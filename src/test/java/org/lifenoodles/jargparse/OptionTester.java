package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionTester extends TestCase {
    public void testOptionKnowsName() {
        final NamedOptionValidator optionValidator = new FlagOptionValidator("desc", "-t", "--test");
        assertTrue(optionValidator.getName().equals("-t"));
    }

    public void testIsWellFormed() {
        assertFalse(new FlagOptionValidator("desc", "--name").isArgumentLegal("anything"));
        assertTrue(new StringOptionValidator(x -> true, "desc", "arg", "--name").isArgumentLegal("anything"));
        assertTrue(new StringOptionValidator(x -> x.length() == 2, "desc", "arg", "--name").isArgumentLegal("ab"));
        assertFalse(new StringOptionValidator(x -> x.length() == 2, "desc", "arg", "--name").isArgumentLegal("abc"));
    }
}

package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionValidatorTest extends TestCase {
    public void testOptionKnowsName() {
        assertTrue(Option.of("-t", "--test").make().getName().equals("-t"));
        assertTrue(Option.of("name").make().getName().equals("name"));
    }

    public void testOptionNamesLength() {
        assertTrue(Option.of("-n", "-a", "-b", "-c").make().getNames()
                .size() == 4);
        assertTrue(Option.of("t").make().getNames().size() == 1);
    }

    public void testOptionLabels() {
        assertTrue(Option.of("-t").arguments(0, "NONE").make()
                .formatHelp().equals("-t"));
        assertTrue(Option.of("-t").arguments(1, "test").make()
                .formatHelp().equals("-t test"));
        assertTrue(Option.of("-t").arguments(1).make()
                .formatHelp().equals("-t -T"));
        assertTrue(Option.of("-t").arguments(2, "first", "second").make()
                .formatHelp().equals("-t first second"));
        assertTrue(Option.of("-t").arguments(5, "first", "second").make()
                .formatHelp().equals("-t first second first first first"));
        assertTrue(Option.of("-t").arguments("?", "opt").make()
                .formatHelp().equals("-t [opt]"));
        assertTrue(Option.of("-t").arguments("*", "opt").make()
                .formatHelp().equals("-t [opt ...]"));
        assertTrue(Option.of("-t").arguments("+", "opt").make()
                .formatHelp().equals("-t opt [opt ...]"));
        assertTrue(Option.of("-t").arguments("+", "opt", "opts").make()
                .formatHelp().equals("-t opt [opts ...]"));
    }

    public void testOptionSizes() {
        assertTrue(Option.of("-t").arguments(0).make()
                .minimumArgumentCount() == 0);
        assertTrue(Option.of("-t").arguments(0).make()
                .maximumArgumentCount() == 0);
        assertTrue(Option.of("-t").arguments(5).make()
                .minimumArgumentCount() == 5);
        assertTrue(Option.of("-t").arguments(5).make()
                .maximumArgumentCount() == 5);
        assertTrue(Option.of("-t").arguments("*").make()
                .minimumArgumentCount() == 0);
        assertTrue(Option.of("-t").arguments("*").make()
                .maximumArgumentCount() == Integer.MAX_VALUE);
        assertTrue(Option.of("-t").arguments("?").make()
                .minimumArgumentCount() == 0);
        assertTrue(Option.of("-t").arguments("?").make()
                .maximumArgumentCount() == 1);
        assertTrue(Option.of("-t").arguments("+").make()
                .minimumArgumentCount() == 1);
        assertTrue(Option.of("-t").arguments("+").make()
                .maximumArgumentCount() == Integer.MAX_VALUE);
        assertTrue(Option.of("-t").arguments(2, 3).make()
                .minimumArgumentCount() == 2);
        assertTrue(Option.of("-t").arguments(2, 3).make()
                .maximumArgumentCount() == 3);
    }

    public void testOptionMatches() {
        assertTrue(Option.of("-t").matches(x -> x.endsWith("foo")).make()
                .isArgumentLegal("foo"));
        assertFalse(Option.of("-t").matches(x -> x.endsWith("foo")).make()
                .isArgumentLegal("bar"));
        assertTrue(Option.of("-t").matches("foo", "bar").make()
                .isArgumentLegal("foo"));
        assertFalse(Option.of("-t").matches("foo", "bar").make()
                .isArgumentLegal("word"));
        assertTrue(Option.of("-t").matches(Utility.listOf("foo", "bar")).make()
                .isArgumentLegal("bar"));
        assertFalse(Option.of("-t").matches(Utility.listOf("foo", "bar")).make()
                .isArgumentLegal("word"));
    }
}

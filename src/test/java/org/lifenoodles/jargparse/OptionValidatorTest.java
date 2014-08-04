package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionValidatorTest extends TestCase {
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
        assertTrue(Option.optional("-t").make().isArgumentListLegal(
                Utility.listOf("anything")));
        assertTrue(Option.optional("-t").arguments(3).make()
                .isArgumentListLegal(
                        Utility.listOf("anything", "and", "something")));
        assertTrue(Option.optional("-t").arguments(3).make()
                .isArgumentListLegal(
                        Utility.listOf("anything", "and", "something", "-e")));
        assertTrue(Option.optional("-t").arguments(0).make()
                .isArgumentListLegal(Utility.listOf()));
        assertTrue(Option.positional("name").arguments(1)
                .matches(x -> x.length() == 3).make()
                .isArgumentListLegal(
                        Utility.listOf("any", "-e")));
    }

    public void testOptionArgumentCount() {
        assertTrue(Option.positional("t").make().isArgumentCountCorrect(
                Utility.listOf("one item")));
        assertFalse(Option.positional("t").make().isArgumentCountCorrect(
                Utility.listOf("two", "items")));
        assertTrue(Option.positional("t").arguments(0).make()
                .isArgumentCountCorrect(Utility.listOf()));
        assertTrue(Option.optional("-t").arguments("+").make()
                .isArgumentCountCorrect(
                        Utility.listOf("-f", "one", "or", "more")));
        assertFalse(Option.optional("-t").arguments("+").make()
                .isArgumentCountCorrect(Utility.listOf("-f")));
        assertTrue(Option.optional("-t").arguments("*").make()
                .isArgumentCountCorrect(
                        Utility.listOf("-f", "items", "in", "here")));
        assertTrue(Option.optional("-t").arguments("*").make()
                .isArgumentCountCorrect(Utility.listOf()));
        assertFalse(Option.optional("-t").arguments("?").make()
                .isArgumentCountCorrect(Utility.listOf("-f", "items", "here")));
        assertTrue(Option.optional("-t").arguments("?").make()
                .isArgumentCountCorrect(Utility.listOf("-f", "item")));
        assertTrue(Option.optional("-t").arguments("?").make()
                .isArgumentCountCorrect(Utility.listOf("-f")));
    }
}

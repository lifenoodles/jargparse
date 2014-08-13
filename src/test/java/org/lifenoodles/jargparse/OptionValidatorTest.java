package org.lifenoodles.jargparse;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.parsers.ZeroOrOneParser;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class OptionValidatorTest extends TestCase {
    public void testOptionalBadName() {
        try {
            ArgumentParser parser = new ArgumentParser();
            parser.addOption(parser.optional("bad"));
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testPositionalBadName() {
        try {
            ArgumentParser parser = new ArgumentParser("+");
            parser.positional("+bad").make();
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testOptionKnowsName() {
        assertTrue(new ArgumentParser().optional("-t").alias("--test").make()
                .getName().equals("-t"));
        assertTrue(new ArgumentParser().positional("name").make().getName().
                equals("name"));
    }

    public void testOptionNamesLength() {
        assertTrue(new ArgumentParser().optional("-n").alias("-a", "-b", "-c")
                .make().getNames().size() == 4);
        assertTrue(new ArgumentParser().positional("t").make().getNames()
                .size() == 1);
    }

    public void testOptionalLegal() {
        assertTrue(new ArgumentParser().optional("-t").make()
                .isArgumentListLegal(Utility.listOf("anything")));
        assertTrue(new ArgumentParser().optional("-t").arguments(3).make()
                .isArgumentListLegal(
                        Utility.listOf("anything", "and", "something")));
        assertTrue(new ArgumentParser().optional("-t").arguments(3).make()
                .isArgumentListLegal(
                        Utility.listOf("anything", "and", "something", "-e")));
        assertTrue(new ArgumentParser().optional("-t").arguments(0).make()
                .isArgumentListLegal(Utility.listOf()));
        assertTrue(new ArgumentParser().positional("name").arguments(1)
                .matches(x -> x.length() == 3).make()
                .isArgumentListLegal(Utility.listOf("any", "-e")));
    }

    public void testOptionArgumentCount() {
        assertTrue(new ArgumentParser().positional("t").make()
                .isArgumentCountCorrect(Utility.listOf("one item")));
        assertFalse(new ArgumentParser().positional("t").make()
                .isArgumentCountCorrect(Utility.listOf("two", "items")));
        assertTrue(new ArgumentParser().positional("t").arguments(0).make()
                .isArgumentCountCorrect(Utility.listOf()));
        assertTrue(new ArgumentParser().optional("-t").arguments("+").make()
                .isArgumentCountCorrect(
                        Utility.listOf("-f", "one", "or", "more")));
        assertFalse(new ArgumentParser().optional("-t").arguments("+").make()
                .isArgumentCountCorrect(Utility.listOf("-f")));
        assertTrue(new ArgumentParser().optional("-t").arguments("*").make()
                .isArgumentCountCorrect(
                        Utility.listOf("-f", "items", "in", "here")));
        assertTrue(new ArgumentParser().optional("-t").arguments("*").make()
                .isArgumentCountCorrect(Utility.listOf()));
        assertFalse(new ArgumentParser().optional("-t").arguments("?").make()
                .isArgumentCountCorrect(Utility.listOf("-f", "items", "here")));
        assertTrue(new ArgumentParser().optional("-t").arguments("?").make()
                .isArgumentCountCorrect(Utility.listOf("-f", "item")));
        assertTrue(new ArgumentParser().optional("-t").arguments("?").make()
                .isArgumentCountCorrect(Utility.listOf("-f")));
    }

    public void testOptionLabels() {
        ArgumentParser parser = new ArgumentParser();
        assertTrue(parser.optional("-t").arguments(0, "NONE").make()
                .helpSummary().equals("-t"));
        assertTrue(parser.optional("-t").arguments(1, "test").make()
                .helpSummary().equals("-t test"));
        assertTrue(parser.optional("-t").arguments(1).make()
                .helpSummary().equals("-t t"));
        assertTrue(parser.optional("-t").arguments(2, "first", "second").make()
                .helpSummary().equals("-t first second"));
        assertTrue(parser.optional("-t").arguments(5, "first", "second").make()
                .helpSummary().equals("-t first second first first first"));
        assertTrue(parser.optional("-t").arguments("?", "opt").make()
                .helpSummary().equals("-t [opt]"));
        assertTrue(parser.optional("-t").arguments("*", "opt").make()
                .helpSummary().equals("-t [opt ...]"));
        assertTrue(parser.optional("-t").arguments("+", "opt").make()
                .helpSummary().equals("-t opt [opt ...]"));
        assertTrue(parser.optional("-t").arguments("+", "opt", "opts").make()
                .helpSummary().equals("-t opt [opts ...]"));
    }
}

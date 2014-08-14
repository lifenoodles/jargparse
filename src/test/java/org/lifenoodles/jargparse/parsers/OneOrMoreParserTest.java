package org.lifenoodles.jargparse.parsers;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.OneOrMoreParser;
import org.lifenoodles.jargparse.Utility;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 04/08/2014.
 */
public class OneOrMoreParserTest extends TestCase {
    public static void testIsCountCorrect() {
        assertTrue(new OneOrMoreParser().isCountCorrect(Utility.listOf("a")));
        assertTrue(new OneOrMoreParser().isCountCorrect(Utility.listOf(
                "a", "b", "c", "-flag")));
        assertTrue(new OneOrMoreParser().isCountCorrect(Utility.listOf(
                "a", "b", "c", "-d", "-e", "f")));
        assertFalse(new OneOrMoreParser().isCountCorrect(Utility.listOf()));
    }

    public static void testExtractArguments() {
        final List<String> argList = Utility.listOf("opt1", "opt2", "-flag");
        OneOrMoreParser parser = new OneOrMoreParser();
        assertTrue(Utility.sameLists(
                parser.extractArguments(argList),
                Utility.listOf("opt1", "opt2")));
    }

    public void testRestOfArguments() {
        final List<String> argList = Utility.listOf("opt1", "opt2", "-flag");
        OneOrMoreParser parser = new OneOrMoreParser();
        assertTrue(Utility.sameLists(
                parser.restOfArguments(argList),
                Utility.listOf("-flag")));
    }
}

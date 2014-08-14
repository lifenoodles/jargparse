package org.lifenoodles.jargparse.parsers;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.FixedCountParser;
import org.lifenoodles.jargparse.Utility;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 04/08/2014.
 */
public class FixedCountParserTest extends TestCase {
    public static void testIsCountCorrect() {
        assertTrue(new FixedCountParser(1).isCountCorrect(Utility.listOf("a")));
        assertTrue(new FixedCountParser(1).isCountCorrect(Utility.listOf(
                "a", "-flag")));
        assertTrue(new FixedCountParser(3).isCountCorrect(Utility.listOf(
                "a", "b", "c", "-d", "-e", "f")));
        assertFalse(new FixedCountParser(1).isCountCorrect(Utility.listOf()));
    }

    public static void testExtractArguments() {
        final List<String> argList = Utility.listOf("opt1", "opt2", "-flag");
        FixedCountParser parser = new FixedCountParser(2);
        assertTrue(Utility.sameLists(
                parser.extractArguments(argList),
                Utility.listOf("opt1", "opt2")));
    }

    public void testRestOfArguments() {
        final List<String> argList = Utility.listOf("opt1", "opt2", "-flag");
        FixedCountParser parser = new FixedCountParser(2);
        assertTrue(Utility.sameLists(
                parser.restOfArguments(argList),
                Utility.listOf("-flag")));
    }
}

package org.lifenoodles.jargparse.parsers;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.Utility;
import org.lifenoodles.jargparse.ZeroOrOneParser;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 04/08/2014.
 */
public class ZeroOrOneParserTest extends TestCase {
    public static void testIsCountCorrect() {
        assertTrue(new ZeroOrOneParser().isCountCorrect(Utility.listOf("a")));
        assertTrue(new ZeroOrOneParser().isCountCorrect(Utility.listOf(
                "a", "-flag")));
        assertFalse(new ZeroOrOneParser().isCountCorrect(Utility.listOf(
                "a", "b", "c", "-d", "-e", "f")));
        assertTrue(new ZeroOrOneParser().isCountCorrect(Utility.listOf()));
    }

    public static void testExtractArguments() {
        final List<String> argList = Utility.listOf("opt1", "-flag");
        ZeroOrOneParser parser = new ZeroOrOneParser();
        assertTrue(Utility.sameLists(
                parser.extractArguments(argList),
                Utility.listOf("opt1")));
        assertTrue(Utility.sameLists(
                parser.extractArguments(Utility.listOf("-a")),
                Utility.listOf()));
    }

    public void testRestOfArguments() {
        final List<String> argList = Utility.listOf("opt1", "-flag");
        ZeroOrOneParser parser = new ZeroOrOneParser();
        assertTrue(Utility.sameLists(
                parser.restOfArguments(argList),
                Utility.listOf("-flag")));
        assertTrue(Utility.sameLists(
                parser.restOfArguments(Utility.listOf()),
                Utility.listOf()));
    }
}

package org.lifenoodles.jargparse.parsers;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.Utility;
import org.lifenoodles.jargparse.ZeroOrMoreCounter;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 04/08/2014.
 */
public class ZeroOrMoreParserTest extends TestCase {
    public static void testIsCountCorrect() {
        assertTrue(new ZeroOrMoreCounter().isCountCorrect(Utility.listOf("a")));
        assertTrue(new ZeroOrMoreCounter().isCountCorrect(Utility.listOf(
                "a", "-flag")));
        assertTrue(new ZeroOrMoreCounter().isCountCorrect(Utility.listOf(
                "a", "b", "c", "-d", "-e", "f")));
        assertTrue(new ZeroOrMoreCounter().isCountCorrect(Utility.listOf()));
    }

    public static void testExtractArguments() {
        final List<String> argList = Utility.listOf("opt1", "opt2", "-flag");
        ZeroOrMoreCounter parser = new ZeroOrMoreCounter();
        assertTrue(Utility.sameLists(
                parser.extractArguments(argList),
                Utility.listOf("opt1", "opt2")));
        assertTrue(Utility.sameLists(
                parser.extractArguments(Utility.listOf("-a")),
                Utility.listOf()));
    }

    public void testRestOfArguments() {
        final List<String> argList = Utility.listOf("opt1", "opt2", "-flag");
        ZeroOrMoreCounter parser = new ZeroOrMoreCounter();
        assertTrue(Utility.sameLists(
                parser.restOfArguments(argList),
                Utility.listOf("-flag")));
    }
}

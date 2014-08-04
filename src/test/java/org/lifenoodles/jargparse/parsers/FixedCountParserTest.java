package org.lifenoodles.jargparse.parsers;

import junit.framework.TestCase;
import org.lifenoodles.jargparse.Utility;

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
}

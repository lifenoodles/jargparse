package org.lifenoodles.jargparse;

import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Donagh Hatton
 *         created on 04/08/2014.
 */
public class UtilityTest extends TestCase {
    public static void testSameLists() {
        assertTrue(Utility.sameLists(
                Stream.of("a", "b", "c").collect(Collectors.toList()),
                Stream.of("a", "b", "c").collect(Collectors.toList())));
        assertFalse(Utility.sameLists(
                Stream.of("a", "b", "c").collect(Collectors.toList()),
                Stream.of("a", "c").collect(Collectors.toList())));
        assertTrue(Utility.sameLists(new LinkedList<>(), new LinkedList<>()));
    }

    public static void testListOf() {
        List<String> list = Utility.listOf("a", "b", "c");
        assertTrue(list.size() == 3);
        assertTrue(list.get(0).equals("a"));
        assertTrue(list.get(1).equals("b"));
        assertTrue(list.get(2).equals("c"));
        assertTrue(Utility.listOf().size() == 0);
    }

    public void testIsOption() {
        assertTrue(Utility.isOption("-h"));
        assertTrue(Utility.isOption("--help"));
        assertFalse(Utility.isOption(""));
        assertFalse(Utility.isOption("something else"));
    }

    public void testFirstArgumentIndex() {
        List<String> arguments = Utility.listOf(
                "opt", "opt2", "--flag", "opt3");
        assertTrue(Utility.firstArgumentIndex(arguments).map(x -> x == 2)
                .orElse(false));
         List<String> noFlagArguments = Utility.listOf(
                "opt", "opt2", "noflag", "opt3");
        assertFalse(Utility.firstArgumentIndex(noFlagArguments).isPresent());
        assertFalse(Utility.firstArgumentIndex(new LinkedList<>()).isPresent());
    }

    public void testArgumentCount() {
        List<String> arguments = Utility.listOf(
                "opt", "opt2", "--flag", "opt3");
        assertTrue(Utility.argumentCount(arguments) == 2);
        List<String> noFlagArguments = Utility.listOf(
                "opt", "opt2", "noflag", "opt3");
        assertTrue(Utility.argumentCount(noFlagArguments) ==
                noFlagArguments.size());
        assertTrue(Utility.argumentCount(new LinkedList<>()) == 0);
    }
}

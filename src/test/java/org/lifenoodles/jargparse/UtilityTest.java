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
        assertTrue(new ArgumentParser().isOption("-h"));
        assertTrue(new ArgumentParser().isOption("--help"));
        assertFalse(new ArgumentParser().isOption(""));
        assertFalse(new ArgumentParser().isOption("something else"));
    }

    public void testDropN() {
        assertTrue(Utility.sameLists(
                Utility.dropN(2, Utility.listOf("1", "2", "3", "4")),
                Utility.listOf("3", "4")));
        assertTrue(Utility.dropN(10, Utility.listOf()).size() == 0);
    }
}

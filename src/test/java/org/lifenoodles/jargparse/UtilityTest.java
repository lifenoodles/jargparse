package org.lifenoodles.jargparse;

import junit.framework.TestCase;

import java.util.List;

/**
 * @author Donagh Hatton
 *         created on 04/08/2014.
 */
public class UtilityTest extends TestCase {
    public static void testListOf() {
        List<String> list = Utility.listOf("a", "b", "c");
        assertTrue(list.size() == 3);
        assertTrue(list.get(0).equals("a"));
        assertTrue(list.get(1).equals("b"));
        assertTrue(list.get(2).equals("c"));
        assertTrue(Utility.listOf().size() == 0);
    }

    public void testDropN() {
        assertTrue(Utility.dropN(2, Utility.listOf("1", "2", "3", "4"))
                .equals(Utility.listOf("3", "4")));
        assertTrue(Utility.dropN(10, Utility.listOf()).size() == 0);
    }
}

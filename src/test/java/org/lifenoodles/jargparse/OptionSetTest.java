package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 8/8/14.
 */
public class OptionSetTest extends TestCase {
    private OptionParser parser;

    public void setUp() {
        parser = new OptionParser();
        parser.add(Option.of("-h", "--help").arguments(0));
        parser.add(Option.of("-v", "--verbose").arguments("?")
                .matches(x -> x.matches("[0-2]")));
        parser.add(Positional.of("files").arguments("*"));
    }

    public void testGetArgument() {
        try {
            OptionSet optionSet = parser.parse("-v", "2");
            assertTrue(optionSet.get("--verbose").orElse("")
                    .equals("2"));
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetArguments() {
        try {
            OptionSet optionSet = parser.parse("file1", "file2", "file3");
            assertTrue(optionSet.getArguments("files").size() == 3);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testIsOptionPresent() {
        try {
            OptionSet optionSet = parser.parse("--help");
            assertTrue(optionSet.contains("-h"));
            assertTrue(optionSet.contains("--help"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}

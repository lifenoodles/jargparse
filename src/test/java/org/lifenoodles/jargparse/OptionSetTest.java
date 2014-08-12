package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 8/8/14.
 */
public class OptionSetTest extends TestCase {
    private ArgumentParser parser;

    public void setUp() {
        parser = new ArgumentParser();
        parser.addOption(parser.optional("-h").alias("--help").arguments(0));
        parser.addOption(parser.optional("-v").alias("--verbose")
                .arguments("?").matches(x -> x.matches("[0-2]")));
        parser.addOption(parser.positional("files").arguments("*"));
    }

    public void testIsOptionPresent() {
        try {
            OptionSet optionSet = parser.parse("--help");
            assertTrue(optionSet.isOptionPresent("-h"));
            assertTrue(optionSet.isOptionPresent("--help"));
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetArgument() {
        try {
            OptionSet optionSet = parser.parse("-v", "2");
            assertTrue(optionSet.getArgument("--verbose").orElse("")
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
            fail();
        }
    }
}

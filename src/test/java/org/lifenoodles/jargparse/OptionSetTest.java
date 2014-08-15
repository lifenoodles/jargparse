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
        parser.addOption(Argument.optional("-h").alias("--help").arguments(0)
                .make());
        parser.addOption(Argument.optional("-v").alias("--verbose")
                .arguments("?").matches(x -> x.matches("[0-2]")).make());
        parser.addOption(Argument.positional("files").arguments("*").make());
    }

    public void testIsOptionPresent() {
        try {
            OptionSet optionSet = parser.parse("--help");
            assertTrue(optionSet.contains("-h"));
            assertTrue(optionSet.contains("--help"));
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

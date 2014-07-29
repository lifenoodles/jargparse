package org.lifenoodles.jargparse;

import junit.framework.TestCase;
import java.util.stream.Stream;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class ArgumentParserTest extends TestCase {
    private String[] asArray(Stream<String> s) {
        return s.toArray(x -> new String[x]);
    }

    public void testDuplicateOptionFails() {
        try {
            new ArgumentParser().addOption(
                    Option.optional("-f").make()).addOption(
                    Option.optional("-f").make());
            fail();
        } catch (IllegalArgumentException e) { }
    }

    public void testParseSucceeds() {
        ArgumentParser parser = new ArgumentParser();
        parser.addOption(Option.optional("-f").make());
        try {
            parser.parse(asArray(Stream.of("-f")));
        } catch (Exception e) {
            fail();
        }
    }
}

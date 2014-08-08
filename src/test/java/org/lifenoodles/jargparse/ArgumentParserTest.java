package org.lifenoodles.jargparse;

import junit.framework.TestCase;

/**
 * @author Donagh Hatton
 *         created on 06/07/2014.
 */
public class ArgumentParserTest extends TestCase {
    public void testDuplicateOptionFails() {
        try {
            new ArgumentParser()
                    .addOption(Option.optional("-f").make())
                    .addOption(Option.optional("-f").make());
            new ArgumentParser()
                    .addOption(Option.positional("name").make())
                    .addOption(Option.positional("name").make());
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    public void testDifferentOptionSucceeds() {
        try {
            new ArgumentParser()
                    .addOption(Option.optional("-name").make())
                    .addOption(Option.optional("-name2").make())
                    .addOption(Option.positional("name3").make());
        } catch (Exception e) {
            fail();
        }
    }

//    public void testTooManyArgsFails() {
//        final ArgumentParser parser = new ArgumentParser()
//                .addOption(Option.optional("-t").make());
//        try {
//            parser.parse("-t", "string");
//            fail();
//        } catch (ArgumentCountException e) {
//            //pass
//        } catch (Exception e) {
//            fail();
//        }
//    }
//
//    public void testBadArgumentFails() {
//        final ArgumentParser parser = new ArgumentParser()
//                .addOption(Option.optional("-t").arguments(1)
//                        .matches(x -> x.length() == 3).make());
//        try {
//            parser.parse("-t", "abcd");
//            fail();
//        } catch (BadArgumentException e) {
//            //pass
//        } catch (Exception e) {
//            fail();
//        }
//    }
//
//    public void testGoodArgumentSucceeds() {
//        final ArgumentParser parser = new ArgumentParser()
//                .addOption(Option.optional("-t").arguments(1)
//                        .matches(x -> x.length() == 3).make());
//        try {
//            parser.parse("-t", "abc");
//        } catch (Exception e) {
//            fail();
//        }
//    }

    public void testSimpleParseSucceeds() {
        ArgumentParser parser = new ArgumentParser();
        parser.addOption(Option.optional("-f").arguments(0).make());
        try {
            parser.parse("-f");
        } catch (Exception e) {
            fail();
        }
    }
}

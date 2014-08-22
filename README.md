Jargparse Documentation
-----------------------
Jargparse is a simple library for handling command line parsing in Java.
Documentation is a WIP.

Examples:

```java
// an AutoOptionParser automatically includes -h and --help as options
HelpfulOptionParser parser = new HelpfulOptionParser("Foo");
// options can be added via a fluent interface
parser.addOption(Option.of("-f", "--full-name"));
parser.addOption(Option.of("-t", "--other-full-name"));
// there are two types of options, Option and Positional
parser.addOption(Positional.of("positional"));
// the String[] args array should be passed to the parser
// an OptionSet contains the parsed results
OptionSet optionSet = parser.parse();
// you can check if an option exists with contains(option)
assert optionSet.contains("-f");
// any name can be used
assert optionSet.contains("--other-full-name");
// optionSet::get returns an Optional<String>, rather than null if
// not present
assert optionSet.get("positional").orElse("").equals("argument");
```

```java
// all methods can be chained
// options can be given an arbitrary number of args
HelpfulOptionParser parser = new HelpfulOptionParser()
        .addOption(Option.of("--takes-2").arguments(2))
        .addOption(Option.of("--takes-1-to-3").arguments(1, 3))
         .addOption(Option.of("--takes-0-or-more").arguments("*"))
        .addOption(Option.of("--takes-1-or-more").arguments("+"))
        .addOption(Option.of("--takes-0-or-1").arguments("?"));

// options can be marked as required, parsing will fail if they are omitted
parser.addOption(Option.of("--this-is-required").required());
// options can be marked as helpers, helpers will allow required options to be omitted
parser.addOption(Option.of("--this-is-a-helper").helper());
```

```java
// options can be given either a predicate or a list of values to match
HelpfulOptionParser parser = new HelpfulOptionParser()
        .addOption(Positional.of("longer-than-3").matches(x -> x.length() > 3))
        .addOption(Positional.of("a-b-c").matches("a", "b", "c"));
```

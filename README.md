Jargparse Documentation
-----------------------
Jargparse is a simple library for handling command line parsing in Java.

See the [javadocs](http://lifenoodles.github.io/jargparse/) for full documentation.

Typical usage is to create an OptionParser or AutoOptionParser object,
register options with it using the fluent interface provided by Option.of()
or Positional.of() and pass the String[] args array for parsing.
An OptionParser will throw exceptions if the parse fails, an AutoOptionParser
will instead generate a help message from the options and then terminate.
Options can be optional/required or positional. They can be given an
arbitrary number of names, typically a short and long, and can specify
a minimum/maximum argument count and optional predicate matching.


Standard usage:

```java
// an AutoOptionParser automatically includes -h and --help as options
AutoOptionParser parser = new AutoOptionParser("Example");
// options can be added via a fluent interface
parser.addOption(Option.of("-f", "--full-name"));
parser.addOption(Option.of("-t", "--other-full-name"));
// there are two types of options, Option and Positional
parser.addOption(Positional.of("positional"));
// the String[] args array should be passed to the parser
// an OptionSet contains the parsed results
OptionSet optionSet = parser.parse(args);
// you can check if an option exists with contains(option)
assert optionSet.contains("-f");
// any name can be used
assert !optionSet.contains("--other-full-name");
// optionSet::get returns an Optional<String>, rather than null if not present
assert optionSet.get("positional").orElse("").equals("argument");
```

```bash
java Example -f argument
```

Argument counts:

```java
// all methods can be chained
// options can be given an arbitrary number of arguments
OptionSet optionSet = new AutoOptionParser()
        .addOption(Option.of("--takes-2").arguments(2))
        .addOption(Option.of("--takes-1-to-3").arguments(1, 3))
        .addOption(Option.of("--takes-0-or-more").arguments("*"))
        .addOption(Option.of("--takes-1-or-more").arguments("+"))
        .addOption(Option.of("--takes-0-or-1").arguments("?"))
        .parse(args);
```

```bash
java Example --takes-2 arg1 arg2 --takes-0-or-more --takes-0-or-1 arg1
```

Required and helper flags:

```java
// options can be marked as required, parsing will fail if they are omitted
// positional arguments are always required
parser.addOption(Option.of("--this-is-required").required());
// options can be marked as helpers, helpers will allow required options to be omitted
parser.addOption(Option.of("--this-is-a-helper").helper());

```

Argument validation:

```java
// options can be given either a predicate or a list of values to match
AutoOptionParser parser = new AutoOptionParser()
        .addOption(Positional.of("longer-than-3").matches(x -> x.length() > 3))
        .addOption(Positional.of("a-b-c").matches("a", "b", "c"));
```

Auto help messages:

```java
// parsers can auto generate help and usage messages from given argument names and descriptions
// AutoOptionParsers will add help options as "-h" and "--help" by default
AutoOptionParser parser = new AutoOptionParser("Example");
parser.addOption(Option.of("-f", "--from").arguments(1, "FROM_FILE")
        .description("File to read from"));
parser.addOption(Option.of("-t", "--to").arguments(1, "TO_FILE")
        .description("File to write to"));
parser.addOption(Option.of("-d", "--debug").arguments(1, "DEBUG_LEVEL")
        .matches("0", "1", "2")
        .description("Set debug level, must be 0, 1 or 2"));
parser.addOption(Option.of("-v", "--verbose")
        .description("Enable verbose mode"));
OptionSet optionSet = parser.parse(args);
```

An incorrect usage with an AutoOptionParser will print usage information (bad debug value):

```bash
java Example -f filename -t filename -d 3
usage: Example [-h] [-v] [-t TO_FILE] [-f FROM_FILE] [-d DEBUG_LEVEL]
Illegal argument for option -d: 3

Run with -h or --help for more information
```

Running with the -h option will print the auto generated help information:

```bash
java Example --help
usage: Example [-f FROM_FILE] [-v] [-d DEBUG_LEVEL] [-h] [-t TO_FILE]

optional arguments:
 -f FROM_FILE, --from FROM_FILE
        File to read from
 -v , --verbose
        Enable verbose mode
 -d DEBUG_LEVEL, --debug DEBUG_LEVEL
        Set debug level, must be 0, 1 or 2
 -h , --help
        Print this message and exit
 -t TO_FILE, --to TO_FILE
        File to write to
```

These help messages can be accessed with OptionParser::getHelpText() and OptionParser::getUsageText()

If you would prefer to implement custom error handling, A standard OptionParser will throw one of ArgumentCountException, BadArgumentException, RequiredOptionException or UnknownOptionException.
These can be caught and handled as desired and each carries relevant information about the error.
See javadocs for more info.

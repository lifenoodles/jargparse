package org.lifenoodles.jargparse;

import org.lifenoodles.jargparse.parsers.OptionParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Donagh Hatton
 *         created on 7/22/14.
 */

abstract class OptionValidator {
    private final String description;
    private final List<String> names;
    private final OptionParser optionParser;
    private final Predicate<String> predicate;
    private final List<String> prefixes;
    private final List<String> argumentLabels;

    public OptionValidator(final List<String> names,
            final String description,
            final OptionParser optionParser,
            final Predicate<String> predicate,
            final List<String> prefixes,
            final List<String> argumentLabels) {
        this.names = new ArrayList<>(names);
        this.description = description;
        this.optionParser = optionParser;
        this.predicate = predicate;
        this.prefixes = new ArrayList<>(prefixes);
        this.argumentLabels = new ArrayList<>(argumentLabels);
        if (this.argumentLabels.isEmpty()) {
            this.argumentLabels.add(stripPrefix(getName()));
        }
    }

    public String getName() {
        return names.get(0);
    }

    /**
     * @return return the full list of names of this option
     */
    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    /**
     * @return the description of this option
     */
    public String getDescription() {
        return description;
    }

    public String helpSummary() {
        return String.format("%s %s", getName(),
                optionParser.helpSummary(argumentLabels)).trim();
    }

    public boolean isArgumentListLegal(final List<String> arguments) {
        return extractArguments(arguments).stream().allMatch(predicate);
    }

    public boolean isArgumentCountCorrect(final List<String> arguments) {
        return optionParser.isCountCorrect(arguments);
    }

    public List<String> extractArguments(final List<String> arguments) {
        return optionParser.extractArguments(arguments);
    }

    public List<String> restOfArguments(final List<String> arguments) {
        return optionParser.restOfArguments(arguments);
    }

    public int expectedOptionCount() {
        return optionParser.expectedOptionCount();
    }

    public List<String> getBadArguments(List<String> options) {
        return optionParser.extractArguments(options).stream()
                .filter(predicate.negate()).collect(Collectors.toList());
    }

    public boolean isOption(String option) {
        return prefixes.stream().anyMatch(option::startsWith);
    }

    private String stripPrefix(final String name) {
        List<String> orderedPrefixes = prefixes;
        orderedPrefixes.sort((x, y) -> y.length() - x.length());
        for (String prefix : orderedPrefixes) {
            if (name.startsWith(prefix)) {
                return name.replaceFirst(prefix, "");
            }
        }
        return name;
    }

    protected List<String> getArgumentLabels() {
        return new ArrayList<>(argumentLabels);
    }

    protected OptionParser getOptionParser() {
        return optionParser;
    }
}

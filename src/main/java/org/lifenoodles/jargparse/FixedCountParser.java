package org.lifenoodles.jargparse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Donagh Hatton
 *         created on 7/30/14.
 */
public class FixedCountParser implements OptionParser {
    private final int argumentCount;

    public FixedCountParser(final int argumentCount) {
        this.argumentCount = argumentCount;
    }

    @Override
    public boolean isCountCorrect(final List<String> arguments) {
        return Utility.argumentCount(arguments) == argumentCount;
    }

    @Override
    public int expectedOptionCount() {
        return argumentCount;
    }

    @Override
    public List<String> extractArguments(final List<String> arguments) {
        return arguments.stream().limit(argumentCount)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> restOfArguments(final List<String> arguments) {
        final List<String> rest = new ArrayList<>();
        for (int i = argumentCount; i < arguments.size(); ++i) {
            rest.add(arguments.get(i));
        }
        return rest;
    }

    @Override
    public String helpSummary(final List<String> argumentLabels) {
        assert (!argumentLabels.isEmpty());
        StringBuilder builder = new StringBuilder();
        List<String> labelList = new ArrayList<>(argumentLabels);
        while (labelList.size() < expectedOptionCount()) {
            labelList.add(labelList.get(0));
        }
        for (int i = 0; i < expectedOptionCount(); ++i) {
            builder.append("%s ");
        }
        return String.format(builder.toString(),
                labelList.stream().limit(expectedOptionCount())
                        .toArray()).trim();
    }
}

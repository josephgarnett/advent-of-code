package com.mtab.adventofcode;

import com.mtab.adventofcode.year2022.Challenge2022;
import com.mtab.adventofcode.year2023.Challenge2023;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AdventOfCode {
    private static final Options OPTIONS = new Options()
            .addRequiredOption("y", "year", true, "Advent of code year");
    public static void main(final String[] args) throws Exception {
        final CommandLine cmd = new DefaultParser()
                .parse(OPTIONS, args);

        if (cmd.hasOption("y")) {
            final String year = cmd.getOptionValue("y");

            switch (year) {
                case "2023": {
                    Challenge2023.main(args);
                    break;
                }
                case "2022": {
                    Challenge2022.main(args);
                    break;
                }
            }
        } else {
            throw new RuntimeException();
        }
    }
}

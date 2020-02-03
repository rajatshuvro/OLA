package com.ola;

import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import com.ola.parsers.FlatObjectParser;
import com.ola.parsers.ParserUtilities;
import com.ola.utilities.PrintUtilities;
import org.apache.commons.cli.*;

import java.util.ArrayList;

public class Filter {
    private static String commandSyntax = "filter  (-v) (-g [genre]) (-l [reading level])";
    public static void Run(String[] args, BookDb bookDb) {
        Options options = new Options();
        Option verboseOption = new Option("v", "verbose", false, "Optional: Report verbose results");
        verboseOption.setRequired(false);
        options.addOption(verboseOption);

        Option genreOption = new Option("g", "genre", true, "Optional: genre(s). Multiple genre may be specified as genre1/.../genre4");
        genreOption.setRequired(false);
        options.addOption(genreOption);

        Option levelOption = new Option("l", "level", true, "Optional: reading level");
        levelOption.setRequired(false);
        options.addOption(levelOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntax, options);
            return;
        }
        try {
            cmd = parser.parse(options, args);
            var verbose = cmd.hasOption('v');
            var genre = cmd.hasOption('g')? cmd.getOptionValue('g'): null;
            var level = cmd.hasOption('l')? Integer.parseInt(cmd.getOptionValue('l')): -1;

            var searchResults = bookDb.Filter(genre, level);
            System.out.println(SummarizeSearch(searchResults));

            if(!verbose) return;
            for (Book book: searchResults) {
                PrintUtilities.PrintDelimiterLine(FlatObjectParser.RecordSeparator);
                System.out.println(book.toString());
            }
            System.out.println(FlatObjectParser.RecordSeparator);
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntax, options);
        }
    }

    private static String SummarizeSearch(ArrayList<Book> searchResults) {
        return searchResults.size() + " matches found";
    }
}

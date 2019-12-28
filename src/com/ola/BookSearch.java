package com.ola;

import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import com.ola.parsers.ParserUtilities;
import org.apache.commons.cli.*;

import java.util.ArrayList;

public class BookSearch {
    private static String commandSyntax = "find  (-v) (-g [genre]) (-l [reading level]) (-t [title]) (-a [author])";
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

        Option authorOption = new Option("a", "author", true, "Optional: Author name");
        authorOption.setRequired(false);
        options.addOption(authorOption);

        Option titleOption = new Option("t", "title", true, "Optional: Title");
        titleOption.setRequired(false);
        options.addOption(titleOption);

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
            var author = cmd.hasOption('a')? cmd.getOptionValue('a'): null;
            var title = cmd.hasOption('t')? cmd.getOptionValue('t'): null;

            var searchResults = bookDb.Search(genre, level, author, title);
            System.out.println(SummarizeSearch(searchResults));

            if(!verbose) return;
            for (Book book: searchResults) {
                System.out.println(ParserUtilities.GetBookRecordDelimiter());
                System.out.println(book.toString());
            }
            System.out.println(ParserUtilities.GetBookRecordDelimiter());
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntax, options);
        }
    }

    private static String SummarizeSearch(ArrayList<Book> searchResults) {
        return searchResults.size() + " books matched the search criteria";
    }
}

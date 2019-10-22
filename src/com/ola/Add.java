package com.ola;
import org.apache.commons.cli.*;
import static java.lang.Integer.valueOf;


public class Add {
    public static void Run(String[] args){
        Options options = new Options();

        Option bookId = new Option("i", "isbn", true, "book id (isbn)");
        bookId.setRequired(true);
        options.addOption(bookId);

        Option genre = new Option("g", "gen", true, "book genre (FIC, NFI, SOC, SCI)");
        bookId.setRequired(true);
        options.addOption(genre);

        Option readingLevel = new Option("l", "lev", true, "Reading level (1 to 5)");
        bookId.setRequired(true);
        options.addOption(readingLevel);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp("add  -i {isbn number} -g {genre} -l {reading level}", options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            AddNewBook(cmd);
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("add  -i {isbn number} -g {genre} -l {reading level}", options);

        }
    }

    private static void AddNewBook(CommandLine cmd) {
        String isbn = cmd.getOptionValue("isbn");
        String genre =  cmd.getOptionValue("gen");
        Integer readingLevel = valueOf(cmd.getOptionValue("lev"));
        System.out.printf("Adding new book: isbn: %s, genre: %s, reading level: %d \n", isbn, genre, readingLevel);
    }
}

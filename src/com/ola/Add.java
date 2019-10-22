package com.ola;
import org.apache.commons.cli.*;
import static java.lang.Integer.valueOf;


public class Add {
    private static String commandSyntex = "add  -i {string} -g {string} -l {integer} [-a {string}] [-t {string} -y {integer}]";
    public static void Run(String[] args){
        Options options = new Options();

        Option bookId = new Option("i", "isbn", true, "book id (isbn)");
        bookId.setRequired(true);
        options.addOption(bookId);

        Option genre = new Option("g", "genre", true, "book genre (FIC, NFI, SOC, SCI)");
        bookId.setRequired(true);
        options.addOption(genre);

        Option readingLevel = new Option("l", "level", true, "Reading level (1 to 5)");
        bookId.setRequired(true);
        options.addOption(readingLevel);

        Option author = new Option("a", "author", true, "Author Name (optional)");
        options.addOption(author);

        Option title = new Option("t", "title", true, "Title of the book (optional)");
        options.addOption(title);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            AddNewBook(cmd);
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);

        }
    }

    private static void AddNewBook(CommandLine cmd) {
        String isbn = cmd.getOptionValue("isbn");
        String genre =  cmd.getOptionValue("genre");
        Integer readingLevel = valueOf(cmd.getOptionValue("level"));
        System.out.printf("Adding new book: isbn: %s, genre: %s, reading level: %d \n", isbn, genre, readingLevel);
    }
}

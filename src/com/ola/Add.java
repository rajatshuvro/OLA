package com.ola;
import org.apache.commons.cli.*;
import static java.lang.Integer.valueOf;


public class Add {
    private static String commandSyntex = "add  -i c:\\path\\to\\file\\newBooks.tsv";
    public static void Run(String[] args){
        Options options = new Options();

        Option bookId = new Option("i", "in", true, "input TSV with book details");
        bookId.setRequired(true);
        options.addOption(bookId);

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

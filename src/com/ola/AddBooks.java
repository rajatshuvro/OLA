package com.ola;
import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import com.ola.parsers.BookParser;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class AddBooks {
    private static String commandSyntex = "add  -i [path to new books dat file]";
    public static void Run(String[] args, BookDb bookDb){
        Options options = new Options();

        Option bookId = new Option("i", "in", true, "input file with book details");
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
            var filePath = cmd.getOptionValue("in");
            InputStream stream = new FileInputStream(filePath);
            var bookParser = new BookParser(stream);
            var count = AddNewBook(bookParser.GetBooks(), bookDb);

            System.out.println("Number of new books added: "+count);
        }
        catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }

    public static int AddNewBook(ArrayList<Book> books, BookDb bookDb) throws IOException {
        for (Book book: books) {
            var displayId = bookDb.AddNew(book);
            System.out.println("New book added: "+displayId);
        }
        return books.size();
    }
}

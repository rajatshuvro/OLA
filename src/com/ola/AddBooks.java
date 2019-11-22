package com.ola;
import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import com.ola.parsers.BookParser;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;


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
            AddNewBook(bookParser.GetBooks(), bookDb);
        }
        catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }

    public static void AddNewBook(ArrayList<Book> books, BookDb bookDb) throws IOException {
        for (Book book: books) {
            var copyNum = bookDb.GetLatestCopyNumber(book.Isbn) +1;
            Date date = new Date(System.currentTimeMillis());

            var newBook = new Book(book.Isbn, book.Author, book.Title,
                    book.Publisher,book.Year, book.PageCount, book.Price,
                    book.Genre, book.ReadingLevel, copyNum, date, null );

            if(copyNum > 1) {
                if(bookDb.ValidateDetails(newBook) == false)
                {
                    System.out.println("WARNING!! Book details mismatch found for Title:"+book.Title);
                    System.out.println("Either the Genre or the Reading level does not match with those of existing copies.");
                    continue;
                }
            }
            bookDb.Add(newBook);
            System.out.println("Adding new book: "+ newBook.GetId());
        }
    }
}

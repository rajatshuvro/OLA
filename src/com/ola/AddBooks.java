package com.ola;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class AddBooks {
    private static String commandSyntex = "add  -i c:\\path\\to\\file\\newBooks.tsv";
    public static void Run(String[] args, BookDb bookDb){
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
            var filePath = cmd.getOptionValue("in");
            InputStream stream = new FileInputStream(filePath);
            AddNewBook(stream, bookDb);
        }
        catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }

    public static void AddNewBook(InputStream stream, BookDb bookDb) throws IOException {
        var parser = new BookTsvParser(stream);
        for (Book book: parser.GetBooks()) {
            var copyNum = bookDb.GetLatestCopyNumber(book.Isbn) +1;
            var newBook = new Book(book.Isbn, book.Author, book.Title,
                    book.Publisher,book.Year, book.PageCount, book.Price,
                    book.Genre, book.ReadingLevel, copyNum);
            bookDb.Add(newBook);
            System.out.println("Adding new book: "+ newBook.GetId());
        }
    }
}

package com.ola;

import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import org.apache.commons.cli.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LabelPrinter {
    private static String commandSyntex = "label  -o [output file path]";

    public static void Run(String[] args, BookDb bookDb){
        Options options = new Options();

        Option outOption = new Option("o", "out", true, "output file path");
        outOption.setRequired(true);
        options.addOption(outOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter  = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);

            var outFilePath  = cmd.getOptionValue("out");
            var outputStream = new FileOutputStream(outFilePath);
            var writer       = new OutputStreamWriter(outputStream);
            WriteLabels(bookDb.GetAllBooks(), writer);
            System.out.println("Printed out labels to output file.");
            writer.close();
        }
        catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }

    private static void WriteLabels(Iterable<Book> books, OutputStreamWriter writer) throws IOException {
        for(var book: books){
            writer.write(book.Title+'\n'+book.GetUserFriendlyId()+"\n\n");
        }
    }
}

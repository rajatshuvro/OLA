package com.ola;

import com.ola.dataStructures.Book;
import com.ola.parsers.LegacyBookParser;
import com.ola.parsers.ParserUtilities;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;

public class LegacyImporter {
    private static String commandSyntex = "add  -i [path to legacy TSV] -o [path of new book dat file]";

    public static void Run(String[] args) {
        Options options = new Options();

        Option legacyTsv = new Option("i", "in", true, "input legacy TSV file");
        legacyTsv.setRequired(true);
        options.addOption(legacyTsv);

        Option bookDat = new Option("o", "out", true, "path of new book dat file");
        bookDat.setRequired(true);
        options.addOption(bookDat);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            var inFilePath = cmd.getOptionValue("in");
            var inputStream = new FileInputStream(inFilePath);
            var bookParser = new LegacyBookParser(inputStream);

            var outFilePath = cmd.getOptionValue("out");
            var outputStream = new FileOutputStream(outFilePath);
            var writer = new OutputStreamWriter(outputStream);
            ConvertLegacyRecords(bookParser.GetBooks(), writer);

            bookParser.Close();
        }
        catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }

    private static void ConvertLegacyRecords(ArrayList<Book> books, OutputStreamWriter writer) throws IOException {
        writer.write(ParserUtilities.GetBookFileHeader());
        writer.write(ParserUtilities.GetBookRecordDelimiter());

        for (Book book: books) {
            writer.write(book.toString());
            writer.write(ParserUtilities.GetBookRecordDelimiter());
        }

        writer.close();
    }
}

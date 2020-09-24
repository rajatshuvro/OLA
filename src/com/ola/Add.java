package com.ola;
import com.ola.parsers.BookCsvParser;
import com.ola.parsers.CheckoutCsvParser;
import com.ola.parsers.UserCsvParser;
import com.ola.utilities.FileUtilities;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Add {
    private static String commandSyntex = "add  -b [books CSV file] -u [users CSV file] -c [checkout CSV file]";
    public static void Run(String[] args, DataProvider dataProvider){
        Options options = new Options();

        Option bookOption = new Option("b", "book", true, "file with book details");
        bookOption.setRequired(false);
        options.addOption(bookOption);

        Option userOption = new Option("u", "user", true, "file with user details");
        userOption.setRequired(false);
        options.addOption(userOption);

        Option checkoutOption = new Option("c", "co", true, "file with checkout details");
        checkoutOption.setRequired(false);
        options.addOption(checkoutOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("book")){
                var filePath = cmd.getOptionValue("book");
                if(!FileUtilities.Exists(filePath)){
                    System.out.println("Specified file does not exist: "+filePath);
                }

                InputStream stream = new FileInputStream(filePath);
                var bookParser = new BookCsvParser(stream);
                var count = dataProvider.AddBooks(bookParser.GetBooks());
                System.out.println("Number of new books added: "+count);
            }
            if(cmd.hasOption("user")){
                var filePath = cmd.getOptionValue("user");
                if(!FileUtilities.Exists(filePath)){
                    System.out.println("Specified file does not exist: "+filePath);
                }
                InputStream stream = new FileInputStream(filePath);
                var userParser = new UserCsvParser(stream);
                var count = dataProvider.AddUsers(userParser.GetUsers());
                System.out.println("Number of new users added: "+count);
            }
            if(cmd.hasOption("co")){
                var filePath = cmd.getOptionValue("co");
                if(!FileUtilities.Exists(filePath)){
                    System.out.println("Specified file does not exist: "+filePath);
                }
                InputStream stream = new FileInputStream(filePath);
                var userParser = new CheckoutCsvParser(stream);
                var count = dataProvider.AddCheckouts(userParser.GetCheckouts());
                System.out.println("Number of successful checkouts: "+count);
            }
        }
        catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }
}

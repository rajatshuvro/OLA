package com.ola;

import com.ola.databases.TransactionDb;
import com.ola.parsers.CheckoutCsvParser;
import com.ola.utilities.FileUtilities;
import com.ola.utilities.PrintUtilities;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CheckOut {
    private static String commandSyntax = "co  -b [book id] -u [user id] -f [csv file path]";
    public static void Run(String[] args, TransactionDb transactionDb){
        Options options = new Options();

        Option bookIdOption = new Option("b", "book", true, "book id");
        bookIdOption.setRequired(false);
        options.addOption(bookIdOption);

        Option userIdOption = new Option("u", "user", true, "user id");
        userIdOption.setRequired(false);
        options.addOption(userIdOption);

        Option checkoutOption = new Option("f", "file", true, "file with checkout details");
        checkoutOption.setRequired(false);
        options.addOption(checkoutOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntax, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption('b') && cmd.hasOption('u')){
                var bookId = cmd.getOptionValue('b');
                var userId = Integer.parseInt(cmd.getOptionValue('u'));
                if(transactionDb.Checkout(bookId, userId))
                {
                    PrintUtilities.PrintSuccessLine(bookId +" has been checked out by "+ userId);
                }
                else PrintUtilities.PrintWarningLine("Checkout attempt was unsuccessful!!");
            }
            if(cmd.hasOption('f')){
                var filePath = cmd.getOptionValue("co");
                if(!FileUtilities.Exists(filePath)){
                    System.out.println("Specified file does not exist: "+filePath);
                }
                InputStream stream = new FileInputStream(filePath);
                var userParser = new CheckoutCsvParser(stream);
                var count = transactionDb.AddCheckouts(userParser.GetCheckouts());
                System.out.println("Number of successful checkouts: "+count);
            }

        }
        catch (ParseException | IOException e) {
            PrintUtilities.PrintLine(e.getMessage());
            formatter.printHelp(commandSyntax, options);
        }
    }
}

package com.ola;

import com.ola.databases.BookDb;
import com.ola.databases.CheckoutDb;
import com.ola.databases.TransactionDb;
import com.ola.databases.UserDb;
import com.ola.parsers.CheckoutCsvParser;
import com.ola.utilities.FileUtilities;
import com.ola.utilities.PrintUtilities;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CheckOut {
    private static String commandSyntax = "co -f [csv file path]";
    public static void Run(String[] args, CheckoutDb checkoutDb, BookDb bookDb, UserDb userDb){
        Options options = new Options();

        Option checkoutOption = new Option("f", "file", true, "file with checkout details");
        checkoutOption.setRequired(true);
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
            if(cmd.hasOption('f')){
                var filePath = cmd.getOptionValue("file");
                if(!FileUtilities.Exists(filePath)){
                    System.out.println("Specified file does not exist: "+filePath);
                }
                InputStream stream = new FileInputStream(filePath);
                var csvParser = new CheckoutCsvParser(stream);
                var count = checkoutDb.TryAddRange(csvParser.GetCheckouts(),bookDb, userDb);
                System.out.println("Number of successful checkouts: "+count);
            }

        }
        catch (ParseException | IOException e) {
            PrintUtilities.PrintLine(e.getMessage());
            formatter.printHelp(commandSyntax, options);
        }
    }
}

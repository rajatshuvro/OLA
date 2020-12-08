package com.ola;

import com.ola.dataStructures.Checkout;
import com.ola.databases.*;
import com.ola.parsers.CheckoutCsvParser;
import com.ola.utilities.FileUtilities;
import com.ola.utilities.PrintUtilities;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CheckoutMain {
    private static String commandSyntax = "co -f [csv file path]";
    public static void Run(String[] args, DataProvider dataProvider){
        Options options = new Options();

        Option filePathOption = new Option("f", "file", true, "file with checkout details");
        filePathOption.setRequired(true);
        options.addOption(filePathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntax, options);
            return;
        }

        var checkoutDb = dataProvider.CheckoutDb;
        var userDb = dataProvider.UserDb;
        var transactionDb = dataProvider.TransactionDb;
        try {
            cmd = parser.parse(options, args);
            if(cmd.hasOption('f')){
                var filePath = cmd.getOptionValue("file");
                if(!FileUtilities.Exists(filePath)){
                    System.out.println("Specified file does not exist: "+filePath);
                    return;
                }
                InputStream stream = new FileInputStream(filePath);
                var csvParser = new CheckoutCsvParser(stream);
                var checkouts = new ArrayList<Checkout>();
                // resolve unknown users
                for (var checkout : csvParser.GetCheckouts()) {
                    var resolvedUser = userDb.ResolveUser(checkout.UserId, checkout.Email);
                    if (resolvedUser == null){
                        PrintUtilities.PrintErrorLine("Failed to resolve user, skipping checkout for book:"+checkout.BookId);
                        continue;
                    }
                    checkouts.add(new Checkout(checkout.BookId, resolvedUser.Id, resolvedUser.Email, checkout.CheckoutDate, checkout.DueDate));
                }

                var count = checkoutDb.TryAddRange(checkouts);
                System.out.println("Number of successful checkouts: "+count);

                //adding transactions for future records
                count = transactionDb.AddCheckouts(checkouts);
                System.out.println("Number of successful transactions: "+count);
            }

        }
        catch (ParseException | IOException e) {
            PrintUtilities.PrintLine(e.getMessage());
            formatter.printHelp(commandSyntax, options);
        }
    }
}

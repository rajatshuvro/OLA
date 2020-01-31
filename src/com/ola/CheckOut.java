package com.ola;

import com.ola.dataStructures.Transaction;
import com.ola.databases.TransactionDb;
import com.ola.utilities.TimeUtilities;
import org.apache.commons.cli.*;

import java.io.IOException;

public class CheckOut {
    private static String commandSyntax = "co  -b [book id] -u [user id]";
    public static void Run(String[] args, TransactionDb transactionDb){
        Options options = new Options();

        Option bookIdOption = new Option("b", "book", true, "book id");
        bookIdOption.setRequired(true);
        options.addOption(bookIdOption);

        Option userIdOption = new Option("u", "user", true, "user id");
        userIdOption.setRequired(true);
        options.addOption(userIdOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntax, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            var bookId = cmd.getOptionValue('b');
            var userId = Integer.parseInt(cmd.getOptionValue('u'));
            var date = TimeUtilities.GetCurrentTime();
            if(transactionDb.Add(new Transaction(bookId, userId, date, Transaction.CheckoutTag)))
            {
                System.out.println(bookId +" has been checked out by "+ userId);
                System.out.print("Rebuilding transaction search index...");
                transactionDb.BuildSearchIndex();
                System.out.println("done");
            }
            else System.out.println("Checkout attempt was unsuccessful!!");

        }
        catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntax, options);
        }
    }
}

package com.ola;

import com.ola.dataStructures.Transaction;
import com.ola.databases.TransactionDb;
import com.ola.utilities.TimeUtilities;
import org.apache.commons.cli.*;

public class CheckOut {
    private static String commandSyntex = "co  -b [book id] -u [user id]";
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
            formatter.printHelp(commandSyntex, options);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            var bookId = cmd.getOptionValue('b');
            var userId = Integer.parseInt(cmd.getOptionValue('u'));
            var date = TimeUtilities.GetCurrentTime();
            transactionDb.Add(new Transaction(bookId, userId, date, Transaction.CheckoutTag));
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }
}

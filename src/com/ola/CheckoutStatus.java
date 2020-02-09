package com.ola;

import com.ola.dataStructures.Transaction;
import com.ola.parsers.ParserUtilities;
import com.ola.utilities.PrintUtilities;
import org.apache.commons.cli.*;

public class CheckoutStatus {
    private static String commandSyntax = "co  (-u [user id]) OR (-b [book id]) OR (-i [isbn])";
    public static void Run(String[] args, DataProvider dataProvider){
        Options options = new Options();

        Option userIdOption = new Option("u", "user", true, "Optional: user id");
        userIdOption.setRequired(false);
        options.addOption(userIdOption);

        Option bookIdOption = new Option("b", "book", true, "Optional: book id");
        bookIdOption.setRequired(false);
        options.addOption(bookIdOption);

        Option isbnOption = new Option("i", "isbn", true, "Optional: isbn");
        isbnOption.setRequired(false);
        options.addOption(isbnOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            for(var record: dataProvider.TransactionDb.GetPendingCheckouts())
            {
                PrintTransaction(dataProvider, record);
            }
            return;
        }

        try {
            cmd = parser.parse(options, args);
            if(cmd.hasOption('u') ){
                var userId = Integer.parseInt(cmd.getOptionValue('u'));
                if(dataProvider.UserDb.GetUser(userId) == null)
                    PrintUtilities.PrintErrorLine("Unknown user id: "+userId);

                else PrintUserCheckouts(dataProvider, userId);
                return;

            }
            if(cmd.hasOption('b')){
                var bookId = cmd.getOptionValue('b');
                var latestTransaction = dataProvider.TransactionDb.GetLatest(bookId);;
                if (latestTransaction!=null)
                    PrintTransaction(dataProvider, latestTransaction);
                else PrintUtilities.PrintLine("No transaction found for book id:"+bookId);
                return;
            }
            if(cmd.hasOption('i')){
                var isbn = ParserUtilities.ParseIsbn(cmd.getOptionValue('i'));
                for(var book: dataProvider.BookDb.GetBooks(isbn)){
                    var bookId = book.GetId();
                    var latestTransaction = dataProvider.TransactionDb.GetLatest(bookId);
                    if (latestTransaction != null && latestTransaction.Type.equals(Transaction.CheckoutTag))
                    {
                        PrintUtilities.PrintWarningLine("Title: "+ book.Title + " [id:" +bookId + "] is checked out");
                    }
                    else PrintUtilities.PrintSuccessLine("Title: "+ book.Title + " [id:" +bookId + "] in stock");
                }
            }
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntax, options);
        }
    }

    private static void PrintUserCheckouts(DataProvider dataProvider, int userId) {
        PrintUtilities.PrintLine("Pending checkouts for: "+ dataProvider.UserDb.GetUserName(userId));
        var pendingCheckouts = dataProvider.GetPendingCheckouts(userId);
        if (pendingCheckouts == null) {
            PrintUtilities.PrintLine("No checkouts found for user id:"+userId);
        }

        for(var checkout: pendingCheckouts){
            PrintTransaction(dataProvider, checkout);
        }
    }



    private static void PrintTransaction(DataProvider dataProvider, Transaction transaction){
        var recordString = dataProvider.GetTransactionString(transaction);
        if(recordString.contains("Due:")) PrintUtilities.PrintWarningLine(recordString);
        else PrintUtilities.PrintSuccessLine(recordString);
    }


}

package com.ola;

import com.ola.dataStructures.Transaction;
import com.ola.parsers.FlatObjectParser;
import com.ola.utilities.PrintUtilities;
import com.ola.utilities.TimeUtilities;
import org.apache.commons.cli.*;

public class CheckoutStatus {
    private static String commandSyntax = "stat  (-u [user id]) (-b [book id])";
    public static void Run(String[] args, DataProvider dataProvider){
        Options options = new Options();

        Option userIdOption = new Option("u", "user", true, "Optional: user id");
        userIdOption.setRequired(false);
        options.addOption(userIdOption);

        Option bookIdOption = new Option("b", "book", true, "Optional: book id");
        bookIdOption.setRequired(false);
        options.addOption(bookIdOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            System.out.print(GetAllCheckouts(dataProvider));
            return;
        }

        try {
            cmd = parser.parse(options, args);
            if(cmd.hasOption('u') ){
                var userId = Integer.parseInt(cmd.getOptionValue('u'));
                var userRecords = GetUserCheckouts(dataProvider, userId);
                if (userRecords != null)  PrintUtilities.PrintLine(userRecords);
                else PrintUtilities.PrintLine("No checkouts found for user id:"+userId);
            }
            if(cmd.hasOption('b')){
                var bookId = cmd.getOptionValue('b');
                var latestTransaction = GetLatestBookTransaction(dataProvider, bookId);
                if (latestTransaction!=null)
                    PrintUtilities.PrintLine(latestTransaction);
                else PrintUtilities.PrintLine("No transaction found for book id:"+bookId);
            }
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntax, options);
        }
    }

     public static String GetLatestBookTransaction(DataProvider dataProvider, String bookId) {
        var transactionDb = dataProvider.TransactionDb;
        var userDb = dataProvider.UserDb;
        var bookDb = dataProvider.BookDb;
        var record = transactionDb.GetLatest(bookId);
        if (record== null) return null;

        var sb = new StringBuilder();
        sb.append("Transaction status for book: " +bookId+'\n');
        sb.append(FlatObjectParser.RecordSeparator+'\n');
        var userName = userDb.GetUserName(record.UserId);
        var bookTitle = bookDb.GetTitle(bookId);
        AppendTransactionDetails(record, sb, userName, bookTitle);
        return sb.toString();
    }

    private static void AppendTransactionDetails(Transaction record, StringBuilder sb, String userName, String bookTitle) {
        sb.append("User:   " + userName + " [id:" + record.UserId + "]\n");
        sb.append("Title:  " + bookTitle + " [id:" + record.BookId + "]\n");
        sb.append("Date:   " + TimeUtilities.ToString(record.Date) + "\n");
        sb.append(FlatObjectParser.RecordSeparator + '\n');
    }

    public static String GetUserCheckouts(DataProvider dataProvider, int userId) {
        var transactionDb = dataProvider.TransactionDb;
        var userDb = dataProvider.UserDb;
        var bookDb = dataProvider.BookDb;
        var userName = userDb.GetUserName(userId);
        if(userName==null) return null;
        var sb = new StringBuilder();
        sb.append("Checkout status for: " +userName+'\n');
        sb.append(FlatObjectParser.RecordSeparator+'\n');
        for (Transaction record: transactionDb.GetPendingCheckouts()) {
            if(userId != record.UserId) continue;
            var bookTitle = bookDb.GetTitle(record.BookId);
            AppendTransactionDetails(record, sb, userName, bookTitle);
        }
        return sb.toString();
    }

    public static String GetAllCheckouts(DataProvider dataProvider) {
        var transactionDb = dataProvider.TransactionDb;
        var userDb = dataProvider.UserDb;
        var bookDb = dataProvider.BookDb;
        var sb = new StringBuilder();
        sb.append("Checkout status\n");
        sb.append(FlatObjectParser.RecordSeparator+'\n');
        for (Transaction record: transactionDb.GetPendingCheckouts()) {
            var userName = userDb.GetUserName(record.UserId);
            var bookTitle = bookDb.GetTitle(record.BookId);
            AppendTransactionDetails(record, sb, userName, bookTitle);
        }
        return sb.toString();
    }
}

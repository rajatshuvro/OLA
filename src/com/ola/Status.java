package com.ola;

import com.ola.dataStructures.Transaction;
import com.ola.utilities.TimeUtilities;
import org.apache.commons.cli.*;

public class Status {
    private static String commandSyntax = "stat  (-u [user id])";
    public static void Run(String[] args, DataProvider dataProvider){
        Options options = new Options();

        Option userIdOption = new Option("u", "user", true, "Optional: user id");
        userIdOption.setRequired(false);
        options.addOption(userIdOption);

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
                System.out.print(GetUserCheckouts(dataProvider, userId));
            }
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntax, options);
        }
    }

    public static String GetUserCheckouts(DataProvider dataProvider, int userId) {
        var transactionDb = dataProvider.TransactionDb;
        var userDb = dataProvider.UserDb;
        var bookDb = dataProvider.BookDb;
        var userName = userDb.GetUserName(userId);
        var sb = new StringBuilder();
        sb.append("Checkout status for: " +userName+"\n");
        sb.append("**********************************************\n");
        for (Transaction record: transactionDb.GetPendingCheckouts()) {
            if(userId != record.UserId) continue;
            var bookTitle = bookDb.GetTitle(record.BookId);
            sb.append("User:          " + userName + " [id:"+ record.UserId+"]\n");
            sb.append("Title:         " + bookTitle + " [id:"+ record.BookId+"]\n");
            sb.append("Borrowed on:   "+ TimeUtilities.ToString(record.Date)+"\n");
            sb.append("**********************************************\n");
        }
        return sb.toString();
    }

    public static String GetAllCheckouts(DataProvider dataProvider) {
        var transactionDb = dataProvider.TransactionDb;
        var userDb = dataProvider.UserDb;
        var bookDb = dataProvider.BookDb;
        var sb = new StringBuilder();
        sb.append("Checkout status\n");
        sb.append("**********************************************\n");
        for (Transaction record: transactionDb.GetPendingCheckouts()) {
            var userName = userDb.GetUserName(record.UserId);
            var bookTitle = bookDb.GetTitle(record.BookId);
            sb.append("User:            " + userName + " [id:"+ record.UserId+"]\n");
            sb.append("Title:           " + bookTitle + " [id:"+ record.BookId+"]\n");
            sb.append("Borrowed on:     "+ TimeUtilities.ToString(record.Date)+"\n");
            sb.append("**********************************************\n");
        }
        return sb.toString();
    }
}

package com.ola;

import com.ola.dataStructures.Transaction;
import com.ola.utilities.TimeUtilities;
import org.apache.commons.cli.*;

public class Status {
    private static String commandSyntex = "status  (-b [book id]) (-u [user id])";
    public static void Run(String[] args, DataProvider dataProvider){
        Options options = new Options();

        Option bookIdOption = new Option("b", "book", true, "book id");
        bookIdOption.setRequired(false);
        options.addOption(bookIdOption);
        Option userIdOption = new Option("u", "user", true, "user id");
        userIdOption.setRequired(false);
        options.addOption(userIdOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            ReportAllCheckouts(dataProvider);
            return;
        }

        try {
            cmd = parser.parse(options, args);
            if(cmd.hasOption('b') ){
                var bookId = cmd.getOptionValue('b');
                ReportBookCheckout(dataProvider, bookId);
            }
            if(cmd.hasOption('u') ){
                var userId = Integer.parseInt(cmd.getOptionValue('u'));
                ReportUserCheckout(dataProvider, userId);
            }
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
        }
    }

    private static void ReportUserCheckout(DataProvider dataProvider, int userId) {
        var transactionDb = dataProvider.TransactionsDb;
        var userDb = dataProvider.UserDb;
        var bookDb = dataProvider.BookDb;
        var userName = userDb.GetUserName(userId);
        System.out.println("Checkout status for: " +userName);
        System.out.println("**********************************************");
        for (Transaction record: transactionDb.GetPendingCheckouts()) {
            if(userId != record.UserId) continue;
            var bookTitle = bookDb.GetTitle(record.BookId);
            System.out.println("User:\t\t\t" + userName + " [id:"+ record.UserId+"]");
            System.out.println("Title:\t\t\t" + bookTitle + " [id:"+ record.BookId+"]");
            System.out.println("Borrowed on:\t"+ TimeUtilities.ToString(record.Date));
            System.out.println("**********************************************");
        }
    }

    private static void ReportBookCheckout(DataProvider dataProvider, String bookId) {
        var transactionDb = dataProvider.TransactionsDb;
        var userDb = dataProvider.UserDb;
        var bookDb = dataProvider.BookDb;
        var bookTitle = bookDb.GetTitle(bookId);
        System.out.println("Checkout status for: "+ bookTitle);
        System.out.println("**********************************************");
        for (Transaction record: transactionDb.GetPendingCheckouts()) {
            if(!bookId.equals(record.BookId)) continue;
            var userName = userDb.GetUserName(record.UserId);
            System.out.println("User:\t\t\t" + userName + " [id:"+ record.UserId+"]");
            System.out.println("Title:\t\t\t" + bookTitle + " [id:"+ record.BookId+"]");
            System.out.println("Borrowed on:\t"+ TimeUtilities.ToString(record.Date));
            System.out.println("**********************************************");
        }
    }

    private static void ReportAllCheckouts(DataProvider dataProvider) {
        var transactionDb = dataProvider.TransactionsDb;
        var userDb = dataProvider.UserDb;
        var bookDb = dataProvider.BookDb;
        System.out.println("Checkout status");
        System.out.println("**********************************************");
        for (Transaction record: transactionDb.GetPendingCheckouts()) {
            var userName = userDb.GetUserName(record.UserId);
            var bookTitle = bookDb.GetTitle(record.BookId);
            System.out.println("User:\t\t\t" + userName + " [id:"+ record.UserId+"]");
            System.out.println("Title:\t\t\t" + bookTitle + " [id:"+ record.BookId+"]");
            System.out.println("Borrowed on:\t"+ TimeUtilities.ToString(record.Date));
            System.out.println("**********************************************");
        }
    }
}

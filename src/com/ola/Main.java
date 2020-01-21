package com.ola;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {
//https://github.com/rajatshuvro/OLA/blob/master/README.md
    public static void main(String[] args) throws Exception{

        DataProvider dataProvider = Initialize(args);
        dataProvider.Load();
        System.out.println("Welcome to OLA (Onkur Library Application");

        boolean quit=false;
        while (!quit){
            PrintMenu();
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            String[] subArgs = command.split("\\s+");

            String subCommand = subArgs[0];
            switch (subCommand){
                case "add-books":
                    AddBooks.Run(subArgs, dataProvider.BookDb);
                    break;
                case "add-user":
                    AddBooks.Run(subArgs, dataProvider.BookDb);
                    break;
                case "co":
                    CheckOut.Run(subArgs, dataProvider.TransactionsDb);
                    break;
                case "ret":
                    Return.Run(subArgs, dataProvider.TransactionsDb);
                    break;
                case "co-stat":
                    Status.Run(subArgs, dataProvider);
                    break;
                case "filter":
                    Filter.Run(subArgs, dataProvider.BookDb);
                    break;
                case "search":
                    Search.Run(subArgs, dataProvider.BookDb);
                    break;
                case "legacy":
                    LegacyImporter.Run(subArgs);
                    break;
                case "quit":
                    quit = true;
                    break;
                default:
                    System.out.println("Type {quit} to exit");
            }

        }
        //save changes to file
        dataProvider.Close();

    }
    private static void PrintMenu() {
        System.out.println("Please choose from the following options:");
        System.out.println("\tadd-books      (add new books)");
        System.out.println("\tadd-user       (add new user via command line)");
        System.out.println("\tco             (checkout book)");
        System.out.println("\tret            (return book)");
        System.out.println("\tco-stat        (checkout status)");
        System.out.println("\tsearch         (search book database)");
        System.out.println("\tfilter         (filter book database by genre, level, etc fields)");
        System.out.println("\tlegacy         (import books from legacy tsv)");
        System.out.println("\tquit           (quit OLA)");
        System.out.println("\t[Type command to get detailed help]");
    }

    private static String commandSyntex = "ola -b [full path to books data file] " +
            "-u [full path to users data file] " +
            "-t [full path to transactions data file]";
    private static DataProvider Initialize(String[] args) {
        Options options = new Options();

        Option bookDbFile = new Option("b", "books", true, "book database file");
        bookDbFile.setRequired(true);
        options.addOption(bookDbFile);

        Option userDbFile = new Option("u", "users", true, "user database file");
        userDbFile.setRequired(true);
        options.addOption(userDbFile);

        Option transactionsFile = new Option("t", "transactions", true, "transactions records file");
        transactionsFile.setRequired(true);
        options.addOption(transactionsFile);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return null;
        }

        try {
            cmd = parser.parse(options, args);
            String bookFileName = cmd.getOptionValue("books");
            String userFileName = cmd.getOptionValue("users");
            String transactionFileName = cmd.getOptionValue("transactions");
            return new DataProvider(new FileInputStream(bookFileName), new FileInputStream(userFileName),
                    new FileInputStream(transactionFileName), new FileOutputStream(transactionFileName,true),
                    new FileOutputStream(bookFileName, true));
        }
        catch (ParseException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
            return null;
        }

    }


}

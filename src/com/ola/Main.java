package com.ola;
import com.ola.utilities.FileUtilities;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {
//https://github.com/rajatshuvro/OLA/blob/master/README.md
    public static void main(String[] args) throws Exception{

        DataProvider dataProvider = Initialize(args);
        if(dataProvider == null) return;
        dataProvider.Load();
        System.out.println("Welcome to OLA (Onkur Library Application");

        boolean quit=false;
        PrintMenu();

        while (!quit){
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            String[] subArgs = command.split("\\s+");

            String subCommand = subArgs[0];
            switch (subCommand){
                case "add":
                    Add.Run(subArgs, dataProvider.BookDb, dataProvider.UserDb);
                    break;
                case "add-user":
                    AddUser.Run(subArgs, dataProvider.UserDb);
                    break;
                case "co":
                    CheckOut.Run(subArgs, dataProvider.TransactionDb);
                    break;
                case "ret":
                    Return.Run(subArgs, dataProvider.TransactionDb);
                    break;
                case "co-stat":
                    CheckoutStatus.Run(subArgs, dataProvider);
                    break;
                case "filter":
                    Filter.Run(subArgs, dataProvider.BookDb);
                    break;
                case "search":
                    Search.Run(subArgs, dataProvider);
                    break;
                case "legacy":
                    LegacyImporter.Run(subArgs);
                    break;
                case "quit":
                    quit = true;
                    break;
                case "help":
                    PrintMenu();
                    break;
                default:
                    System.out.println("Unrecognized command: "+subCommand+"\nType \"help\" for the help menu or \"quit\" to exit");
            }

        }
        //save changes to file
        dataProvider.Close();

    }
    private static void PrintMenu() {
        System.out.println("Please choose from the following options:");
        System.out.println("\tadd            (add new books or users)");
        System.out.println("\tadd-user       (add new user via command line)");
        System.out.println("\tco             (checkout book)");
        System.out.println("\tret            (return book)");
        System.out.println("\tco-stat        (checkout status)");
        System.out.println("\tsearch         (search book database)");
        System.out.println("\tfilter         (filter book database by genre, level, etc fields)");
        System.out.println("\tlegacy         (import books from legacy tsv)");
        System.out.println("\tquit           (quit OLA)");
        System.out.println("\thelp           (print this menu)");
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
            if(!FileUtilities.Exists(bookFileName)){
                System.out.println("Failed to find file: "+bookFileName);
                return null;
            }
            String userFileName = cmd.getOptionValue("users");
            if(!FileUtilities.Exists(userFileName)){
                System.out.println("Failed to find file: "+userFileName);
                return null;
            }
            String transactionFileName = cmd.getOptionValue("transactions");
            if(!FileUtilities.Exists(transactionFileName)){
                System.out.println("Failed to find file: "+transactionFileName);
                return null;
            }
            return new DataProvider(new FileInputStream(bookFileName), new FileInputStream(userFileName),
                    new FileInputStream(transactionFileName), new FileOutputStream(transactionFileName,true),
                    new FileOutputStream(bookFileName, true),
                    new FileOutputStream(userFileName, true));
        }
        catch (ParseException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
            return null;
        }

    }


}

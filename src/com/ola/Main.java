package com.ola;
import com.ola.utilities.FileUtilities;
import com.ola.utilities.PrintUtilities;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
//https://github.com/rajatshuvro/OLA/blob/master/README.md
    public static void main(String[] args) throws Exception{

        DataProvider dataProvider = Initialize(args);
        if(dataProvider == null) return;
        dataProvider.Load();
        PrintUtilities.PrintBanner();
        PrintUtilities.PrintSuccessLine("Welcome to Onkur Library Application");

        boolean quit=false;
        PrintMenu();

        while (!quit){
            PrintUtilities.PrintPrompt();
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            command = command.trim();
            String[] subArgs = command.split("\\s+");

            String subCommand = subArgs[0];
            switch (subCommand){
                case "add":
                    Add.Run(subArgs, dataProvider.BookDb, dataProvider.UserDb, dataProvider.Appender);
                    break;
                case "au":
                case "add-user":
                    AddUser.Run(subArgs, dataProvider.UserDb, dataProvider.Appender);
                    break;
                case "co":
                    CheckOut.Run(subArgs, dataProvider.TransactionDb);
                    break;
                case "ret":
                    Return.Run(subArgs, dataProvider.TransactionDb);
                    break;
                case "cs":
                case "co-stat":
                    CheckoutStatus.Run(subArgs, dataProvider);
                    break;
                case "filter":
                    Filter.Run(subArgs, dataProvider.BookDb);
                    break;
                case "$":
                case "search":
                    Search.Run(subArgs, dataProvider);
                    break;
                case "label":
                    LabelPrinter.Run(subArgs, dataProvider.BookDb);
                    break;
                case "quit":
                    quit = true;
                    break;
                case "":
                    break;//user pressed enter
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
        PrintUtilities.PrintInfoLine("Please choose from the following options:");
        PrintUtilities.PrintInfoLine("\tadd            (add new books or users)");
        PrintUtilities.PrintInfoLine("\tadd-user/au    (add new user via command line)");
        PrintUtilities.PrintInfoLine("\tco             (checkout book)");
        PrintUtilities.PrintInfoLine("\tret            (return book)");
        PrintUtilities.PrintInfoLine("\tco-stat/cs     (checkout status)");
        PrintUtilities.PrintInfoLine("\tsearch/$       (search book and user records)");
        PrintUtilities.PrintInfoLine("\tfilter         (filter book database by genre, level, etc fields)");
        PrintUtilities.PrintInfoLine("\tlabel          (print out book titles and Ids)");
        PrintUtilities.PrintInfoLine("\tquit           (quit OLA)");
        PrintUtilities.PrintInfoLine("\thelp           (print this menu)");
        PrintUtilities.PrintInfoLine("\t[Type command to get detailed help]");
    }

    private static String commandSyntex = "ola -d [full path data directory] ";
    private static String UsersFileName = "Users.fob";
    private static String BooksFileName = "Books.fob";
    private static String TransactionsFileName = "Transactions.fob";

    private static DataProvider Initialize(String[] args) {
        Options options = new Options();

        Option dataDirOption = new Option("d", "dir", true, "data directory path");
        dataDirOption.setRequired(true);
        options.addOption(dataDirOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return null;
        }

        try {
            cmd = parser.parse(options, args);
            String dataDir = cmd.getOptionValue('d');

            String bookFileName = dataDir+ File.separatorChar+BooksFileName;
            if(!FileUtilities.Exists(bookFileName)){
                PrintUtilities.PrintErrorLine("Failed to find file: "+bookFileName);
                return null;
            }
            String userFileName = dataDir+ File.separatorChar+UsersFileName;
            if(!FileUtilities.Exists(userFileName)){
                PrintUtilities.PrintErrorLine("Failed to find file: "+userFileName);
                return null;
            }
            String transactionFileName = dataDir+ File.separatorChar+TransactionsFileName;
            if(!FileUtilities.Exists(transactionFileName)){
                PrintUtilities.PrintErrorLine("Failed to find file: "+transactionFileName);
                return null;
            }
            return new DataProvider(new FileInputStream(bookFileName),
                    new FileInputStream(userFileName),
                    new FileInputStream(transactionFileName),
                    new FileOutputStream(transactionFileName,true),
                    new FileOutputStream(bookFileName, true),
                    new FileOutputStream(userFileName, true));
        }
        catch (ParseException | FileNotFoundException e) {
            PrintUtilities.PrintErrorLine(e.getMessage());
            formatter.printHelp(commandSyntex, options);
            return null;
        }

    }


}

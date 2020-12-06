package com.ola;
import com.ola.dataStructures.Book;
import com.ola.databases.IdDb;
import com.ola.parsers.FlatObjectParser;
import com.ola.utilities.FileUtilities;
import com.ola.utilities.PrintUtilities;
import com.ola.utilities.TimeUtilities;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
//https://github.com/rajatshuvro/OLA/blob/master/README.md
    private static String DataDir;
    public static void main(String[] args) throws Exception{

        DataProvider dataProvider = Initialize(args);
        if(dataProvider == null) return;
        //dataProvider.Load();
        PrintUtilities.PrintBanner();
        PrintUtilities.PrintSuccessLine("Welcome to Onkur Library Application");

        boolean quit=false;
        PrintMenu();

        var logger = GetLogger();
        while (!quit){
            PrintUtilities.PrintPrompt();
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            command = command.trim();
            String[] subArgs = command.split("\\s+");

            logger.write(command);

            String subCommand = subArgs[0];
            switch (subCommand){
                case "add":
                    Add.Run(subArgs, dataProvider);
                    break;
                case "au":
                case "add-user":
                    AddUser.Run(subArgs, dataProvider);
                    break;
                case "co":
                    CheckoutMain.Run(subArgs, dataProvider);
                    break;
                case "ret":
                    var checkoutFilePath =  DataDir+ File.separatorChar+CheckoutsFileName;
                    Return.Run(subArgs, dataProvider, checkoutFilePath);
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
                //    Search.Run(subArgs, dataProvider);
                //    break;
                //case "#":
                //case "find":
                    TokenSearch.Run(subArgs, dataProvider);
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
            logger.write("\tsuccess\n");
            logger.flush();

        }
        //save changes to file
        dataProvider.Close();
        logger.close();
    }

    private static OutputStreamWriter GetLogger() throws FileNotFoundException {
        var time = TimeUtilities.GetCurrentTime().getTime();
        return new OutputStreamWriter(new FileOutputStream(time+".log"));
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
    private static String CheckoutsFileName = "Checkouts.fob";
    private static String IdMapsFileName = "IdMaps.fob";

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
            DataDir = cmd.getOptionValue('d');

            String bookFileName = DataDir+ File.separatorChar+BooksFileName;
            if(!FileUtilities.Exists(bookFileName)){
                PrintUtilities.PrintErrorLine("Failed to find file: "+bookFileName);
                return null;
            }
            String userFileName = DataDir+ File.separatorChar+UsersFileName;
            if(!FileUtilities.Exists(userFileName)){
                PrintUtilities.PrintErrorLine("Failed to find file: "+userFileName);
                return null;
            }
            String transactionFileName = DataDir+ File.separatorChar+TransactionsFileName;
            if(!FileUtilities.Exists(transactionFileName)){
                PrintUtilities.PrintErrorLine("Failed to find file: "+transactionFileName);
                return null;
            }
            var dataProvider =new DataProvider(new FileInputStream(bookFileName),
                    new FileInputStream(userFileName),
                    new FileInputStream(transactionFileName),
                    new FileOutputStream(transactionFileName,true),
                    new FileOutputStream(bookFileName, true),
                    new FileOutputStream(userFileName, true));

            var idMapFileName =  DataDir+ File.separatorChar+IdMapsFileName;
            if(FileUtilities.Exists(idMapFileName)){
                var inputStream = new FileInputStream(idMapFileName);
                dataProvider.AddIdMapDb(inputStream, new FileOutputStream(idMapFileName,true));
            }

            var checkoutFileName =  DataDir+ File.separatorChar+CheckoutsFileName;
            if(FileUtilities.Exists(checkoutFileName)){
                var inputStream = new FileInputStream(checkoutFileName);
                dataProvider.AddCheckoutDb(inputStream, new FileOutputStream(checkoutFileName,true), dataProvider.UserDb, dataProvider.IdDb);
            }

//            var newBooks = new ArrayList<Book>();
//            for (var book: dataProvider.BookDb.GetAllBooks()) {
//                var shortIdDb = new IdDb(null, null);
//                book.ShortId = shortIdDb.GenerateShortId();
//                newBooks.add(book);
//            }
//            for (var book:newBooks
//                 ) {
//                PrintUtilities.PrintLine(book.toString());
//                PrintUtilities.PrintLine(FlatObjectParser.RecordSeparator);
//            }

            return dataProvider;
        }
        catch (ParseException | FileNotFoundException e) {
            PrintUtilities.PrintErrorLine(e.getMessage());
            formatter.printHelp(commandSyntex, options);
            return null;
        } catch (IOException e) {
            PrintUtilities.PrintErrorLine(e.getMessage());
            formatter.printHelp(commandSyntex, options);
            return null;
        }

    }


}

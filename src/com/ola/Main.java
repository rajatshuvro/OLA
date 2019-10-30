package com.ola;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
//https://github.com/rajatshuvro/OLA/blob/master/README.md
    public static void main(String[] args) throws Exception{

        DataProvider dataProvider = Initialize(args);
        dataProvider.Load();
        boolean quit=false;
        while (!quit){
            PrintMenu();
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            String[] subArgs = command.split("\\s+");

            String subCommand = subArgs[0];
            switch (subCommand){
                case "add":
                    AddBooks.Run(subArgs, dataProvider.BooksDb);
                    break;
                case "co":
                    CheckOut.Run(subArgs);
                    break;
                case "quit":
                    quit = true;
                    break;
                default:
                    System.out.println("Type {quit} to exit");
            }

            //todo: save changes to file
        }

    }
    private static String commandSyntex = "ola -b c:\\path\\to\\file\\bookDb.tsv";
    private static DataProvider Initialize(String[] args) {
        System.out.println("Welcome to OLA (Onkur Library Application");
        Options options = new Options();
        Option bookDbFile = new Option("b", "book", true, "book database file");
        bookDbFile.setRequired(true);
        options.addOption(bookDbFile);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        if(args.length==1) {
            formatter.printHelp(commandSyntex, options);
            return null;
        }

        try {
            cmd = parser.parse(options, args);
            String fileName = cmd.getOptionValue("book");

            return new DataProvider(new FileInputStream(fileName));
        }
        catch (ParseException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(commandSyntex, options);
            return null;
        }
    }

    private static void PrintMenu() {
        System.out.println("Options:");
        System.out.println("\tadd (add new book to database)");
        System.out.println("\tco (checkout book)");
        System.out.println("\tuser (manage user info)");
        System.out.println("\tquit (quit OLA)");
        System.out.println("\t[Type command to get detailed help]");
    }

}

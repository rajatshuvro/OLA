package com.ola;
import org.apache.commons.cli.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
//https://github.com/rajatshuvro/OLA/blob/master/README.md
    public static void main(String[] args) throws Exception{

        while (true){
            PrintMenu();
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            String[] subArgs = command.split("\\s+");

            String subCommand = subArgs[0];
            switch (subCommand){
                case "add":
                    Add.Run(subArgs);
                    break;
                case "co":
                    CheckOut.Run(subArgs);
                    break;
                case "quit":
                    return;
                default:
                    System.out.println("Type {quit} to exit");
            }

        }

    }

    private static void PrintMenu() {
        System.out.println("Options:");
        System.out.println("\tadd (add new book to database)");
        System.out.println("\tco (checkout book)");
        System.out.println("\tquit (quit OLA)");
        System.out.println("\tType command to get detailed help");
    }

}

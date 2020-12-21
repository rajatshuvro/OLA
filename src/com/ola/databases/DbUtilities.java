package com.ola.databases;

import com.ola.dataStructures.Book;
import com.ola.dataStructures.Checkout;
import com.ola.dataStructures.Transaction;
import com.ola.dataStructures.User;
import com.ola.parsers.BookParser;
import com.ola.parsers.CheckoutParser;
import com.ola.parsers.TransactionParser;
import com.ola.parsers.UserParser;
import com.ola.utilities.PrintUtilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DbUtilities {
    public static ArrayList<Checkout> ReadCheckouts(InputStream inputStream)  {
        if(inputStream !=null){
            var parser = new CheckoutParser(inputStream);
            ArrayList<Checkout> checkouts;
            try {
                checkouts = parser.GetCheckouts();
                inputStream.close();
                return checkouts;
            } catch (IOException e) {
                PrintUtilities.PrintWarningLine("Error reading checkout fob file");
                return null;
            }

        }
        return null;
    }

    public static ArrayList<Book> ReadBooks(InputStream inputStream) {
        if(inputStream==null) return null;
        var parser = new BookParser(inputStream);
        ArrayList<Book> books;
        try {
            books = parser.GetBooks();
            inputStream.close();
            return books;
        } catch (IOException e) {
            PrintUtilities.PrintWarningLine("Error reading book fob file");
            return null;
        }
    }

    public static Iterable<User> ReadUsers(FileInputStream inputStream) {
        if(inputStream==null) return null;
        var parser = new UserParser(inputStream);
        ArrayList<User> users;
        try {
            users = parser.GetUsers();
            inputStream.close();
            return users;
        } catch (IOException e) {
            PrintUtilities.PrintWarningLine("Error reading book fob file");
            return null;
        }
    }

    public static Iterable<Transaction> ReadTransactions(FileInputStream inputStream) {
        if(inputStream==null) return null;
        var parser = new TransactionParser(inputStream);
        ArrayList<Transaction> transactions;
        try {
            transactions = parser.GetTransactions();
            inputStream.close();
            return transactions;
        } catch (IOException e) {
            PrintUtilities.PrintWarningLine("Error reading book fob file");
            return null;
        }
    }
}

package com.ola.unitTests;

import com.ola.dataStructures.Transaction;
import com.ola.databases.TransactionDb;
import com.ola.parsers.TransactionParser;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTests {
    public static InputStream GetStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("#Onkur library transactions\n");
        writer.write("***************************\n");
        writer.write("Book Id:\t7890788-(2)\n");
        writer.write("User Id:\t234\n");
        writer.write("Date:\t\t2019-09-13 10:30:31\n");
        writer.write("Type:\t\tCheckout\n");
        writer.write("************************************\n");
        writer.write("Book Id:\t678564-(1)\n");
        writer.write("User Id:\t123\n");
        writer.write("Date:\t\t2019-10-15 11:01:22\n");
        writer.write("Type:\t\tCheckout\n");
        writer.write("************************************\n");
        writer.write("Book Id:\t456098-(1)\n");
        writer.write("User Id:\t345\n");
        writer.write("Date:\t\t2019-11-03 10:33:10\n");
        writer.write("Type:\t\tCheckout\n");
        writer.write("************************************\n");
        writer.write("Book Id:\t7890788-(2)\n");
        writer.write("User Id:\t234\n");
        writer.write("Date:\t\t2019-11-13 10:30:25\n");
        writer.write("Type:\t\tReturn\n");
        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }

    @Test
    public void ParseTransactionsTest() throws IOException {
        var parser = new TransactionParser(GetStream());
        var transactions = parser.GetTransactions();

        assertEquals(4, transactions.size());

        assertEquals("7890788-(2)", transactions.get(0).BookId);
        assertEquals("Checkout", transactions.get(0).Type);
    }
    private HashSet<String> GetBookIds() {
        var books = new HashSet<String>();
        books.add("7890788-(2)");
        books.add("678564-(1)");
        books.add("456098-(1)");

        return books;
    }

    private HashSet<Integer> GetUserIds() {
        var users = new HashSet<Integer>();
        users.add(234);
        users.add(123);
        users.add(345);
        return users;
    }
    @Test
    public void TransactionDbTest() throws IOException{
        var parser = new TransactionParser(GetStream());
        var transactions = parser.GetTransactions();

        var transactionDb = new TransactionDb(transactions, GetUserIds(), GetBookIds());

        assertEquals(Transaction.ReturnTag, transactionDb.GetBookStatus("7890788-(2)"));
        assertEquals(Transaction.CheckoutTag, transactionDb.GetBookStatus("678564-(1)"));
        assertEquals(Transaction.CheckoutTag, transactionDb.GetBookStatus("456098-(1)"));
    }

    @Test
    public void TransactionDbTest_add_transactions() throws IOException{
        var parser = new TransactionParser(GetStream());
        var transactions = parser.GetTransactions();

        var transactionDb = new TransactionDb(transactions, GetUserIds(), GetBookIds());

        //cannot return a book that is already returned
        assertFalse(transactionDb.Add(new Transaction("7890788-(2)", 234, TimeUtilities.GetCurrentTime(), Transaction.ReturnTag)));
        assertTrue(transactionDb.Add(new Transaction("678564-(1)", 123, TimeUtilities.GetCurrentTime(), Transaction.ReturnTag)));

        assertEquals(Transaction.ReturnTag, transactionDb.GetBookStatus("678564-(1)"));
    }

    @Test
    public void TransactionDbTest_match_user_on_return() throws IOException{
        var parser = new TransactionParser(GetStream());
        var transactions = parser.GetTransactions();

        var transactionDb = new TransactionDb(transactions, GetUserIds(), GetBookIds());

        //cannot return a book that is already returned
        assertFalse(transactionDb.Add(new Transaction("678564-(1)", 234, TimeUtilities.GetCurrentTime(), Transaction.ReturnTag)));

        assertEquals(Transaction.CheckoutTag, transactionDb.GetBookStatus("678564-(1)"));
    }

    private HashSet<String> GetBookIds_reduced() {
        var books = new HashSet<String>();
        books.add("7890788-(2)");
        //books.add("678564-(1)");
        books.add("456098-(1)");

        return books;
    }

    private HashSet<Integer> GetUserIds_reduced() {
        var users = new HashSet<Integer>();
        users.add(234);
        users.add(123);
        //users.add(345);
        return users;
    }
    @Test
    public void SkipInvalidUserAndBook() throws IOException{
        var parser = new TransactionParser(GetStream());
        var transactions = parser.GetTransactions();

        var transactionDb = new TransactionDb(transactions, GetUserIds_reduced(), GetBookIds_reduced());

        assertEquals(Transaction.ReturnTag, transactionDb.GetBookStatus("7890788-(2)"));
    }
    private ArrayList<Transaction> GetTransactions(){
        var transactions = new ArrayList<Transaction>();
        transactions.add(new Transaction("7890788-(2)", 234, TimeUtilities.parseDate("2019-09-13 10:30:31"), Transaction.CheckoutTag));
        transactions.add(new Transaction("678564-(1)", 123, TimeUtilities.parseDate("2019-10-15 11:01:22"), Transaction.CheckoutTag));
        transactions.add(new Transaction("456098-(1)", 345, TimeUtilities.parseDate("2019-11-03 10:33:22"), Transaction.CheckoutTag));
        transactions.add(new Transaction("7890788-(2)", 234, TimeUtilities.parseDate("2019-11-13 10:30:25"), Transaction.ReturnTag));

        return transactions;
    }
    @Test
    public void AppendNewTransactions() throws IOException{
        var memStream = new ByteArrayOutputStream();
        //initializing with empty transaction list
        var transactionDb = new TransactionDb(GetTransactions(), GetUserIds(), GetBookIds());
        //adding new transactions
        transactionDb.Add(new Transaction("7890788-(2)", 234, TimeUtilities.parseDate("2019-11-13 10:39:31"), Transaction.CheckoutTag));
        transactionDb.Add(new Transaction("678564-(1)", 123, TimeUtilities.parseDate("2019-11-15 11:01:22"), Transaction.ReturnTag));

        transactionDb.Append(memStream);
        var buffer = memStream.toByteArray();
        memStream.close();
        var inputStream =  new ByteArrayInputStream(buffer);
        var hasTimeStamp1= false;
        var hasTimeStamp2 = false;
        try (Scanner scanner =  new Scanner(inputStream)){
            while (scanner.hasNextLine()){
                var line = scanner.nextLine();
                if(line.contains("2019-11-13 10:39:31")){
                    hasTimeStamp1 = true;
                }
                if(line.contains("2019-11-15 11:01:22")){
                    hasTimeStamp2 = true;
                }
            }
        }

        assertTrue(hasTimeStamp1);
        assertTrue(hasTimeStamp2);
    }
}

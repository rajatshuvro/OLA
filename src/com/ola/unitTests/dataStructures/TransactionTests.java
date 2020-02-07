package com.ola.unitTests.dataStructures;

import com.ola.Appender;
import com.ola.dataStructures.Book;
import com.ola.dataStructures.Transaction;
import com.ola.dataStructures.User;
import com.ola.databases.BookDb;
import com.ola.databases.TransactionDb;
import com.ola.databases.UserDb;
import com.ola.parsers.TransactionParser;
import com.ola.unitTests.TestStreams;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTests {

    @Test
    public void ParseTransactionsTest() throws IOException {
        var parser = new TransactionParser(TestStreams.GetTransactionsStream());
        var transactions = parser.GetTransactions();

        assertEquals(4, transactions.size());

        assertEquals("7890788-(2)", transactions.get(0).BookId);
        assertEquals("Checkout", transactions.get(0).Type);
    }
    private BookDb GetBookDb() {
        var books = new ArrayList<Book>();
        books.add(new Book(7890788L,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, null));
        books.add(new Book(678564,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 1, null, null, null));
        books.add(new Book(456098,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 1, null, null, null));

        return new BookDb(books);
    }

    private UserDb GetUserDb() {
        var users = new ArrayList<User>();
        users.add(User.Create(234, "name1", User.StudentRoleTag, "name1@onkur.com", "4568932678"));
        users.add(User.Create(123, "name2", User.StudentRoleTag, "name2@onkur.com", "4568732678"));
        users.add(User.Create(345, "name2", User.VolunteerRoleTag, "name3@onkur.com", "4568732676"));
        return new UserDb(users);
    }
    @Test
    public void TransactionDbTest() throws IOException{
        var parser = new TransactionParser(TestStreams.GetTransactionStreamReduced());
        var transactions = parser.GetTransactions();
        var appender = new Appender(null, null, new ByteArrayOutputStream());
        var transactionDb = new TransactionDb(transactions, GetUserDb(), GetBookDb(), appender);

        assertEquals(Transaction.ReturnTag, transactionDb.GetBookStatus("7890788-(2)"));
        assertEquals(Transaction.CheckoutTag, transactionDb.GetBookStatus("678564-(1)"));
        assertEquals(Transaction.CheckoutTag, transactionDb.GetBookStatus("456098-(1)"));
    }

    @Test
    public void TransactionDbTest_add_transactions() throws IOException{
        var parser = new TransactionParser(TestStreams.GetTransactionStreamReduced());
        var transactions = parser.GetTransactions();
        var appender = new Appender(null, null, new ByteArrayOutputStream());
        var transactionDb = new TransactionDb(transactions, GetUserDb(), GetBookDb(), appender);

        //cannot return a book that is already returned
        assertFalse(transactionDb.Add(new Transaction("7890788-(2)", 234, TimeUtilities.GetCurrentTime(), Transaction.ReturnTag)));
        assertTrue(transactionDb.Add(new Transaction("678564-(1)", 123, TimeUtilities.GetCurrentTime(), Transaction.ReturnTag)));

        assertEquals(Transaction.ReturnTag, transactionDb.GetBookStatus("678564-(1)"));
    }

    @Test
    public void TransactionDbTest_match_user_on_return() throws IOException{
        var parser = new TransactionParser(TestStreams.GetTransactionsStream());
        var transactions = parser.GetTransactions();
        var appender = new Appender(null, null, new ByteArrayOutputStream());
        var transactionDb = new TransactionDb(transactions, GetUserDb(), GetBookDb(), appender);

        assertTrue(transactionDb.Add(new Transaction("678564-(1)", Integer.MIN_VALUE, TimeUtilities.GetCurrentTime(), Transaction.ReturnTag)));

        assertEquals(Transaction.ReturnTag, transactionDb.GetBookStatus("678564-(1)"));
    }

    private BookDb GetBookDb_reduced() {
        var books = new ArrayList<Book>();
        books.add(new Book(7890788L,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, null));
        books.add(new Book(456098,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 1, null, null, null));

        return new BookDb(books);
    }

    private UserDb GetUserDb_reduced() {
        var users = new ArrayList<User>();
        users.add(User.Create(234, "name1", User.StudentRoleTag, "name1@onkur.com", "4568932678"));
        users.add(User.Create(123, "name2", User.StudentRoleTag, "name2@onkur.com", "4568732678"));
        return new UserDb(users);
    }
    @Test
    public void SkipInvalidUserAndBook() throws IOException{
        var parser = new TransactionParser(TestStreams.GetTransactionStreamReduced());
        var transactions = parser.GetTransactions();
        var appender = new Appender(null, null, new ByteArrayOutputStream());

        var transactionDb = new TransactionDb(transactions, GetUserDb_reduced(), GetBookDb_reduced(), appender);

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
        var appender = new Appender(null, null, memStream);
        //initializing with empty transaction list
        var transactionDb = new TransactionDb(GetTransactions(), GetUserDb(), GetBookDb(), appender);
        //adding new transactions
        transactionDb.Add(new Transaction("7890788-(2)", 234, TimeUtilities.parseDate("2019-11-13 10:39:31"), Transaction.CheckoutTag));
        transactionDb.Add(new Transaction("678564-(1)", 123, TimeUtilities.parseDate("2019-11-15 11:01:22"), Transaction.ReturnTag));

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
        appender.Close();
        assertTrue(hasTimeStamp1);
        assertTrue(hasTimeStamp2);
    }
}

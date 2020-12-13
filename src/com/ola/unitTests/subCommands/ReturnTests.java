package com.ola.unitTests.subCommands;

import com.ola.Appender;
import com.ola.dataStructures.*;
import com.ola.databases.*;
import com.ola.parsers.CheckoutParser;
import com.ola.parsers.ReturnCsvParser;
import com.ola.unitTests.TestStreams;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ReturnTests {
    
    private BookDb GetBookDb() {
        var books = new ArrayList<Book>();
        books.add(Book.Create(7890788L,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, "CAT12"));
        books.add(Book.Create(678564,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 1, null, null, "BAT12"));
        books.add(Book.Create(678564,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, "DOG99"));
        books.add(Book.Create(456098,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 1, null, null, "PIG07"));
        books.add(Book.Create(456098,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, "GIP09"));

        return new BookDb(books);
    }

    private UserDb GetUserDb() {
        var users = new ArrayList<User>();
        users.add(User.Create("234", "name1", User.StudentRoleTag, "name1@onkur.com", "4568932678"));
        users.add(User.Create("123", "name2", User.StudentRoleTag, "name2@onkur.com", "4568732678"));
        users.add(User.Create("345", "name3", User.VolunteerRoleTag, "name3@onkur.com", "4568732676"));
        return new UserDb(users);
    }

    private ArrayList<Transaction> GetTransactions(){
        var transactions = new ArrayList<Transaction>();
        transactions.add(Transaction.Create("7890788-(2)", "234", TimeUtilities.parseDateTime("2019-09null3 10:30:31"), Transaction.CheckoutTag));
        transactions.add(Transaction.Create("678564-(1)", "123", TimeUtilities.parseDateTime("2019null0null5 11:01:22"), Transaction.CheckoutTag));
        transactions.add(Transaction.Create("456098-(1)", "345", TimeUtilities.parseDateTime("2019null1-03 10:33:22"), Transaction.CheckoutTag));
        transactions.add(Transaction.Create("7890788-(2)", "234", TimeUtilities.parseDateTime("2019null1null3 10:30:25"), Transaction.ReturnTag));

        return transactions;
    }
    @Test
    public void Transaction_return(){
        var appender = new Appender(null, null, new ByteArrayOutputStream());
        var transactionDb = new TransactionDb(GetTransactions(), GetUserDb(), GetBookDb(), appender);
        transactionDb.Return(new Return("456098-(1)", TimeUtilities.GetCurrentTime()));

        assertEquals(Transaction.ReturnTag, transactionDb.GetBookStatus("456098-(1)"));
    }

    @Test
    public void Transaction_return_invalid_book(){
        var appender = new Appender(null, null, new ByteArrayOutputStream());
        var transactionDb = new TransactionDb(GetTransactions(), GetUserDb(), GetBookDb(), appender);
        transactionDb.Return(new Return("456098-(4)", TimeUtilities.GetCurrentTime()));

        assertEquals(Transaction.UnknownTag, transactionDb.GetBookStatus("456098-(4)"));
    }
    private ArrayList<Checkout> GetCheckouts(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("7890788-(2)", "234","name1@onkur.com", TimeUtilities.parseGoogleDateTime("2020/09/30 3:20:16 PM MDT"), TimeUtilities.parseDate("2020null0-25")));
        checkouts.add(new Checkout("678564-(1)", "123", "name2@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020null0-29")));
        checkouts.add(new Checkout("456098-(1)", "345", "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:22:04 PM MDT"), TimeUtilities.parseDate("2020null0-28")));
        checkouts.add(new Checkout("7890788-(2)", "234", "name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:23:30 PM MDT"), TimeUtilities.parseDate("2020null0-26")));

        return checkouts;
    }

    private ArrayList<Checkout> GetCheckouts_without_userid(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("7890788-(2)", null,"name1@onkur.com", TimeUtilities.parseGoogleDateTime("2020/09/30 3:20:16 PM MDT"), TimeUtilities.parseDate("2020null0-25")));
        checkouts.add(new Checkout("678564-(1)", null, "name2@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020null0-29")));
        checkouts.add(new Checkout("456098-(1)", null, "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:22:04 PM MDT"), TimeUtilities.parseDate("2020null0-28")));
        checkouts.add(new Checkout("7890788-(2)", null, "name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:23:30 PM MDT"), TimeUtilities.parseDate("2020null0-26")));

        return checkouts;
    }

    @Test
    public void Bulk_return(){
        var csvParser = new ReturnCsvParser(TestStreams.GetReturnCsvStream());
        var checkoutDb = new CheckoutDb(GetCheckouts(), null, GetUserDb(), GetBookDb());

        for (var returnRecord: csvParser.GetReturnes()) {
            assertTrue(checkoutDb.Return(returnRecord));
        }

        assertFalse(checkoutDb.Return(new Return("1234567-(3)", TimeUtilities.GetCurrentTime())));
    }

    @Test
    public void Bulk_return_shortId(){
        var csvParser = new ReturnCsvParser(TestStreams.GetReturnCsvStream_shortId());
        var checkoutDb = new CheckoutDb(GetCheckouts(), null, GetUserDb(), GetBookDb());

        for (var returnRecord: csvParser.GetReturnes()) {
            assertTrue(checkoutDb.Return(returnRecord));
        }
        assertFalse(checkoutDb.Return(new Return("1234567-(3)", TimeUtilities.GetCurrentTime())));
    }

    @Test
    public void Bulk_return_without_userid(){
        var csvParser = new ReturnCsvParser(TestStreams.GetReturnCsvStream());
        var checkoutDb = new CheckoutDb(GetCheckouts_without_userid(), null, GetUserDb(), GetBookDb());

        for (var returnRecord: csvParser.GetReturnes()) {
            assertTrue(checkoutDb.Return(returnRecord));
        }

        assertFalse(checkoutDb.Return(new Return("1234567-(3)", TimeUtilities.GetCurrentTime())));

    }

    @Test
    public void Return_write() throws IOException {
        var csvParser = new ReturnCsvParser(TestStreams.GetReturnCsvStream());
        var checkoutDb = new CheckoutDb(GetCheckouts(), null, GetUserDb(), GetBookDb());

        for (var bookId: csvParser.GetReturnes()) {
            assertTrue(checkoutDb.Return(bookId));
        }
        //write out returns
        var memStream = new ByteArrayOutputStream();
        checkoutDb.WriteReturns(memStream,true);

        checkoutDb.Close();
        //read re-written checkouts
        var buffer = memStream.toByteArray();
        memStream.close();
        var readStream = new ByteArrayInputStream(buffer);

        var checkoutParser = new CheckoutParser(readStream);
        checkoutDb = new CheckoutDb(checkoutParser.GetCheckouts(), null, GetUserDb(), GetBookDb());
        assertFalse(checkoutDb.Return(new Return("1234567-(3)", TimeUtilities.GetCurrentTime())));
    }
}

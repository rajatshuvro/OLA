package com.ola.unitTests.subCommands;

import com.ola.Appender;
import com.ola.CheckOut;
import com.ola.dataStructures.Book;
import com.ola.dataStructures.Transaction;
import com.ola.dataStructures.User;
import com.ola.databases.BookDb;
import com.ola.databases.TransactionDb;
import com.ola.databases.UserDb;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckoutTests {
    private BookDb GetBookDb() {
        var books = new ArrayList<Book>();
        books.add(new Book(7890788L,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, null));
        books.add(new Book(678564,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 1, null, null, null));
        books.add(new Book(678564,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, null));
        books.add(new Book(456098,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 1, null, null, null));
        books.add(new Book(456098,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, null));

        return new BookDb(books);
    }

    private UserDb GetUserDb() {
        var users = new ArrayList<User>();
        users.add(new User(234, "name1", User.StudentRoleTag, "name1@onkur.com", "4568932678"));
        users.add(new User(123, "name2", User.StudentRoleTag, "name2@onkur.com", "4568732678"));
        users.add(new User(345, "name2", User.VolunteerRoleTag, "name3@onkur.com", "4568732676"));
        return new UserDb(users);
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
    public void Checkout_already_in_circulation(){
        var appender = new Appender(null, null, new ByteArrayOutputStream());
        var transactionDb = new TransactionDb(GetTransactions(), GetUserDb(), GetBookDb(), appender);
        var args = new String[]{"co", "-b", "7890788-(2)","-u","345"};
        CheckOut.Run(args, transactionDb);

        assertEquals(Transaction.CheckoutTag, transactionDb.GetBookStatus("7890788-(2)"));
    }

    @Test
    public void Checkout_new_book(){
        var appender = new Appender(null, null, new ByteArrayOutputStream());
        var transactionDb = new TransactionDb(GetTransactions(), GetUserDb(), GetBookDb(), appender);
        var args = new String[]{"co", "-b", "678564-(2)","-u","345"};
        CheckOut.Run(args, transactionDb);

        assertEquals(Transaction.CheckoutTag, transactionDb.GetBookStatus("678564-(2)"));
    }

    @Test
    public void Checkout_invalid_user(){
        var appender = new Appender(null, null, new ByteArrayOutputStream());
        var transactionDb = new TransactionDb(GetTransactions(), GetUserDb(), GetBookDb(), appender);
        var args = new String[]{"co", "-b", "678564-(2)","-u","1345"};
        CheckOut.Run(args, transactionDb);

        assertEquals(Transaction.UnknownTag, transactionDb.GetBookStatus("678564-(2)"));
    }

    @Test
    public void Checkout_invalid_book(){
        var appender = new Appender(null, null, new ByteArrayOutputStream());
        var transactionDb = new TransactionDb(GetTransactions(), GetUserDb(), GetBookDb(), appender);
        var args = new String[]{"co", "-b", "678564-(3)","-u","345"};
        CheckOut.Run(args, transactionDb);

        assertEquals(Transaction.UnknownTag, transactionDb.GetBookStatus("678564-(2)"));
    }
}

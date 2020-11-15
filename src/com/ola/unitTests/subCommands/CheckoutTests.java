package com.ola.unitTests.subCommands;

import com.ola.Appender;
import com.ola.CheckOut;
import com.ola.dataStructures.Book;
import com.ola.dataStructures.Checkout;
import com.ola.dataStructures.Transaction;
import com.ola.dataStructures.User;
import com.ola.databases.BookDb;
import com.ola.databases.CheckoutDb;
import com.ola.databases.TransactionDb;
import com.ola.databases.UserDb;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTests {
    private BookDb GetBookDb() {
        var books = new ArrayList<Book>();
        books.add(Book.Create(7890788L,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, null));
        books.add(Book.Create(678564,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 1, null, null, null));
        books.add(Book.Create(678564,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, null));
        books.add(Book.Create(456098,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 1, null, null, null));
        books.add(Book.Create(456098,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "Fiction", 3, 2, null, null, null));

        return new BookDb(books);
    }

    private UserDb GetUserDb() {
        var users = new ArrayList<User>();
        users.add(User.Create(234, "name1", User.StudentRoleTag, "name1@onkur.com", "4568932678"));
        users.add(User.Create(123, "name2", User.StudentRoleTag, "name2@onkur.com", "4568732678"));
        users.add(User.Create(345, "name2", User.VolunteerRoleTag, "name3@onkur.com", "4568732676"));
        return new UserDb(users);
    }


    private ArrayList<Checkout> GetCheckouts(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("7890788-(2)", 234, "name1@onkur.com", TimeUtilities.parseGoogleDateTime("2020/09/30 3:20:16 PM MDT"), TimeUtilities.parseDate("2020-10-25")));
        checkouts.add(new Checkout("678564-(1)", 123, "name2@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));
        checkouts.add(new Checkout("456098-(1)", 345, "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:22:04 PM MDT"), TimeUtilities.parseDate("2020-10-28")));
        checkouts.add(new Checkout("7890788-(2)", 234, "name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:23:30 PM MDT"), TimeUtilities.parseDate("2020-10-26")));

        return checkouts;
    }

    private ArrayList<Checkout> GetNewCheckouts(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("678564-(2)", 345, "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));

        return checkouts;
    }

    private ArrayList<Checkout> GetNewCheckouts_without_userid(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("678564-(2)", -1, "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));

        return checkouts;
    }
    @Test
    public void Checkout_already_in_circulation(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), new ByteArrayOutputStream());

        assertTrue(chekoutDb.IsCheckedOut("7890788-(2)"));
    }

    @Test
    public void Checkout_new_book(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), new ByteArrayOutputStream());
        chekoutDb.TryAddRange(GetNewCheckouts(), GetBookDb(), GetUserDb());
        assertTrue(chekoutDb.IsCheckedOut("678564-(2)"));
    }

    @Test
    public void Checkout_new_book_without_userid(){
        var chekoutDb = new CheckoutDb(GetNewCheckouts_without_userid(), new ByteArrayOutputStream());
        assertTrue(chekoutDb.IsCheckedOut("678564-(2)"));
    }

    @Test
    public void Checkout_invalid_user(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), null);
        var invalidCheckout = new Checkout("678564-(2)", 12345,"name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29") );

        assertFalse(chekoutDb.TryAdd(invalidCheckout, GetBookDb(), GetUserDb()));
    }

    @Test
    public void Checkout_invalid_book(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), null);
        var invalidCheckout = new Checkout("678564-(3)", 345,"name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29") );

        assertFalse(chekoutDb.TryAdd(invalidCheckout, GetBookDb(), GetUserDb()));
    }
}

package com.ola.unitTests.subCommands;

import com.ola.dataStructures.*;
import com.ola.databases.*;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTests {
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
        users.add(User.Create("name.1", "name1", User.StudentRoleTag, "name1@onkur.com", "4568932678"));
        users.add(User.Create("name.2", "name2", User.StudentRoleTag, "name2@onkur.com", "4568732678"));
        users.add(User.Create("name.3", "name2", User.VolunteerRoleTag, "name3@onkur.com", "4568732676"));
        users.add(User.Create("name.4", "name3", User.VolunteerRoleTag, "name3@onkur.com", "4568732676"));
        return new UserDb(users);
    }


    private ArrayList<Checkout> GetCheckouts(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("7890788-(2)", "name.1", "name1@onkur.com", TimeUtilities.parseGoogleDateTime("2020/09/30 3:20:16 PM MDT"), TimeUtilities.parseDate("2020-10-25")));
        checkouts.add(new Checkout("678564-(1)", "name.2", "name2@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));
        checkouts.add(new Checkout("456098-(1)", "name.3", "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:22:04 PM MDT"), TimeUtilities.parseDate("2020-10-28")));
        checkouts.add(new Checkout("7890788-(2)", "name.1", "name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:23:30 PM MDT"), TimeUtilities.parseDate("2020-10-26")));

        return checkouts;
    }

    private ArrayList<Checkout> GetCheckouts_shortId(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("CAT12", "name.1", "name1@onkur.com", TimeUtilities.parseGoogleDateTime("2020/09/30 3:20:16 PM MDT"), TimeUtilities.parseDate("2020-10-25")));
        checkouts.add(new Checkout("BAT12", "name.2", "name2@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));
        checkouts.add(new Checkout("PIG07", "name.3", "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:22:04 PM MDT"), TimeUtilities.parseDate("2020-10-28")));
        checkouts.add(new Checkout("CAT12", "name.1", "name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:23:30 PM MDT"), TimeUtilities.parseDate("2020-10-26")));

        return checkouts;
    }

    private ArrayList<Checkout> GetNewCheckouts(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("678564-(2)", "name.3", "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));

        return checkouts;
    }

    private ArrayList<Checkout> GetCheckouts_without_userid(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("7890788-(2)", null, "name1@onkur.com", TimeUtilities.parseGoogleDateTime("2020/09/30 3:20:16 PM MDT"), TimeUtilities.parseDate("2020-10-25")));
        checkouts.add(new Checkout("678564-(1)", "name.3", "name2@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));
        checkouts.add(new Checkout("456098-(1)", "name.4", "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:22:04 PM MDT"), TimeUtilities.parseDate("2020-10-28")));

        return checkouts;
    }
    @Test
    public void Checkout_already_in_circulation(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), new ByteArrayOutputStream(), GetUserDb(), GetBookDb());

        assertTrue(chekoutDb.IsCheckedOut("7890788-(2)"));
    }

    @Test
    public void Checkout_with_short_id(){
        var chekoutDb = new CheckoutDb(null, new ByteArrayOutputStream(), GetUserDb(), GetBookDb());
        chekoutDb.TryAddRange(GetCheckouts_shortId());

        assertTrue(chekoutDb.IsCheckedOut("7890788-(2)"));
    }
    @Test
    public void Checkout_new_book(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), new ByteArrayOutputStream(), GetUserDb(), GetBookDb());
        chekoutDb.TryAddRange(GetNewCheckouts());
        assertTrue(chekoutDb.IsCheckedOut("678564-(2)"));
    }

    @Test
    public void Checkout_new_book_without_userid(){
        var checkoutDb = new CheckoutDb(null, new ByteArrayOutputStream(), GetUserDb(), GetBookDb());
        checkoutDb.TryAddRange(GetCheckouts_without_userid());

        assertTrue(checkoutDb.IsCheckedOut("7890788-(2)"));
        assertFalse(checkoutDb.IsCheckedOut("678564-(1)"));
        assertEquals("name.4", checkoutDb.GetCheckout("456098-(1)").UserId);
    }

    @Test
    public void Checkout_invalid_user(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), null, GetUserDb(), GetBookDb());
        var invalidCheckout = new Checkout("678564-(2)", "name.10","name10@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29") );

        assertFalse(chekoutDb.TryAdd(invalidCheckout));
    }

    @Test
    public void Checkout_invalid_book(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), null, GetUserDb(), GetBookDb());
        var invalidCheckout = new Checkout("678564-(3)", "name.3","name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29") );

        assertFalse(chekoutDb.TryAdd(invalidCheckout));
    }

}

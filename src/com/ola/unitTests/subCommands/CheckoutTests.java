package com.ola.unitTests.subCommands;

import com.ola.Appender;
import com.ola.CheckOut;
import com.ola.dataStructures.*;
import com.ola.databases.*;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTests {
    private IdDb GetIdDb(){
        var idMaps = new ArrayList<IdMap>();
        idMaps.add(new IdMap("CAT12", "7890788-(2)"));
        idMaps.add(new IdMap("BAT12", "678564-(1)"));
        idMaps.add(new IdMap("DOG99", "678564-(2)"));
        idMaps.add(new IdMap("PIG07", "456098-(1)"));
        idMaps.add(new IdMap("GIP09", "456098-(2)"));

        return new IdDb(idMaps,null);
    }

    private UserDb GetUserDb() {
        var users = new ArrayList<User>();
        users.add(User.Create(234, "name1", User.StudentRoleTag, "name1@onkur.com", "4568932678"));
        users.add(User.Create(123, "name2", User.StudentRoleTag, "name2@onkur.com", "4568732678"));
        users.add(User.Create(345, "name2", User.VolunteerRoleTag, "name3@onkur.com", "4568732676"));
        users.add(User.Create(349, "name3", User.VolunteerRoleTag, "name3@onkur.com", "4568732676"));
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

    private ArrayList<Checkout> GetCheckouts_shortId(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("CAT12", 234, "name1@onkur.com", TimeUtilities.parseGoogleDateTime("2020/09/30 3:20:16 PM MDT"), TimeUtilities.parseDate("2020-10-25")));
        checkouts.add(new Checkout("BAT12", 123, "name2@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));
        checkouts.add(new Checkout("PIG07", 345, "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:22:04 PM MDT"), TimeUtilities.parseDate("2020-10-28")));
        checkouts.add(new Checkout("CAT12", 234, "name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:23:30 PM MDT"), TimeUtilities.parseDate("2020-10-26")));

        return checkouts;
    }

    private ArrayList<Checkout> GetNewCheckouts(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("678564-(2)", 345, "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));

        return checkouts;
    }

    private ArrayList<Checkout> GetCheckouts_without_userid(){
        var checkouts = new ArrayList<Checkout>();
        checkouts.add(new Checkout("7890788-(2)", -1, "name1@onkur.com", TimeUtilities.parseGoogleDateTime("2020/09/30 3:20:16 PM MDT"), TimeUtilities.parseDate("2020-10-25")));
        checkouts.add(new Checkout("678564-(1)", 345, "name2@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29")));
        checkouts.add(new Checkout("456098-(1)", 349, "name3@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:22:04 PM MDT"), TimeUtilities.parseDate("2020-10-28")));

        return checkouts;
    }
    @Test
    public void Checkout_already_in_circulation(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), new ByteArrayOutputStream(), GetUserDb(), GetIdDb());

        assertTrue(chekoutDb.IsCheckedOut("7890788-(2)"));
    }

    @Test
    public void Checkout_with_short_id(){
        var chekoutDb = new CheckoutDb(null, new ByteArrayOutputStream(), GetUserDb(), GetIdDb());
        chekoutDb.TryAddRange(GetCheckouts_shortId());

        assertTrue(chekoutDb.IsCheckedOut("7890788-(2)"));
    }
    @Test
    public void Checkout_new_book(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), new ByteArrayOutputStream(), GetUserDb(), GetIdDb());
        chekoutDb.TryAddRange(GetNewCheckouts());
        assertTrue(chekoutDb.IsCheckedOut("678564-(2)"));
    }

    @Test
    public void Checkout_new_book_without_userid(){
        var checkoutDb = new CheckoutDb(null, new ByteArrayOutputStream(), GetUserDb(), GetIdDb());
        checkoutDb.TryAddRange(GetCheckouts_without_userid());

        assertTrue(checkoutDb.IsCheckedOut("7890788-(2)"));
        assertFalse(checkoutDb.IsCheckedOut("678564-(1)"));
        assertEquals(349, checkoutDb.GetCheckout("456098-(1)").UserId);
    }

    @Test
    public void Checkout_invalid_user(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), null, GetUserDb(), GetIdDb());
        var invalidCheckout = new Checkout("678564-(2)", 12345,"name10@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29") );

        assertFalse(chekoutDb.TryAdd(invalidCheckout));
    }

    @Test
    public void Checkout_invalid_book(){
        var chekoutDb = new CheckoutDb(GetCheckouts(), null, GetUserDb(), GetIdDb());
        var invalidCheckout = new Checkout("678564-(3)", 345,"name1@onkur.com",TimeUtilities.parseGoogleDateTime("2020/09/30 3:21:27 PM MDT"), TimeUtilities.parseDate("2020-10-29") );

        assertFalse(chekoutDb.TryAdd(invalidCheckout));
    }

}

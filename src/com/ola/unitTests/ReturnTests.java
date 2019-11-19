package com.ola.unitTests;

import com.ola.CheckOut;
import com.ola.Return;
import com.ola.dataStructures.Transaction;
import com.ola.databases.TransactionDb;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReturnTests {
    private HashSet<String> GetBookIds() {
        var books = new HashSet<String>();
        books.add("7890788-(2)");
        books.add("678564-(1)");
        books.add("678564-(2)");
        books.add("456098-(1)");
        books.add("456098-(2)");

        return books;
    }

    private HashSet<Integer> GetUserIds() {
        var users = new HashSet<Integer>();
        users.add(234);
        users.add(123);
        users.add(345);
        return users;
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
    public void Return(){
        var transactionDb = new TransactionDb(GetTransactions(), GetUserIds(), GetBookIds());
        var args = new String[]{"ret", "-b", "456098-(1)","-u","345"};
        Return.Run(args, transactionDb);

        assertEquals(Transaction.ReturnTag, transactionDb.GetBookStatus("456098-(1)"));
    }

    @Test
    public void Return_invalid_book(){
        var transactionDb = new TransactionDb(GetTransactions(), GetUserIds(), GetBookIds());
        var args = new String[]{"ret", "-b", "456098-(4)","-u","345"};
        Return.Run(args, transactionDb);

        assertEquals(Transaction.UnknownTag, transactionDb.GetBookStatus("456098-(4)"));
    }
}

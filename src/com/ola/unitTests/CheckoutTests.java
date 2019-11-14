package com.ola.unitTests;

import com.ola.dataStructures.Transaction;
import com.ola.databases.TransactionDb;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

public class CheckoutTests {
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
        transactions.add(new Transaction("7890788-(2)", 234, TimeUtilities.GetCurrentTime(), Transaction.CheckoutTag));
        transactions.add(new Transaction("678564-(1)", 123, TimeUtilities.GetCurrentTime(), Transaction.CheckoutTag));
        transactions.add(new Transaction("456098-(1)", 345, TimeUtilities.GetCurrentTime(), Transaction.CheckoutTag));
        transactions.add(new Transaction("7890788-(2)", 234, TimeUtilities.GetCurrentTime(), Transaction.ReturnTag));

        return transactions;
    }

    @Test
    public void Checkout_already_in_circulation(){
        var transactionDb = new TransactionDb(GetTransactions(), GetUserIds(), GetBookIds());
    }
}

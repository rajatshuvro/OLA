package com.ola.dataStructures;

import java.util.Date;
import java.util.HashSet;

public class Transaction {
    public final String BookId;
    public final int UserId;
    public final Date Date; // use the transaction time stamp
    public final String Type;

    public Transaction(String bookId, int userId, Date date, String type){
        BookId = bookId;
        UserId = userId;
        Date = date;
        Type = type;
    }

    public static boolean IsValid(String bookId, int userId, Date date, String type){
        return bookId != null &&
                userId != 0 &&
                IsValidType(type);
    }
    public String toString(){
        return "BookId: "+BookId + "UserId: "+UserId + "Time: "+ Date.toString()
                + "Transaction type: "+ Type;
    }

    //static fields
    public static final String CheckoutTag = "Checkout";
    public static final String ReturnTag = "Return";

    public static final HashSet<String> TransactionTags = new HashSet<>(){{
        add(CheckoutTag);
        add(ReturnTag);
    }};
    private static boolean IsValidType(String role) {
        return TransactionTags.contains(role);
    }

    public boolean OlderThan(Transaction record) {
        return Date.before(record.Date);
    }
}

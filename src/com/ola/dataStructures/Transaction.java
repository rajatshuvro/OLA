package com.ola.dataStructures;

import com.ola.DataProvider;
import com.ola.utilities.TimeUtilities;

import java.util.Date;
import java.util.HashSet;

public class Transaction {
    public final String BookId;
    public final int UserId;
    public final Date Date; // use the transaction time stamp
    public final String Type;
    public final long Id;

    private Transaction(String bookId, int userId, Date date, String type){
        BookId = bookId;
        UserId = userId;
        Date = date;
        Type = type;
        Id = date.getTime();
    }

    private static boolean IsValid(String bookId, int userId, Date date, String type){
        return bookId != null &&
                userId != 0 &&
                IsValidType(type);
    }

    public static Transaction Create(String bookId, int userId, Date date, String type){
        if(!IsValid( bookId, userId, date, type)) return null;
        return new Transaction(bookId, userId, date, type);
    }

    //static fields
    public static final String CheckoutTag = "Checkout";
    public static final String ReturnTag = "Return";
    public static final String UnknownTag = "Unknown";

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

    @Override
    public String toString(){
        return "Id:            "+ Date.getTime()+'\n'+
               "Book Id:       "+ BookId+'\n'+
               "User Id:       "+ UserId+'\n'+
               "Date:          "+ TimeUtilities.ToString(Date)+'\n'+
               "Type:          "+Type;

    }
}

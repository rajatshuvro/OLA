package com.ola.dataStructures;

import com.ola.utilities.TimeUtilities;

import java.util.Date;

public class Checkout {
    public final String BookId;
    public final int UserId;
    public final Date CheckoutDate;
    public final Date DueDate;

    public Checkout(String bookId, int userId, Date checkoutDate, Date dueDate ){
        BookId = bookId;
        UserId = userId;
        CheckoutDate = checkoutDate;
        DueDate = dueDate;
    }

    @Override
    public String toString(){
        return
                "Book Id:       "+ BookId+'\n'+
                "User Id:       "+ UserId+'\n'+
                "CheckoutDate:  "+ TimeUtilities.ToString(CheckoutDate)+'\n'+
                "DueDate:       "+TimeUtilities.ToString(DueDate);

    }
}

package com.ola;

import com.ola.dataStructures.Transaction;
import com.ola.utilities.TimeUtilities;

public class Status {
    public static void Run(DataProvider dataProvider){
        var transactionDb = dataProvider.TransactionsDb;
        var userDb = dataProvider.UserDb;
        var bookDb = dataProvider.BookDb;
        for (Transaction record: transactionDb.GetPendingCheckouts()) {
            var userName = userDb.GetUserName(record.UserId);
            var bookTitle = bookDb.GetTitle(record.BookId);
            System.out.println("User:\t" + userName + "\nTitle:\t" + bookTitle + "\ndate:\t"+ TimeUtilities.ToString(record.Date));
            System.out.println("**********************************************");
        }
    }
}

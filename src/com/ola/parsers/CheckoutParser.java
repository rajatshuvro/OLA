package com.ola.parsers;

import com.ola.dataStructures.Checkout;
import com.ola.dataStructures.Transaction;
import com.ola.utilities.TimeUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CheckoutParser {
    private InputStream _inputStream;

    private final String BookIdTag = "Book Id";
    private final String UserIdTag = "User Id";
    private final String CheckoutDateTag = "Checkout Date";
    private final String DueDateTag = "Due Date";

    public CheckoutParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    public ArrayList<Checkout> GetCheckouts() throws IOException {
        ArrayList<Checkout> checkouts = new ArrayList<>();
        var fobParser = new FlatObjectParser(_inputStream, new String[]{
                BookIdTag, UserIdTag, CheckoutDateTag, DueDateTag
        });

        var nextSetOfValues =fobParser.GetNextRecord();
        while ( nextSetOfValues != null){
            var checkout = GetCheckout(nextSetOfValues);
            if (checkout != null)  checkouts.add(checkout);

            nextSetOfValues = fobParser.GetNextRecord();
        }
        fobParser.close();
        return checkouts;
    }

    private Checkout GetCheckout(HashMap<String, String> keyValues) {
        String bookId = null;
        int userId = 0;
        Date checkoutDate = null;
        Date dueDate =null;

        for (var entry: keyValues.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            switch (key){
                case BookIdTag:
                    bookId = value;
                    break;
                case UserIdTag:
                    userId = Integer.parseInt(value);
                    break;
                case CheckoutDateTag:
                    checkoutDate = TimeUtilities.parseDateTime(value);
                    break;
                case DueDateTag:
                    dueDate = TimeUtilities.parseDateTime(value);
                    break;
            }

        }

        return new Checkout(bookId, userId, null, checkoutDate, dueDate);
    }
}

package com.ola.parsers;

import com.ola.dataStructures.Transaction;
import com.ola.utilities.TimeUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TransactionParser {
    private InputStream _inputStream;

    private final String BookIdTag = "Book Id";
    private final String UserIdTag = "User Id";
    private final String DateTag = "Date";
    private final String TypeTag = "Type";

    public TransactionParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    public ArrayList<Transaction> GetTransactions() throws IOException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        var fobParser = new FlatObjectParser(_inputStream, new String[]{
                BookIdTag, UserIdTag, DateTag, TypeTag
        });

        var nextSetOfValues =fobParser.GetNextRecord();
        while ( nextSetOfValues != null){
            var transaction = GetTransaction(nextSetOfValues);
            if (transaction != null)  transactions.add(transaction);

            nextSetOfValues = fobParser.GetNextRecord();
        }
        fobParser.close();
        return transactions;
    }

    private Transaction GetTransaction(HashMap<String, String> keyValues) {
        String bookId = null;
        int userId = 0;
        Date date = null;
        String type = null;

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
                case DateTag:
                    date = TimeUtilities.parseDate(value);
                    break;
                case TypeTag:
                    type = value;
                    break;
            }

        }
        if(!Transaction.IsValid(bookId, userId, date, type)) return null;
        return new Transaction(bookId, userId, date, type);
    }


}

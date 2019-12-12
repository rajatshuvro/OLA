package com.ola.parsers;

import com.ola.dataStructures.Transaction;
import com.ola.utilities.TimeUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static com.ola.parsers.ParserUtilities.GetNextRecordLines;

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

        try (Scanner scanner =  new Scanner(_inputStream)){
            while (scanner.hasNextLine()){
                String[] lines = GetNextRecordLines(scanner,"*");
                if(lines.length == 0) continue;
                var transaction = GetTransaction(lines);
                if (transaction != null){
                    transactions.add(transaction);
                }
            }
        }
        return transactions;
    }

    private Transaction GetTransaction(String[] lines) {
        String bookId = null;
        int userId = 0;
        Date date = null;
        String type = null;
        for (String line: lines) {
            var splits = line.split(":",2);
            var key = splits[0].strip();
            var value = splits[1].strip();

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

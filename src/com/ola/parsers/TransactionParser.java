package com.ola.parsers;

import com.ola.dataStructures.Transaction;
import com.ola.dataStructures.User;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static com.ola.parsers.parserUtilities.GetNextRecordLines;

public class TransactionParser {
    private InputStream _inputStream;

    private final String BookIdTag = "Book Id";
    private final String UserIdTag = "User Id";
    private final String DateTag = "Date";
    private final String TypeTag = "Type";

    private final DateFormat _dateFormat;
    private final String DateTimeFormat = "yyyy-MM-dd HH:mm:ss";


    public TransactionParser(InputStream inputStream){
        _dateFormat = new SimpleDateFormat(DateTimeFormat);
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
                    try {
                        date = !value.equals("") ?_dateFormat.parse(value): null;
                    } catch (ParseException e) {
                        System.out.println("Invalid entry date provided:"+value);
                        System.out.println("Date format: "+ DateTimeFormat);
                    }
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

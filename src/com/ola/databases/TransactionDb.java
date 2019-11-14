package com.ola.databases;

import com.ola.dataStructures.Transaction;
import com.ola.parsers.TransactionParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TransactionDb {
    private ArrayList<Transaction> _transactions;
    public HashMap<String, Transaction> _latestTransaction;

    public TransactionDb(Iterable<Transaction> transactions, HashSet<Integer> userIds, HashSet<String> bookIds){
        //transactions are assumed to ordered by increasing timestamps
        _transactions = new ArrayList<>();
        for (Transaction record: transactions) {
            if(!bookIds.contains(record.BookId)) {
                System.out.println("WARNING: Invalid book id:"+record.BookId+". Ignoring transaction.");
                continue;
            }
            if(!userIds.contains(record.UserId)){
                System.out.println("WARNING: Invalid user id:"+record.UserId+". Ignoring transaction.");
                continue;
            }
            _transactions.add(record);
        }
    }

    public HashMap<String, Transaction> GetLatestTransactions() {
        if(_latestTransaction != null) return _latestTransaction;
        _latestTransaction = new HashMap<>();
        for (Transaction record:_transactions) {
            if(!_latestTransaction.containsKey(record.BookId)){
                _latestTransaction.put(record.BookId, record);
                continue;
            }
            //update existing record
            var existingRecord = _latestTransaction.get(record.BookId);
            if(existingRecord.OlderThan(record)) _latestTransaction.replace(record.BookId, record);
        }
        return _latestTransaction;
    }

    public boolean Add(Transaction record){
        var latestTransactions = GetLatestTransactions();
        if(!latestTransactions.containsKey(record.BookId)){
            _latestTransaction.put(record.BookId, record);
            _transactions.add(record);
            return true;
        }
        //if the latest transaction is of type checkout you cannot add another checkout and similarly for return
        var latestRecord = _latestTransaction.get(record.BookId);
        if (latestRecord.Type.equals(record.Type)) {
            System.out.println("WARNING: cannot "+record.Type+ "a book that is already in that state");
            return false;
        }
        //make sure user ids match for returns
        if (record.Type.equals(Transaction.ReturnTag)){
            if(record.UserId != latestRecord.UserId){
                System.out.println("WARNING: A book has to be returned by the user who checked it out. User on checkout record: "+latestRecord.UserId);
                return false;
            }
        }
        _latestTransaction.replace(record.BookId, record);
        _transactions.add(record);
        return true;
    }
    private final String RecordSeparator = "************************************";
    private Format _dateFormat = new SimpleDateFormat(TransactionParser.DateTimeFormat);

    public void WriteLatestRecords(OutputStream stream) throws IOException {
        var latestTransactions = GetLatestTransactions();
        var writer = new BufferedWriter(new OutputStreamWriter(System.out));
        writer.write(RecordSeparator+'\n');
        for (Transaction record: latestTransactions.values()) {
            writer.write("Book Id:\t"+record.BookId+'\n');
            writer.write("User Id:\t"+record.UserId+'\n');
            writer.write("Date:\t\t"+_dateFormat.format(record.Date)+'\n');
            writer.write("Type:\t\t"+record.Type+'\n');
            writer.write(RecordSeparator+'\n');
        }
        stream.close();
    }
}

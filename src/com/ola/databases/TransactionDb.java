package com.ola.databases;

import com.ola.dataStructures.Transaction;

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
        if (latestRecord.Type == record.Type) {
            System.out.println("WARNING: cannot "+record.Type+ "a book that is already in that state");
            return false;
        }
        _latestTransaction.replace(record.BookId, record);
        _transactions.add(record);
        return true;
    }
}

package com.ola.databases;

import com.ola.dataStructures.Transaction;
import com.ola.utilities.TimeUtilities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TransactionDb {
    private ArrayList<Transaction> _transactions;
    public HashMap<String, Transaction> _latestTransaction;
    private HashSet<Integer> _userIds;
    private HashSet<String> _bookIds;
    private int _newRecordIndex;//keep track of where the new records start from

    public TransactionDb(Iterable<Transaction> transactions, HashSet<Integer> userIds, HashSet<String> bookIds){
        //transactions are assumed to ordered by increasing timestamps
        _userIds = userIds;
        _bookIds = bookIds;
        _transactions = new ArrayList<>();
        _latestTransaction = new HashMap<>();
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
            //updating latest transaction
            if(!_latestTransaction.containsKey(record.BookId)){
                _latestTransaction.put(record.BookId, record);
                continue;
            }
            //update existing record
            var existingRecord = _latestTransaction.get(record.BookId);
            if(existingRecord.OlderThan(record)) _latestTransaction.replace(record.BookId, record);
        }
        _newRecordIndex = _transactions.size();
    }

    public boolean Add(Transaction record){
        //make sure the book exists in the book database and the user in user database
        if(!_bookIds.contains(record.BookId)){
            System.out.println("WARNING:Unknown book id: "+ record.BookId);
            return false;
        }
        if(!_userIds.contains(record.UserId)){
            System.out.println("WARNING:Unknown user id: "+ record.UserId);
            return false;
        }
        if(!_latestTransaction.containsKey(record.BookId)){
            _latestTransaction.put(record.BookId, record);
            _transactions.add(record);
            return true;
        }
        //if the latest transaction is of type checkout you cannot add another checkout and similarly for return
        var latestRecord = _latestTransaction.get(record.BookId);
        if (latestRecord.Type.equals(record.Type)) {
            System.out.println("WARNING: cannot "+record.Type+ " a book that is already in that state. Book id:" + record.BookId);
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

    public void Append(OutputStream stream) throws IOException {
        if(_newRecordIndex == _transactions.size()) return;
        var writer = new BufferedWriter(new OutputStreamWriter(stream));
        for (int i = _newRecordIndex; i < _transactions.size(); i++) {
            var record = _transactions.get(i);
            writer.write("Book Id:\t"+record.BookId+'\n');
            writer.write("User Id:\t"+record.UserId+'\n');
            writer.write("Date:\t\t"+ TimeUtilities.ToString(record.Date)+'\n');
            writer.write("Type:\t\t"+record.Type+'\n');
            writer.write(RecordSeparator+'\n');
            _newRecordIndex++;
        }
        writer.close();

    }

    public final String[] HeaderLines = new String[]{
        "#Onkur library book lending records\n",
        "#Book Id = Onkur book id. Value = <String>\n",
        "#User Id = Onkur user id. Value = <Integer>\n",
        "#Date = Date of transaction. Value = <YYYY-MM-DD HH:MM:ss>\n",
        "#Type = Type of transaction. Value = Checkout/Return\n"
    };
    /// Write out all the records into an output stream
    public void Write(OutputStream stream) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(stream));
        //header lines
        for (String line: HeaderLines) {
            writer.write(line);
        }
        //not required, but for aesthetics
        writer.write(RecordSeparator+'\n');
        for (Transaction record: _transactions) {
            writer.write("Book Id:\t"+record.BookId+'\n');
            writer.write("User Id:\t"+record.UserId+'\n');
            writer.write("Date:\t\t"+ TimeUtilities.ToString(record.Date)+'\n');
            writer.write("Type:\t\t"+record.Type+'\n');
            writer.write(RecordSeparator+'\n');
        }
        writer.close();
    }

    public String GetBookStatus(String bookId) {
        if(_latestTransaction.containsKey(bookId)) return _latestTransaction.get(bookId).Type;
        return Transaction.UnknownTag;
    }

    public ArrayList<Transaction> GetPendingCheckouts() {
        var checkouts = new ArrayList<Transaction>();
        for (Transaction record: _latestTransaction.values()) {
            if(record.Type.equals(Transaction.CheckoutTag))
                checkouts.add(record);
        }
        return checkouts;
    }
}

package com.ola.databases;

import com.ola.Appender;
import com.ola.dataStructures.Transaction;
import com.ola.luceneIndex.TransactionSearchIndex;
import com.ola.parsers.FlatObjectParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionDb {
    private ArrayList<Transaction> _transactions;
    public HashMap<String, Transaction> _latestTransaction;
    private UserDb _userDb;
    private BookDb _bookDb;
    private Appender _appender;
    private TransactionSearchIndex _searchIndex;

    public TransactionDb(Iterable<Transaction> transactions, UserDb userDb, BookDb bookDb, Appender appender){
        //transactions are assumed to ordered by increasing timestamps
        _userDb = userDb;
        _bookDb = bookDb;
        _appender = appender;
        _transactions = new ArrayList<>();
        _latestTransaction = new HashMap<>();
        for (Transaction record: transactions) {
            if(_bookDb.GetBook(record.BookId)== null) {
                System.out.println("WARNING: Invalid book id:"+record.BookId+". Ignoring transaction.");
                continue;
            }
            if(_userDb.GetUser(record.UserId) == null){
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
    }

    public Transaction Get(int index){
        if(index < 0 || index >= _transactions.size()) return null;
        return _transactions.get(index);
    }

    public boolean Add(Transaction record) throws IOException {
        //make sure the book exists in the book database and the user in user database
        if(_bookDb.GetBook(record.BookId)== null){
            System.out.println("WARNING:Unknown book id: "+ record.BookId);
            return false;
        }
        if(_userDb.GetUser(record.UserId) == null && record.UserId != Integer.MIN_VALUE){
            System.out.println("WARNING:Unknown user id: "+ record.UserId);
            return false;
        }
        if(!_latestTransaction.containsKey(record.BookId)){
            _latestTransaction.put(record.BookId, record);
            _transactions.add(record);
            _appender.AppendTransactions(record);
            return true;
        }
        //if the latest transaction is of type checkout you cannot add another checkout and similarly for return
        var latestRecord = _latestTransaction.get(record.BookId);
        if (latestRecord.Type.equals(record.Type)) {
            System.out.println("WARNING: cannot "+record.Type+ " a book that is already in that state. Book id:" + record.BookId);
            return false;
        }

        _latestTransaction.replace(record.BookId, record);
        _transactions.add(record);
        _appender.AppendTransactions(record);
        return true;
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
        writer.write(FlatObjectParser.RecordSeparator+'\n');
        for (Transaction record: _transactions) {
            writer.write(record.toString()+'\n');
            writer.write(FlatObjectParser.RecordSeparator+'\n');
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

    public TransactionSearchIndex GetSearchIndex() throws IOException {
        if(_searchIndex == null) BuildSearchIndex();
        return _searchIndex;
    }

    public void BuildSearchIndex() throws IOException {
        _searchIndex = new TransactionSearchIndex(_transactions);
    }
}

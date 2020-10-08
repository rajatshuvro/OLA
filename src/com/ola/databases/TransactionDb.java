package com.ola.databases;

import com.ola.Appender;
import com.ola.dataStructures.Book;
import com.ola.dataStructures.Checkout;
import com.ola.dataStructures.Transaction;
import com.ola.parsers.FlatObjectParser;
import com.ola.utilities.PrintUtilities;
import com.ola.utilities.TimeUtilities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionDb {
    private ArrayList<Transaction> _transactions;
    private HashMap<String, Transaction> _latestTransactions;
    private UserDb _userDb;
    private BookDb _bookDb;
    private Appender _appender;
    public final int CheckoutLimit = 15;

    public TransactionDb(Iterable<Transaction> transactions, UserDb userDb, BookDb bookDb, Appender appender){
        //transactions are assumed to ordered by increasing timestamps
        _userDb = userDb;
        _bookDb = bookDb;
        _appender = appender;
        _transactions = new ArrayList<>();
        _latestTransactions = new HashMap<>();
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
            if(!_latestTransactions.containsKey(record.BookId)){
                _latestTransactions.put(record.BookId, record);
                continue;
            }
            //update existing record
            var existingRecord = _latestTransactions.get(record.BookId);
            if(existingRecord.OlderThan(record)) _latestTransactions.replace(record.BookId, record);
        }
    }

    public Transaction GetLatest(String bookId){
        if(_latestTransactions.containsKey(bookId)) return _latestTransactions.get(bookId);
        return null;
    }

    public ArrayList<Transaction> GetUserActivity(int userId){
        var transactions = new ArrayList<Transaction>();
        for(var transaction: _transactions){
            if(transaction.UserId != userId) continue;
            transactions.add(transaction);
        }
        return transactions;
    }

    public ArrayList<Transaction> GetBookActivity(long isbn){
        var transactions = new ArrayList<Transaction>();
        for(var transaction: _transactions){
            var bookIsbn = Book.GetIsbn(transaction.BookId);
            if( bookIsbn!= isbn) continue;
            transactions.add(transaction);
        }
        return transactions;
    }

    public Transaction Get(int index){
        if(index < 0 || index >= _transactions.size()) return null;
        return _transactions.get(index);
    }

    public boolean Checkout(String bookId, int userId) throws IOException {
        var date = TimeUtilities.GetCurrentTime();
        var checkouts = GetPendingCheckouts(userId);
        var checkoutCount = checkouts==null? 0: checkouts.size();
        if(checkoutCount >= CheckoutLimit)
        {
            PrintUtilities.PrintWarningLine("Checkout limit reached. Cannot issue more books to user id:"+userId);
            return false;
        }
        bookId = Book.GetReducedId(bookId);
        return Add(Transaction.Create(bookId, userId, date, Transaction.CheckoutTag));
    }

    public int AddCheckouts(ArrayList<Checkout> checkouts) throws IOException {
        var count=0;
        for (var checkout:
                checkouts) {
            if(Checkout(checkout))
            {
                count++;
                PrintUtilities.PrintSuccessLine(checkout.BookId +" has been checked out by "+ checkout.UserId);
            }
            else PrintUtilities.PrintWarningLine("Checkout attempt was unsuccessful!!");
        }
        return count;
    }

    public boolean Checkout(Checkout co) throws IOException {
        return Checkout(co.BookId, co.UserId);
    }
    public boolean Return(String bookId) throws IOException {
        var transaction = GetLatest(bookId);
        if(transaction == null) {
            PrintUtilities.PrintErrorLine("Could not locate a checkout for: "+bookId);
            return false;
        }

        var date = TimeUtilities.GetCurrentTime();
        var userId = transaction.UserId;
        if(Add(Transaction.Create(bookId, userId, date, Transaction.ReturnTag))){
            return true;
        }
        return false;
    }
    public boolean Add(Transaction record) throws IOException {
        //make sure the book exists in the book database and the user in user database
        if(_bookDb.GetBook(record.BookId)== null){
            PrintUtilities.PrintWarningLine("WARNING:Unknown book id: "+ record.BookId);
            return false;
        }
        if(_userDb.GetUser(record.UserId) == null && record.UserId != Integer.MIN_VALUE){
            PrintUtilities.PrintWarningLine("WARNING:Unknown user id: "+ record.UserId);
            return false;
        }
        if(!_latestTransactions.containsKey(record.BookId)){
            _latestTransactions.put(record.BookId, record);
            _transactions.add(record);
            _appender.AppendTransactions(record);
            return true;
        }
        //if the latest transaction is of type checkout you cannot add another checkout and similarly for return
        var latestRecord = _latestTransactions.get(record.BookId);
        if (latestRecord.Type.equals(record.Type)) {
            PrintUtilities.PrintWarningLine("WARNING: cannot "+record.Type+ " a book that is already in that state. Book id:" + record.BookId);
            return false;
        }

        _latestTransactions.replace(record.BookId, record);
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
        if(_latestTransactions.containsKey(bookId)) return _latestTransactions.get(bookId).Type;
        return Transaction.UnknownTag;
    }
    public ArrayList<Transaction> GetPendingCheckouts(int userId) {
        var checkouts = new ArrayList<Transaction>();
        for (Transaction record: GetPendingCheckouts()) {
            if(userId != record.UserId) continue;
            checkouts.add(record);
        }
        return checkouts.size()==0? null: checkouts;
    }
    public ArrayList<Transaction> GetPendingCheckouts() {
        var checkouts = new ArrayList<Transaction>();
        for (Transaction record: _latestTransactions.values()) {
            if(record.Type.equals(Transaction.CheckoutTag))
                checkouts.add(record);
        }
        return checkouts;
    }
}

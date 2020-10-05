package com.ola.databases;

import com.ola.Appender;
import com.ola.dataStructures.Book;
import com.ola.dataStructures.Checkout;
import com.ola.dataStructures.Transaction;
import com.ola.parsers.FlatObjectParser;
import com.ola.utilities.PrintUtilities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CheckoutDb {
    private OutputStream _outputStream;
    private HashMap<String, Checkout> _checkouts;
    private UserDb _userDb;
    private BookDb _bookDb;
    private boolean _hasNewRecords =false;
    public final int CheckoutLimit = 5;

    public CheckoutDb(Iterable<Checkout> checkouts, UserDb userDb, BookDb bookDb, OutputStream outputStream){
        _userDb = userDb;
        _bookDb = bookDb;
        _outputStream = outputStream;
        _checkouts = new HashMap<>();

        for (var checkout: checkouts) {
            if(_bookDb.GetBook(checkout.BookId)== null) {
                System.out.println("WARNING: Invalid book id:"+checkout.BookId+". Ignoring transaction.");
                continue;
            }
            if(_userDb.GetUser(checkout.UserId) == null){
                System.out.println("WARNING: Invalid user id:"+checkout.UserId+". Ignoring transaction.");
                continue;
            }
            _checkouts.put(checkout.BookId, checkout);
        }

    }

    public Checkout GetCheckout(String bookId){
        return _checkouts.getOrDefault(bookId, null);
    }

    public boolean IsCheckedOut(String bookId){
        return _checkouts.containsKey(bookId);
    }
    public ArrayList<Checkout> GetCheckouts(int userId){
        var checkouts = new ArrayList<Checkout>();
        for (var checkout:
             _checkouts.values()) {
            if (checkout.UserId == userId) checkouts.add(checkout);
        }
        return checkouts;
    }

    public boolean TryAdd(Checkout checkout) {
        var checkouts = GetCheckouts(checkout.UserId);
        var checkoutCount = checkouts.size();
        if(checkoutCount >= CheckoutLimit)
        {
            PrintUtilities.PrintWarningLine("Checkout limit reached. Cannot issue more books to user id:"+checkout.UserId);
            return false;
        }
        _checkouts.put(checkout.BookId, checkout);
        _hasNewRecords = true;
        return true;
    }

    public int TryAddRange(Iterable<Checkout> checkouts){
        var count =0;
        for (var checkout:
             checkouts) {
            if(TryAdd(checkout)) count++;
        }
        return count;
    }

    public boolean Return (String bookId){
        if (_checkouts.containsKey(bookId)){
            _checkouts.remove(bookId);
            _hasNewRecords = true;
            return true;
        }
        return false;
    }

    public final String[] HeaderLines = new String[]{
            "#Onkur library book checkout records\n",
            "#Book Id = Onkur book id. Value = <String>\n",
            "#User Id = Onkur user id. Value = <Integer>\n",
            "#CheckoutDate = Checkout date. Value = <YYYY-MM-DD HH:MM:ss>\n",
            "#DueDate = Due date. Value = <YYYY-MM-DD HH:MM:ss>\n"
    };
    /// Write out all the records into an output stream
    public void Close() throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(_outputStream));
        //header lines
        for (String line: HeaderLines) {
            writer.write(line);
        }
        //not required, but for aesthetics
        writer.write(FlatObjectParser.RecordSeparator+'\n');
        for (var record: _checkouts.values()) {
            writer.write(record.toString()+'\n');
            writer.write(FlatObjectParser.RecordSeparator+'\n');
        }
        writer.close();
    }
}

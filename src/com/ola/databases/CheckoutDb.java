package com.ola.databases;

import com.ola.dataStructures.Checkout;
import com.ola.parsers.CheckoutCsvParser;
import com.ola.parsers.FlatObjectParser;
import com.ola.utilities.PrintUtilities;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutDb {
    private final BufferedWriter _appender;
    private OutputStream _outputStream;
    private HashMap<String, Checkout> _checkouts;
    private boolean _hasReturns =false;
    public final int CheckoutLimit = 15;

    public CheckoutDb(Iterable<Checkout> checkouts, OutputStream outputStream)  {
        _outputStream = outputStream;
        _checkouts = new HashMap<>();

        for (var checkout:
             checkouts) {
            _checkouts.put(checkout.BookId, checkout);
        }

        if(_outputStream != null) _appender = new BufferedWriter(new OutputStreamWriter(_outputStream));
        else _appender = null;
    }


    public Checkout GetCheckout(String bookId){
        return _checkouts.getOrDefault(bookId, null);
    }

    public boolean IsCheckedOut(String bookId){
        return _checkouts.containsKey(bookId);
    }
    public ArrayList<Checkout> ReadCheckouts(int userId){
        var checkouts = new ArrayList<Checkout>();
        for (var checkout:
             _checkouts.values()) {
            if (checkout.UserId == userId) checkouts.add(checkout);
        }
        return checkouts;
    }

    public boolean TryAdd(Checkout checkout, BookDb bookDb, UserDb userDb)  {
        if(bookDb.GetBook(checkout.BookId)== null) {
            System.out.println("WARNING: Invalid book id:"+checkout.BookId+". Ignoring transaction.");
            return false;
        }
        if(userDb.GetUser(checkout.UserId) == null){
            System.out.println("WARNING: Invalid user id:"+checkout.UserId+". Ignoring transaction.");
            return false;
        }

        if(IsCheckedOut(checkout.BookId)) {
            PrintUtilities.PrintWarningLine("Can not checkout the same book twice:"+checkout.BookId);
            return false;
        }
        var checkouts = ReadCheckouts(checkout.UserId);
        var checkoutCount = checkouts.size();
        if(checkoutCount >= CheckoutLimit)
        {
            PrintUtilities.PrintWarningLine("Checkout limit reached. Cannot issue more books to user id:"+checkout.UserId);
            return false;
        }
        _checkouts.put(checkout.BookId, checkout);

        return _appender == null? true: Append(checkout);
    }

    private boolean Append(Checkout checkout) {

        try {
            _appender.write(checkout.toString()+'\n');
            _appender.write(FlatObjectParser.RecordSeparator+'\n');
            _appender.flush();
        } catch (IOException e) {
            System.out.println("Failed to append checkouts.\n"+ checkout.toString());
            return false;
        }

        return true;
    }

    public int TryAddRange(Iterable<Checkout> checkouts, BookDb bookDb, UserDb userDb) {
        var count =0;
        if (_appender == null)
            PrintUtilities.PrintWarningLine("Checkout appender set to null. No entry was saved");

        for (var checkout:
             checkouts) {
            if(TryAdd(checkout, bookDb, userDb)) count++;
        }
        return count;
    }

    public boolean Return (String bookId){
        if (_checkouts.containsKey(bookId)){
            _checkouts.remove(bookId);
            _hasReturns = true;
            return true;
        }
        return false;
    }

    public static final String[] HeaderLines = new String[]{
            "#Onkur library book checkout records\n",
            "#Book Id = Onkur book id. Value = <String>\n",
            "#User Id = Onkur user id. Value = <Integer>\n",
            "#CheckoutDate = Checkout date. Value = <YYYY-MM-DD HH:MM:ss>\n",
            "#DueDate = Due date. Value = <YYYY-MM-DD HH:MM:ss>\n"
    };

    public void Close() throws IOException {
        if(_appender != null)_appender.close();
        if(_outputStream != null) _outputStream.close();
    }
}

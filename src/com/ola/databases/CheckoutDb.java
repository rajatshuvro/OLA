package com.ola.databases;

import com.ola.dataStructures.Checkout;
import com.ola.dataStructures.Return;
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

    private UserDb _userDb;
    private BookDb _bookDb;

    public CheckoutDb(Iterable<Checkout> checkouts, OutputStream outputStream, UserDb userDb, BookDb idDb)  {
        _userDb = userDb;
        _bookDb = idDb;

        _outputStream = outputStream;
        _checkouts = new HashMap<>();

        if(checkouts!=null)
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
    public ArrayList<Checkout> ReadCheckouts(String userId){
        var checkouts = new ArrayList<Checkout>();
        for (var checkout:
             _checkouts.values()) {
            if (checkout.UserId.equals(userId)) checkouts.add(checkout);
        }
        return checkouts;
    }

    public boolean TryAdd(Checkout checkout)  {
        var book =_bookDb.GetBook(checkout.BookId);
        if( book == null) {
            PrintUtilities.PrintWarningLine("WARNING: Invalid book id:"+checkout.BookId+". Ignoring transaction.");
            return false;
        }
        var user = _userDb.ResolveUser(checkout.UserId, checkout.Email);

        if( user == null){
            PrintUtilities.PrintWarningLine("WARNING: Invalid user id:"+checkout.UserId+". Ignoring transaction.");
            return false;
        }

        var bookId = book.GetId();
        if(IsCheckedOut(bookId)) {
            PrintUtilities.PrintWarningLine("Can not checkout the same book twice:"+checkout.BookId);
            return false;
        }
        var checkouts = ReadCheckouts(user.Id);
        var checkoutCount = checkouts.size();
        if(checkoutCount >= CheckoutLimit)
        {
            PrintUtilities.PrintWarningLine("Checkout limit reached. Cannot issue more books to user id:"+checkout.UserId);
            return false;
        }
        _checkouts.put(bookId, checkout);

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

    public int TryAddRange(Iterable<Checkout> checkouts) {
        var count =0;
        if (_appender == null)
            PrintUtilities.PrintWarningLine("Checkout appender set to null. No entry was saved");

        for (var checkout:
             checkouts) {
            if(TryAdd(checkout)) count++;
        }
        return count;
    }

    public boolean Return (Return record){
        var book = _bookDb.GetBook(record.BookId);
        if (book == null) return false;

        var bookId = book.GetId();
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
            "#Checkout Date = Checkout date. Value = <YYYY-MM-DD HH:MM:ss>\n",
            "#Due Date = Due date. Value = <YYYY-MM-DD HH:MM:ss>\n"
    };

    public void WriteReturns(OutputStream rewriteStream, boolean leaveOpen) throws IOException{
        if (!_hasReturns) return;
        if(rewriteStream == null) {
            PrintUtilities.PrintErrorLine("Pending returns require a re-write stream to be recorded.");
            throw new IOException("Missing re-write stream");
        }
        var writer =  new BufferedWriter(new OutputStreamWriter(rewriteStream));
        for (var line:
                HeaderLines) {
            writer.write(line);

        }
        writer.write(FlatObjectParser.RecordSeparator+"\n");
        for (var checkout: _checkouts.values()) {
            writer.write(checkout.toString()+'\n');
            writer.write(FlatObjectParser.RecordSeparator+"\n");
        }

        writer.close();
        if(!leaveOpen) rewriteStream.close();
        _hasReturns = false;

    }

    public void Close() throws IOException {
        if(_appender != null)_appender.close();
        if(_outputStream != null) _outputStream.close();
    }

    public boolean HasReturns() {
        return _hasReturns;
    }
}

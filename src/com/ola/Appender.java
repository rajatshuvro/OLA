package com.ola;

import com.ola.dataStructures.Book;
import com.ola.dataStructures.Transaction;
import com.ola.dataStructures.User;
import com.ola.parsers.FlatObjectParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Appender {
    private OutputStream _bookStream;
    private OutputStream _userStream;
    private OutputStream _transactionStream;

    private BufferedWriter _transactionAppender;
    private BufferedWriter _bookAppender;
    private BufferedWriter _userAppender;

    public Appender(OutputStream bookStream, OutputStream userStream, OutputStream transactionStream){
        _bookStream = bookStream;
        _userStream = userStream;
        _transactionStream = transactionStream;
    }

    public void AppendTransactions(Transaction transaction) throws IOException {
        if(_transactionAppender == null)
            _transactionAppender = new BufferedWriter(new OutputStreamWriter(_transactionStream));
        if(transaction == null ) return;
        _transactionAppender.write(transaction.toString()+'\n');
        _transactionAppender.write(FlatObjectParser.RecordSeparator +'\n');
        _transactionAppender.flush();
    }

    public void AppendBooks(Iterable<Book> books) throws IOException {
        if(_bookAppender == null)
            _bookAppender = new BufferedWriter(new OutputStreamWriter(_bookStream));
        if(books == null ) return;
        for (var book: books) {
            _bookAppender.write(book.toString()+'\n');
            _bookAppender.write(FlatObjectParser.RecordSeparator +'\n');
        }
        _bookAppender.flush();
    }

    public void AppendUsers(Iterable<User> users) throws IOException {
        if(_userAppender == null)
            _userAppender = new BufferedWriter(new OutputStreamWriter(_userStream));
        if(users == null ) return;
        for (var user: users) {
            _userAppender.write(user.toString()+'\n');
            _userAppender.write(FlatObjectParser.RecordSeparator +'\n');
        }
        _userAppender.flush();
    }

    public void Close() throws IOException {
        if(_transactionAppender!= null) _transactionAppender.close();
        if(_bookAppender!= null) _bookAppender.close();
        if(_userAppender!= null) _userAppender.close();
    }

}

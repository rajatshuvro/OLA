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
    private BufferedWriter _transactionAppender;
    private BufferedWriter _bookAppender;
    private BufferedWriter _userAppender;
    
    public Appender(OutputStream bookStream, OutputStream userStream, OutputStream transactionStream){
        _userAppender = userStream == null? null: new BufferedWriter(new OutputStreamWriter(userStream));
        _bookAppender = bookStream == null? null: new BufferedWriter(new OutputStreamWriter(bookStream));
        _transactionAppender = transactionStream == null? null: new BufferedWriter(new OutputStreamWriter(transactionStream));
    }

    public void AppendTransactions(Transaction transaction) throws IOException {
        if(transaction == null || _transactionAppender == null) return;
        _transactionAppender.write(transaction.toString()+'\n');
        _transactionAppender.write(FlatObjectParser.RecordSeparator +'\n');
        _transactionAppender.flush();
    }

    public void AppendBooks(Iterable<Book> books) throws IOException {
        if(books == null || _bookAppender == null) return;
        for (var book: books) {
            _bookAppender.write(book.toString()+'\n');
            _bookAppender.write(FlatObjectParser.RecordSeparator +'\n');
        }
        _bookAppender.flush();
    }
    public void AppendUsers(Iterable<User> users) throws IOException {
        if(users == null || _userAppender == null) return;
        for (var user: users) {
            _userAppender.write(user.toString()+'\n');
            _userAppender.write(FlatObjectParser.RecordSeparator +'\n');
        }
        _bookAppender.flush();
    }

    public void Close() throws IOException {
        if(_transactionAppender!= null) _transactionAppender.close();
        if(_bookAppender!= null) _bookAppender.close();
        if(_userAppender!= null) _userAppender.close();
    }
}

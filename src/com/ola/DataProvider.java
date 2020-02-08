package com.ola;

import com.ola.databases.BookDb;
import com.ola.databases.TransactionDb;
import com.ola.databases.UserDb;
import com.ola.parsers.BookParser;
import com.ola.parsers.TransactionParser;
import com.ola.parsers.UserParser;

import java.io.*;

public class DataProvider {
    public BookDb BookDb;
    public UserDb UserDb;
    public TransactionDb TransactionDb;

    private BookParser _bookParser;
    private UserParser _userParser;
    private TransactionParser _transactionParser;

    public final Appender Appender;

    private InputStream _bookInputStream;
    private InputStream _userInputStream;
    private InputStream _transactionInputStream;

    private OutputStream _bookAppendStream;
    private OutputStream _userAppendStream;
    private OutputStream _transactionAppendStream;

    public DataProvider(InputStream bookInputStream, InputStream userInputStream, InputStream transactionInputStream
                        , OutputStream transactionAppendStream, OutputStream bookAppendStream, OutputStream userAppendStream) {
        _bookInputStream = bookInputStream;
        _userInputStream = userInputStream;
        _transactionInputStream = transactionInputStream;

        _bookAppendStream = bookAppendStream;
        _userAppendStream = userAppendStream;
        _transactionAppendStream = transactionAppendStream;

        Appender = new Appender(bookAppendStream, userAppendStream, transactionAppendStream);

        _bookParser = new BookParser(bookInputStream);
        _userParser = new UserParser(userInputStream);
        _transactionParser = new TransactionParser(transactionInputStream);

    }

    public void Load() throws IOException{
        BookDb = new BookDb(_bookParser.GetBooks());
        UserDb = new UserDb(_userParser.GetUsers());
        TransactionDb = new TransactionDb(_transactionParser.GetTransactions(), UserDb, BookDb, Appender);

        BookDb.GetSearchIndex();
        UserDb.GetSearchIndex();

        _userInputStream.close();
        _bookInputStream.close();
        _transactionInputStream.close();
    }

    public void Close() throws IOException {
        Appender.Close();
        _userAppendStream.close();
        _bookAppendStream.close();
        _transactionAppendStream.close();
    }
}

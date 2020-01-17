package com.ola;

import com.ola.dataStructures.Book;
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
    public TransactionDb TransactionsDb;

    private BookParser _bookParser;
    private UserParser _userParser;
    private TransactionParser _transactionParser;

    private InputStream _bookInputStream;
    private InputStream _userInputStream;
    private InputStream _transactionInputStream;
    private OutputStream _transactionAppendStream;
    private OutputStream _bookAppendStream;

    public DataProvider(InputStream bookInputStream, InputStream userInputStream, InputStream transactionInputStream
                        , OutputStream transactionAppendStream, OutputStream bookAppendStream) {
        _bookInputStream = bookInputStream;
        _userInputStream = userInputStream;
        _transactionInputStream = transactionInputStream;
        _transactionAppendStream = transactionAppendStream;
        _bookAppendStream = bookAppendStream;

        _bookParser = new BookParser(bookInputStream);
        _userParser = new UserParser(userInputStream);
        _transactionParser = new TransactionParser(transactionInputStream);
    }

    public void Load() throws IOException{
        BookDb = new BookDb(_bookParser.GetBooks());
        UserDb = new UserDb(_userParser.GetUsers());
        TransactionsDb = new TransactionDb(_transactionParser.GetTransactions(), UserDb.GetIds(), BookDb.GetIds());

        System.out.print("Building search index on all books...");
        BookDb.GetSearchIndex();
        System.out.println("done");


        _userInputStream.close();
        _bookInputStream.close();
        _transactionInputStream.close();
    }

    public void Close() throws IOException {
        TransactionsDb.Append(_transactionAppendStream);
        _transactionAppendStream.close();

        BookDb.Append(_bookAppendStream);
        _bookAppendStream.close();
    }
}

package com.ola;

import com.ola.databases.BookDb;
import com.ola.databases.TransactionDb;
import com.ola.databases.UserDb;
import com.ola.parsers.BookParser;
import com.ola.parsers.TransactionParser;
import com.ola.parsers.UserParser;

import java.io.FileInputStream;
import java.io.IOException;

public class DataProvider {
    public BookDb BookDb;
    public UserDb UserDb;
    public TransactionDb TransactionsDb;

    private BookParser _bookParser;
    private UserParser _userParser;
    private TransactionParser _transactionParser;

    public DataProvider(FileInputStream bookInputStream, FileInputStream userInputStream, FileInputStream transactionInputStream) {
        _bookParser = new BookParser(bookInputStream);
        _userParser = new UserParser(userInputStream);
        _transactionParser = new TransactionParser(transactionInputStream);

    }

    public void Load() throws IOException{
        BookDb = new BookDb(_bookParser.GetBooks());
        UserDb = new UserDb(_userParser.GetUsers());
        TransactionsDb = new TransactionDb(_transactionParser.GetTransactions(), UserDb.GetIds(), BookDb.GetIds());
    }
}

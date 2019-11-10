package com.ola;

import com.ola.databases.BookDb;
import com.ola.databases.TransactionDb;
import com.ola.databases.UserDb;
import com.ola.parsers.BookParser;
import com.ola.parsers.UserParser;

import java.io.FileInputStream;
import java.io.IOException;

public class DataProvider {
    public BookDb BooksDb;
    public UserDb UserDb;

    private BookParser _bookParser;
    private UserParser _userParser;
    public TransactionDb Transactions;

    public DataProvider(FileInputStream bookInputStream, FileInputStream userInputStream, FileInputStream fileInputStream) {
        _bookParser = new BookParser(bookInputStream);
        _userParser = new UserParser(userInputStream);
    }

    public void Load() throws IOException{
        BooksDb = new BookDb(_bookParser.GetBooks());
        UserDb = new UserDb(_userParser.GetUsers());
    }
}

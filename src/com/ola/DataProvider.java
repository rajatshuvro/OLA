package com.ola;

import com.ola.databases.BookDb;
import com.ola.databases.LendingDb;
import com.ola.databases.UserDb;
import com.ola.parsers.BookTsvParser;
import com.ola.parsers.UserTsvParser;

import java.io.FileInputStream;
import java.io.IOException;

public class DataProvider {
    public BookDb BooksDb;
    public UserDb UserDb;

    private BookTsvParser _bookParser;
    private UserTsvParser _userParser;
    public LendingDb Lendings;

    public DataProvider(FileInputStream bookInputStream, FileInputStream userInputStream) {
        _bookParser = new BookTsvParser(bookInputStream);
        _userParser = new UserTsvParser(userInputStream);
    }

    public void Load() throws IOException{
        BooksDb = new BookDb(_bookParser.GetBooks());
        UserDb = new UserDb(_userParser.GetUsers());
    }
}

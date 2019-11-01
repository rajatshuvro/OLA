package com.ola;

import com.ola.databases.BookDb;
import com.ola.databases.LendingDb;
import com.ola.databases.UserDb;
import com.ola.parsers.BookTsvParser;

import java.io.FileInputStream;
import java.io.IOException;

public class DataProvider {
    public BookDb BooksDb;
    private BookTsvParser _bookParser;
    public UserDb Users;
    public LendingDb Lendings;

    public DataProvider(FileInputStream bookInputStream) {
        _bookParser = new BookTsvParser(bookInputStream);

    }

    public void Load() throws IOException{
        BooksDb = new BookDb(_bookParser.GetBooks());
    }
}

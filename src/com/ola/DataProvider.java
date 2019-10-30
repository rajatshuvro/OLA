package com.ola;

import java.io.FileInputStream;
import java.io.IOException;

public class DataProvider {
    public BookDb Books;
    private BookTsvParser _bookParser;
    public UserDb Users;
    public LendingDb Lendings;

    public DataProvider(FileInputStream bookInputStream) {
        _bookParser = new BookTsvParser(bookInputStream);

    }

    public void Load() throws IOException{
        Books = new BookDb(_bookParser.Load());
    }
}

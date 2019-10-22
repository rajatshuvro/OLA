package com.ola;

import java.io.IOException;

public class DataProvider {
    public BookDb Books;
    public UserDb Users;
    public LendingDb Lendings;

    public DataProvider(String bookDbPath) {
        Books = new BookDb(bookDbPath);
    }

    public void Load() throws IOException{
        Books.Load();

    }
}

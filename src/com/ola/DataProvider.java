package com.ola;

import java.io.FileInputStream;
import java.io.IOException;

public class DataProvider {
    public BookDb Books;
    public UserDb Users;
    public LendingDb Lendings;

    public DataProvider(FileInputStream bookInputStream) {
        Books = new BookDb(bookInputStream);
    }

    public void Load() throws IOException{
        Books.Load();

    }
}

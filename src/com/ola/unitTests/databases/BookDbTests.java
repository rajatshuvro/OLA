package com.ola.unitTests.databases;
import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookDbTests {
    public static ArrayList<Book> GetBooks() {
        var books = new ArrayList<Book>();
        books.add(Book.Create(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "General", 4,1, null, null, null));
        books.add(Book.Create(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "General", 4,2, null, null, null));
        books.add(Book.Create(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,1, null, null, null));
        books.add(Book.Create(456098,"Nanda Mitra","Robindra Prem","Bakura Publishers",
                2018, 15, 12, "Fiction", 6,1, null, null, null));

        return books;
    }

    @Test
    public void SearchByIsbn() throws IOException{
        var bookDb = new BookDb(GetBooks());
        assertEquals(2, bookDb.GetBooks(7890788L).size());
        assertEquals(0, bookDb.GetBooks(12345678L).size());
    }
    @Test
    public void LatestCopyNumber() throws IOException{
        var bookDb = new BookDb(GetBooks());
        assertEquals(2, bookDb.GetCopyCount(7890788L));
        assertEquals(1, bookDb.GetCopyCount(678564L));
        assertEquals(0, bookDb.GetCopyCount(123456L));
    }
    @Test
    public void Search(){
        var bookDb = new BookDb(GetBooks());
        var result = bookDb.Filter("Fiction", -1);
        assertEquals(2, result.size());

        result = bookDb.Filter(null, 4);
        assertEquals(2, result.size());

        result = bookDb.Filter("Fiction", 4);
        assertEquals(0, result.size());

        result = bookDb.Filter("Fiction", 5);
        assertEquals(1, result.size());
    }
}

package com.ola.unitTests;
import com.ola.AddBooks;
import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddBooksTest {
    public ArrayList<Book> GetNewBooks() {
        var books = new ArrayList<Book>();
        books.add(new Book(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "GEN", 4,-1, null, null));
        books.add(new Book(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "FIC", 5,-1, null, null));
        books.add(new Book(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "FIC", 5,-1, null, null));
        books.add(new Book(456098,"Nanda Mitra","Robindra Prem","Bakura Publishers",
                2018, 15, 12, "FIC", 6,-1, null, null));

        return books;
    }

    public ArrayList<Book> GetMismatchingBook() {
        var books = new ArrayList<Book>();
        books.add(new Book(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "FIC", 4,-1, null, null));
        books.add(new Book(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "FIC", 6,-1, null, null));
        books.add(new Book(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "FIC", 5,-1, null, null));
        books.add(new Book(456098,"Nandana Mitra","Robindra Prem","Bakura Publishers",
                2018, 15, 12, "FIC", 6,-1, null, null));

        return books;
    }
    @Test
    public void AddBooks() throws IOException {
        var bookDb = new BookDb(BookDbTests.GetBooks());

        AddBooks.AddNewBook(GetNewBooks(), bookDb);

        assertEquals(3, bookDb.GetLatestCopyNumber(7890788));
        assertEquals(3, bookDb.GetLatestCopyNumber(678564));
        assertEquals(2, bookDb.GetLatestCopyNumber(456098));

    }

    @Test
    public void AddBooks_details_mismatch() throws IOException {
        var bookDb = new BookDb(BookDbTests.GetBooks());
        AddBooks.AddNewBook(GetMismatchingBook(), bookDb);

        assertEquals(5, bookDb.Count());
    }

}

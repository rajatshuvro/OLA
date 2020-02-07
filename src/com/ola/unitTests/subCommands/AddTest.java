package com.ola.unitTests.subCommands;
import com.ola.Add;
import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import com.ola.unitTests.databases.BookDbTests;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddTest {
    public ArrayList<Book> GetNewBooks() {
        var books = new ArrayList<Book>();
        books.add(Book.Create(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "General", 4,-1, null, null, null));
        books.add(Book.Create(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,-1, null, null, null));
        books.add(Book.Create(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,-1, null, null, null));
        books.add(Book.Create(456098,"Nanda Mitra","Robindra Prem","Bakura Publishers",
                2018, 15, 12, "Fiction", 6,-1, null, null, null));

        return books;
    }

    public ArrayList<Book> GetMismatchingBook() {
        var books = new ArrayList<Book>();
        books.add(Book.Create(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "Fiction", 4,-1, null, null, null));
        books.add(Book.Create(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 6,-1, null, null, null));
        books.add(Book.Create(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,-1, null, null, null));
        books.add(Book.Create(456098,"Nandana Mitra","Robindra Prem","Bakura Publishers",
                2018, 15, 12, "Fiction", 6,-1, null, null, null));

        return books;
    }
    @Test
    public void AddBooks() throws IOException {
        var bookDb = new BookDb(BookDbTests.GetBooks());

        Add.AddNewBook(GetNewBooks(), bookDb);

        assertEquals(3, bookDb.GetCopyCount(7890788));
        assertEquals(3, bookDb.GetCopyCount(678564));
        assertEquals(2, bookDb.GetCopyCount(456098));

    }

    @Test
    public void AddBooks_details_mismatch() throws IOException {
        var bookDb = new BookDb(BookDbTests.GetBooks());
        Add.AddNewBook(GetMismatchingBook(), bookDb);

        assertEquals(5, bookDb.Count());
    }

}

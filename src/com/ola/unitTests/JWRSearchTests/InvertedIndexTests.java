package com.ola.unitTests.JWRSearchTests;

import com.ola.JWRSearch.JWRInvertedIndex;
import com.ola.dataStructures.Book;
import com.ola.parsers.BookParser;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class InvertedIndexTests {
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
    public void AddDocsAndGetScores() throws IOException {
        var books = GetBooks();

        var jwrIndex = new JWRInvertedIndex();
        for(var book:books){
            jwrIndex.Add(book.GetContent());
        }

        var topDocs = jwrIndex.Search("amar bonosre");

        Assertions.assertEquals(books.size(), topDocs.length);
        Assertions.assertArrayEquals(new int[]{0,1,2,3}, topDocs);
    }
}

package com.ola.unitTests.lucene;

import com.ola.dataStructures.Book;
import com.ola.luceneIndex.SearchIndex;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.util.ArrayList;

public class SearchTests {
    public ArrayList<Book> GetNewBooks() {
        var books = new ArrayList<Book>();
        books.add(new Book(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "General", 4,-1, null, null));
        books.add(new Book(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,-1, null, null));
        books.add(new Book(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,-1, null, null));
        books.add(new Book(456098,"Nandana Mitra","Robindra Prem","Bakura Publishers",
                2018, 15, 12, "Fiction", 6,-1, null, null));

        return books;
    }
    @Test
    public void SearchReadingLevel() throws IOException, ParseException {
        var searcher = new SearchIndex(GetNewBooks());

        var topHitsIndices = searcher.Search("reading level 5", 4);
        assertArrayEquals(new int[]{1, 2, 0, 3}, topHitsIndices);
    }

    @Test
    public void SearchAuthor() throws IOException, ParseException {
        var searcher = new SearchIndex(GetNewBooks());

        var topHitsIndices = searcher.Search("Nandana", 4);
        assertArrayEquals(new int[]{3}, topHitsIndices);
    }
}

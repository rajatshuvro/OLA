package com.ola.unitTests.parsers;

import java.io.*;

import com.ola.parsers.BookParser;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookParserTests {

    @Test
    public void ParseBook() throws IOException{
        var bookParser = new BookParser(TestStreams.GetBooksStream());
        var books = bookParser.GetBooks();
        var count = books.size();
        bookParser.Close();
        assertEquals(4, count);
        assertNull(books.get(2).ExpiryDate);
    }


    @Test
    public void ParseAntiqueBooks() throws IOException{
        var bookParser = new BookParser(TestStreams.GetAntiqueBooksStream());
        var books = bookParser.GetBooks();
        var count = books.size();
        bookParser.Close();
        assertEquals(2, count);
        assertEquals(232192935, books.get(0).Isbn);
        assertEquals(222188120, books.get(1).Isbn);

    }


}

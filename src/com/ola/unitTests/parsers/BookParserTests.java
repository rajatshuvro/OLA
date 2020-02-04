package com.ola.unitTests.parsers;

import com.ola.parsers.BookParser;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookParserTests {

    @Test
    public void ParseBook() throws IOException{
        var stream = TestStreams.GetBooksStream();
        var bookParser = new BookParser(stream);
        stream.close();
        var books = bookParser.GetBooks();
        var count = books.size();
        assertEquals(4, count);
        assertEquals("Little girl describes her daddy", books.get(0).Summary);
        assertNull(books.get(2).ExpiryDate);
    }


    @Test
    public void ParseAntiqueBooks() throws IOException{
        var stream = TestStreams.GetAntiqueBooksStream();
        var bookParser = new BookParser(stream);
        var books = bookParser.GetBooks();
        stream.close();
        var count = books.size();
        assertEquals(2, count);
        assertEquals(232192935, books.get(0).Isbn);
        assertEquals(222188120, books.get(1).Isbn);

    }


}

package com.ola.unitTests.parsers;

import com.ola.parsers.BookCsvParser;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class BookCsvParserTests {

    @Test
    public void GetCsvBookRecord() throws IOException {
        var parser = new BookCsvParser(TestStreams.GetBookCsvStream());

        var books = parser.GetBooks();
        assertEquals(3, books.size());
        assertEquals("Koto boro shalgom", books.get(0).Summary);
    }
}

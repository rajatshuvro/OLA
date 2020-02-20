package com.ola.unitTests.subCommands;
import com.ola.LabelPrinter;
import com.ola.parsers.BookParser;
import com.ola.unitTests.TestStreams;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LabelPrinterTests {
    @Test
    public void GetLabelsAfter() throws IOException {
        var stream = TestStreams.GetBooksStream();
        var bookParser = new BookParser(stream);
        stream.close();
        var books = bookParser.GetBooks();

        var booksAfter = LabelPrinter.GetSortedBooks(books, TimeUtilities.parseDate("2018-06-17"), null);
        assertEquals(2, booksAfter.size());
        assertEquals("Bhua Bhalobasha", booksAfter.get(0).Title);
    }

    @Test
    public void GetLabelsBefore() throws IOException {
        var stream = TestStreams.GetBooksStream();
        var bookParser = new BookParser(stream);
        stream.close();
        var books = bookParser.GetBooks();

        var booksAfter = LabelPrinter.GetSortedBooks(books, null, TimeUtilities.parseDate("2018-06-17"));
        assertEquals(2, booksAfter.size());
        assertEquals("Amar Baba", booksAfter.get(0).Title);
    }

    @Test
    public void GetLabelsBeforeAndAfter() throws IOException {
        var stream = TestStreams.GetBooksStream();
        var bookParser = new BookParser(stream);
        stream.close();
        var books = bookParser.GetBooks();

        var booksAfterBefore = LabelPrinter.GetSortedBooks(books, TimeUtilities.parseDate("2018-05-17"), TimeUtilities.parseDate("2018-11-28"));
        assertEquals(3, booksAfterBefore.size());
        assertEquals("Amar Baba", booksAfterBefore.get(0).Title);
    }
}

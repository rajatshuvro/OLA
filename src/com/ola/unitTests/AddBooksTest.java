package com.ola.unitTests;
import com.ola.AddBooks;
import com.ola.databases.BookDb;
import com.ola.parsers.BookTsvParser;
import org.junit.jupiter.api.Test;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddBooksTest {
    public InputStream GetNewBooksStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("Title\tAuthor\tISBN\tPage count\tPrice\tPublisher\tGenre\tReading level\tYear\n");
        writer.write("Amar Baba\tRajat Shuvro Roy\t7890788\t5\t5\tBonosree books and co\tGEN\t4\t2015\n");
        writer.write("Bhua Bhalobasha \tSaber Nabil\t678564\t10\t10\tDakkhin Khan Publishers\tFIC\t5\t2017\n");
        writer.write("Bhua Bhalobasha \tSaber Nabil\t678564\t10\t10\tDakkhin Khan Publishers\tFIC\t5\t2017\n");
        writer.write("Robindra Prem\tNanda Mitra\t456098\t15\t10\tBakura Publishers\tFIC\t6\t2018\n");

        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }

    @Test
    public void AddBooks() throws IOException {
        var bookParser = new BookTsvParser(BookDbTests.GetBooksStream());
        var bookDb = new BookDb(bookParser.GetBooks());

        AddBooks.AddNewBook(GetNewBooksStream(), bookDb);

        assertEquals(3, bookDb.GetLatestCopyNumber(7890788));
        assertEquals(3, bookDb.GetLatestCopyNumber(678564));
        assertEquals(2, bookDb.GetLatestCopyNumber(456098));

    }
}

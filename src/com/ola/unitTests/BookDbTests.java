package com.ola.unitTests;
import com.ola.BookDb;
import com.ola.BookTsvParser;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookDbTests {
    private InputStream GetBooksStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("Title\tAuthor\tISBN\tCopy number\tPage count\tPrice\tPublisher\tGenre\tReading level\tYear\n");
        writer.write("Amar Baba\tRajat Shuvro Roy\t7890788\t1\t5\t5\tBonosree books and co\tGEN\t4\t2015\n");
        writer.write("Amar Baba\tRajat Shuvro Roy\t7890788\t2\t5\t5\tBonosree books and co\tGEN\t4\t2015\n");
        writer.write("Bhua Bhalobasha \tSaber Nabil\t678564\t1\t10\t10\tDakkhin Khan Publishers\tFIC\t5\t2017\n");
        writer.write("Robindra Prem\tNanda Mitra\t456098\t1\t15\t10\tBakura Publishers\tFIC\t6\t2018\n");

        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }
    @Test
    public void ParseBooks() throws IOException{
        var bookParser = new BookTsvParser(GetBooksStream());
        var bookDb = new BookDb(bookParser.Load());
        var count = bookDb.Count();
        bookParser.Close();
        assertEquals(4, count);
    }

    @Test
    public void GetBooks() throws IOException{
        var bookParser = new BookTsvParser(GetBooksStream());
        var bookDb = new BookDb(bookParser.Load());
        bookParser.Close();
        assertEquals(2, bookDb.GetBooks(7890788L).size());
        assertEquals(0, bookDb.GetBooks(12345678L).size());
    }
    @Test
    public void LatestCopyNumber() throws IOException{
        var bookParser = new BookTsvParser(GetBooksStream());
        var bookDb = new BookDb(bookParser.Load());
        bookParser.Close();
        assertEquals(2, bookDb.GetLatestCopyNumber(7890788L));
        assertEquals(1, bookDb.GetLatestCopyNumber(678564L));
        assertEquals(0, bookDb.GetLatestCopyNumber(123456L));
    }
}

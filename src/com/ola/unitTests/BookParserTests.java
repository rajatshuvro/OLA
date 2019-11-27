package com.ola.unitTests;

import java.io.*;

import com.ola.parsers.BookParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookParserTests {
    public static InputStream GetBooksStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("#Onkur library books\n");
        writer.write("***************************************************************\n");
        writer.write("Title:\t\t\tAmar Baba\n");
        writer.write("Author:\t\t\tRajat Shuvro Roy\n");
        writer.write("ISBN:\t\t\t7890788\n");
        writer.write("Publisher:\t\tBonosree books and co\n");
        writer.write("Year:\t\t\t2015\n");
        writer.write("Genre:\t\t\tGeneral\n");
        writer.write("Copy number:\t1\n");
        writer.write("Page count:\t\t7\n");
        writer.write("Price:\t\t\t5\n");
        writer.write("Reading level:\t4\n");
        writer.write("Entry date:\t\t2018-05-17 11:35:06\n");
        writer.write("Expiry date:\t2018-11-10 11:35:34\n");
        writer.write("***************************************************************\n");
        writer.write("Title:\t\t\tAmar Baba\n");
        writer.write("Author:\t\t\tRajat Shuvro Roy\n");
        writer.write("ISBN:\t\t\t7890788\n");
        writer.write("Publisher:\t\tBonosree books and co\n");
        writer.write("Year:\t\t\t2015\n");
        writer.write("Genre:\t\t\tGeneral\n");
        writer.write("Copy number:\t2\n");
        writer.write("Page count:\t\t7\n");
        writer.write("Price:\t\t\t5\n");
        writer.write("Reading level:\t4\n");
        writer.write("Entry date:\t\t2018-05-17 11:35:06\n");
        writer.write("Expiry date:\t\n");

        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }

    @Test
    public void ParseBook() throws IOException{
        var bookParser = new BookParser(GetBooksStream());
        var books = bookParser.GetBooks();
        var count = books.size();
        bookParser.Close();
        assertEquals(2, count);
        assertNull(books.get(1).ExpiryDate);
    }

    public static InputStream GetAntiqueBooksStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("#Onkur library books\n");
        writer.write("***************************************************************\n");
        writer.write("Title:\t\t\tChotoder Ramayan\n");
        writer.write("Author:\t\t\tUpendrakishore Roy Choudhury\n");
        writer.write("ISBN:\t\t\t-1\n");
        writer.write("Publisher:\t\tKalighat prokashoni\n");
        writer.write("Year:\t\t\t1929\n");
        writer.write("Genre:\t\t\tFiction/Religion\n");
        writer.write("Page count:\t\t35\n");
        writer.write("Price:\t\t\t15\n");
        writer.write("Reading level:\t4\n");
        writer.write("***************************************************************\n");
        writer.write("Title:\t\t\tBorno porichoy\n");
        writer.write("Author:\t\t\tIshwarchandra Viddyasagar\n");
        writer.write("ISBN:\t\t\t-1\n");
        writer.write("Publisher:\t\tBengal publishers\n");
        writer.write("Year:\t\t\t1881\n");
        writer.write("Genre:\t\t\tGeneral\n");
        writer.write("Page count:\t\t20\n");
        writer.write("Price:\t\t\t10\n");
        writer.write("Reading level:\t3\n");

        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }

    @Test
    public void ParseAntiqueBooks() throws IOException{
        var bookParser = new BookParser(GetAntiqueBooksStream());
        var books = bookParser.GetBooks();
        var count = books.size();
        bookParser.Close();
        assertEquals(2, count);
        assertEquals(232192935, books.get(0).Isbn);
        assertEquals(222188120, books.get(1).Isbn);

    }


}

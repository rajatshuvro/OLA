package com.ola.parsers;

import com.ola.dataStructures.Book;
import com.ola.utilities.PrintUtilities;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BookCsvParser {
    private InputStreamReader _reader;
    private final String TimeTag = "Timestamp";
    private final String TitleTag = "Title";
    private final String AuthorTag = "Author";
    private final String IsbnTag = "ISBN";
    private final String PageCountTag = "Page count";
    private final String PriceTag = "Price";
    private final String PublisherTag = "Publisher";
    private final String GenreTag = "Genre";
    private final String ReadingLevelTag = "Reading level";
    private final String YearTag = "Year";
    private final String SummaryTag = "Summary";

    public BookCsvParser(InputStream stream){
        _reader = new InputStreamReader(stream);
    }

    public void Close() throws IOException {
        _reader.close();
    }

    public ArrayList<Book> GetBooks() throws IOException {
        var books = new ArrayList<Book>();
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader(TimeTag, IsbnTag,TitleTag, AuthorTag, PublisherTag,
                YearTag, SummaryTag, GenreTag, ReadingLevelTag, PageCountTag, PriceTag).parse(_reader);
        var isHeaderRecord = true;
        for (CSVRecord record : records) {
            //the first line is also reported as entry. We need to skip it
            if(isHeaderRecord) {
                isHeaderRecord = false;
                continue;
            }

            long isbn = ParserUtilities.ParseIsbn(record.get(IsbnTag));
            String title = record.get(TitleTag).trim();
            String author = record.get(AuthorTag).trim();
            String publisher = record.get(PublisherTag).trim();
            int year = ParserUtilities.ParseUInt(record.get(YearTag));
            var summary = record.get(SummaryTag).trim();
            var genre = record.get(GenreTag).trim();
            var readingLevel = ParserUtilities.ParseUInt(record.get(ReadingLevelTag));
            int pageCount = ParserUtilities.ParseUInt(record.get(PageCountTag));
            float price = ParserUtilities.ParseUFloat(record.get(PriceTag));

            if(isbn == -1)
            {
                isbn = Book.GenerateIsbn(title, author, publisher, year, pageCount);
                System.out.println("Generating ISBN for Title:"+title+"..."+ isbn);
            }
            if(ParserUtilities.IsNullOrEmpty(genre)) genre = Book.GeneralTag;

            var book = Book.Create(isbn, author,title, publisher, year, pageCount, price, genre, readingLevel, -1,
                    null, null, null);
            if(book==null) {
                PrintUtilities.PrintWarningLine("Unable to import:"+title);
                continue;
            }
            book.SetSummary(summary);
            books.add(book);
        }
        return books;
    }

}

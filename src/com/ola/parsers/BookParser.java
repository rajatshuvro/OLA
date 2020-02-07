package com.ola.parsers;

import com.ola.dataStructures.Book;
import com.ola.utilities.TimeUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BookParser {
    private InputStream _inputStream;

    private final String TitleTag = "Title";
    private final String AuthorTag = "Author";
    private final String IsbnTag = "ISBN";
    private final String PageCountTag = "Page count";
    private final String PriceTag = "Price";
    private final String PublisherTag = "Publisher";
    private final String GenreTag = "Genre";
    private final String ReadingLevelTag = "Reading level";
    private final String CopyNumTag = "Copy number";
    private final String YearTag = "Year";
    private final String EntryDateTag = "Entry date";
    private final String ExpiryDateTag = "Expiry date";
    private final String SummaryTag = "Summary";

    public BookParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    public ArrayList<Book> GetBooks() throws IOException {
        ArrayList<Book> books = new ArrayList<>();
        var fobParser = new FlatObjectParser(_inputStream, new String[]{
                TitleTag, AuthorTag, IsbnTag, PageCountTag, PriceTag, PublisherTag, GenreTag, ReadingLevelTag,
                CopyNumTag, YearTag, EntryDateTag, ExpiryDateTag, SummaryTag
        });

        var record =fobParser.GetNextRecord();
        while ( record != null){
            var book = GetBook(record);
            if (book != null) books.add(book);
            record = fobParser.GetNextRecord();
        }
        fobParser.close();
        return books;
    }

    private Book GetBook(HashMap<String, String> record) {
        var title       = record.get(TitleTag);
        var author      = record.get(AuthorTag);
        long isbn       = ParserUtilities.ParseIsbn(record.get(IsbnTag));
        var publisher   = record.get(PublisherTag);
        var summary     = record.get(SummaryTag);
        var year        = ParserUtilities.ParseUInt(record.get(YearTag));
        var genre       = record.get(GenreTag);
        var readingLevel = ParserUtilities.ParseUInt(record.get(ReadingLevelTag));
        var pageCount   = ParserUtilities.ParseUInt(record.get(PageCountTag));
        var price       = ParserUtilities.ParseUFloat(record.get(PriceTag));
        //when adding new books, the copy number field may be absent
        var copyNumber  = record.containsKey(CopyNumTag)?ParserUtilities.ParseUInt(record.get(CopyNumTag)):-1;
        Date entryDate  = TimeUtilities.parseDate(record.get(EntryDateTag));
        Date expiryDate = TimeUtilities.parseDate(record.get(ExpiryDateTag));

        if(isbn == -1)
        {
            isbn = Book.GenerateIsbn(title, author, publisher, year, pageCount);
            System.out.println("Generating ISBN for Title:"+title+"..."+ isbn);
        }
        return Book.Create(isbn, author,title, publisher, year, pageCount, price, genre, readingLevel, copyNumber,
                    entryDate, expiryDate, summary);

    }

}

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

    public BookParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    public ArrayList<Book> GetBooks() throws IOException {
        ArrayList<Book> books = new ArrayList<>();
        var fobParser = new FlatObjectParser(_inputStream, new String[]{
                TitleTag, AuthorTag, IsbnTag, PageCountTag, PriceTag, PublisherTag, GenreTag, ReadingLevelTag,
                CopyNumTag, YearTag, EntryDateTag, ExpiryDateTag
        });

        var nextSetOfValues =fobParser.GetNextSetOfValues();
        while ( nextSetOfValues != null){
            var book = GetBook(nextSetOfValues);
            if (book != null) books.add(book);
            nextSetOfValues = fobParser.GetNextSetOfValues();
        }
        fobParser.close();
        return books;
    }

    private Book GetBook(HashMap<String, String> keyValues) {
        String title = null;
        String author = null;
        long isbn = -1;
        int pageCount = -1;
        float price = (float) 0.0;
        String publisher  = null;
        int year =0;
        String genre = null;
        int readingLevel = -1;
        //when adding new books, the copy number field may be absent
        int copyNumber = -1;
        Date entryDate = null;
        Date expiryDate = null;

        for (var entry: keyValues.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            switch (key){
                case TitleTag:
                    title = value;
                    break;
                case AuthorTag:
                    author = value;
                    break;
                case IsbnTag:
                    isbn = ParserUtilities.ParseIsbn(value);
                    break;
                case PageCountTag:
                    pageCount = ParserUtilities.ParseUInt(value);
                    break;
                case PriceTag:
                    price = ParserUtilities.ParseUFloat(value);
                    break;
                case PublisherTag:
                    publisher = value;
                    break;
                case YearTag:
                    year = ParserUtilities.ParseUInt(value);
                    break;
                case GenreTag:
                    genre = value;
                    break;
                case ReadingLevelTag:
                    readingLevel = ParserUtilities.ParseUInt(value);
                    break;
                case CopyNumTag:
                    copyNumber = ParserUtilities.ParseUInt(value);
                    break;
                case EntryDateTag:
                    entryDate = TimeUtilities.parseDate(value);
                    break;
                case ExpiryDateTag:
                    expiryDate = TimeUtilities.parseDate(value);
                    break;

            }
        }
        if(isbn == -1)
        {
            isbn = Book.GenerateIsbn(title, author, publisher, year, pageCount);
            System.out.println("Generating ISBN for Title:"+title+"..."+ isbn);
        }
        if(Book.IsValid(isbn, author,title, publisher, year, pageCount, price, genre, readingLevel, copyNumber))
            return new Book(isbn, author,title, publisher, year, pageCount, price, genre, readingLevel, copyNumber,
                    entryDate, expiryDate);
        else return null;
    }

    public void Close() throws IOException {

        _inputStream.close();
    }
}

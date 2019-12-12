package com.ola.parsers;

import com.ola.dataStructures.Book;
import com.ola.utilities.TimeUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LegacyBookParser {
    private InputStream _inputStream;

    private final String TitleTag = "Title";
    private final String AuthorTag = "Author";
    private final String IsbnTag = "ISBN";
    private final String PageCountTag = "Page_Count";
    private final String PriceTag = "List_Price";
    private final String PublisherTag = "Publisher";
    private final String GenreTag = "Genre";
    private final String FicNonFicTag = "Fiction_Nonfiction_F_NF";
    private final String ReadingLevelTag = "Reading_Level";
    private final String CopyNumTag = "Copy number";
    private final String YearTag = "Publication_Date";
    private final String EntryDateTag = "Entry date";
    private final String ExpiryDateTag = "Expiry date";

    // TSV header indices
    private int TitleIndex = -1;
    private int AuthorIndex = -1;
    private int IsbnIndex = -1;
    private int PageCountIndex = -1;
    private int PriceIndex = -1;
    private int PublisherIndex = -1;
    private int GenreIndex = -1;
    private int FicNonFicIndex = -1;
    private int ReadingLevelIndex = -1;
    private int CopyNumIndex = -1;
    private int YearIndex = -1;


    public LegacyBookParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    public ArrayList<Book> GetBooks() throws IOException {
        ArrayList<Book> books = new ArrayList<>();

        try (Scanner scanner =  new Scanner(_inputStream)){
            boolean isFirstLine = true;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(line == null || line.length() == 0) continue;
                if(isFirstLine){
                    ParseHeader(line);
                    isFirstLine = false;
                }
                var book = GetBook(line);
                if (book != null){
                    books.add(book);
                }
            }
        }
        return books;
    }

    private void ParseHeader(String line) {
        var words = line.split("\t");
        var wordsToIndex = new HashMap<String, Integer>(words.length);
        for (int i = 0; i < words.length; i++){
            wordsToIndex.put(words[i], i);
        }

        // i want it to crash if any one of the fields is absent
        IsbnIndex = wordsToIndex.get(IsbnTag);
        TitleIndex = wordsToIndex.get(TitleTag);
        AuthorIndex = wordsToIndex.get(AuthorTag);
        PublisherIndex = wordsToIndex.get(PublisherTag);
        YearIndex = wordsToIndex.get(YearTag);
        PageCountIndex = wordsToIndex.get(PageCountTag);
        GenreIndex =  wordsToIndex.get(GenreTag);
        PriceIndex =  wordsToIndex.get(PriceTag);
        CopyNumIndex = wordsToIndex.get(CopyNumTag);
        FicNonFicIndex = wordsToIndex.get(FicNonFicTag);
        ReadingLevelIndex = wordsToIndex.get(ReadingLevelTag);

    }

    private Book GetBook(String line) {

        var words = line.split("\t");
        var title = words[TitleIndex];
        var author = words[AuthorIndex];
        var publisher = words[PublisherIndex];
        var genre = words[GenreIndex];
        var ficNonFic = words[FicNonFicIndex];
        var isbn = ParserUtilities.ParseULong(words[IsbnIndex]);
        var pageCount = ParserUtilities.ParseUInt(words[PageCountIndex]);
        var readingLevel = ParserUtilities.ParseUInt(words[PageCountIndex]);
        var copyNumber = ParserUtilities.ParseUInt(words[PageCountIndex]);
        var year = ParserUtilities.ParseUInt(words[YearIndex]);
        var price = ParserUtilities.ParseUFloat(words[PriceIndex]);

        genre = GetGenre(genre, ficNonFic);
        if(isbn == -1 || isbn == Long.MIN_VALUE)
        {
            isbn = Book.GenerateIsbn(title, author, publisher, year, pageCount);
            System.out.println("Generating ISBN for Title:"+title+"..."+ isbn);
        }
        if(Book.IsValid(isbn, author,title, publisher, year, pageCount, price, genre, readingLevel, copyNumber))
            return new Book(isbn, author,title, publisher, year, pageCount, price, genre, readingLevel, copyNumber,
                    TimeUtilities.GetCurrentTime(), null);
        else return null;
    }

    private String GetGenre(String genre, String ficNonFic) {
        if(ParserUtilities.IsNullOrEmpty(genre) || ParserUtilities.IsNullOrEmpty(ficNonFic)) return Book.GeneralTag;
        if(ParserUtilities.IsNullOrEmpty(genre)) return ficNonFic.equals("F")? Book.FictionTag: Book.GeneralTag;

        return Book.IsValidGenre(genre)? genre: Book.GeneralTag;
    }

    public void Close() throws IOException {
        _inputStream.close();
    }
}

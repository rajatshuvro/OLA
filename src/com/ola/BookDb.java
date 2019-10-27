package com.ola;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class BookDb {
    private InputStream _inputStream;
    private HashMap<String, Book> _books;
    public int Count(){
        return _books.size();
    }
    //Title    Author  ISBN    Page count      Price   Publisher       Genre   Reading level Copy number
    private int TitleIndex = -1;
    private int AuthorIndex = -1;
    private int IsbnIndex = -1;
    private int PageCountIndex = -1;
    private int PriceIndex = -1;
    private int PublisherIndex = -1;
    private int GenreIndex = -1;
    private int ReadingLevelIndex = -1;
    private int CopyNumberIndex =-1;
    private int YearIndex = -1;

    public final String TitleTag = "Title";
    public final String AuthorTag = "Author";
    public final String IsbnTag = "ISBN";
    public final String PageCountTag = "Page count";
    public final String PriceTag = "Price";
    public final String PublisherTag = "Publisher";
    public final String GenreTag = "Genre";
    public final String ReadingLevelTag = "Reading level";
    public final String CopyNumTag = "Copy number";
    public final String YearTag = "Year";


    public BookDb(InputStream inputStream) {
        _inputStream = inputStream;
        _books = new HashMap<>();
    }

    public int Load() throws IOException {
        Integer count=0;
        boolean isFirstLine = true;
        try (Scanner scanner =  new Scanner(_inputStream)){
            while (scanner.hasNextLine()){
                //process each line in some way
                String line = scanner.nextLine();
                if(isFirstLine){
                    SetColumnIndices(line);
                    isFirstLine = false;
                    continue;
                }
                var book = GetBook(line);
                if (book != null){
                    _books.put(book.GetId(),book);
                }
                count++;
            }
        }
        System.out.println("Loaded book database. Book count:"+ count);
        return count;
    }
    private void SetColumnIndices(String headerLine){
        var splits = headerLine.split("\t");
        var splitList =Arrays.asList(splits);

        TitleIndex = splitList.indexOf(TitleTag);
        AuthorIndex = splitList.indexOf(AuthorTag);
        IsbnIndex = splitList.indexOf( IsbnTag);
        PageCountIndex = splitList.indexOf( PageCountTag);
        PriceIndex = splitList.indexOf( PriceTag);
        PublisherIndex = splitList.indexOf( PublisherTag);
        GenreIndex = splitList.indexOf( GenreTag);
        ReadingLevelIndex = splitList.indexOf( ReadingLevelTag);
        CopyNumberIndex = splitList.indexOf(CopyNumTag);
        YearIndex = splitList.indexOf(YearTag);
    }

    private Book GetBook(String line) {
        var splits = line.split("\t");

        var title = splits[TitleIndex];
        var author = splits[AuthorIndex];
        var isbn = Long.parseLong(splits[IsbnIndex]);
        var pageCount = Integer.parseInt(splits[PageCountIndex]);
        var price = Integer.parseInt(splits[PriceIndex]);
        var publisher = splits[PublisherIndex];
        var year = Integer.parseInt(splits[YearIndex]);
        var genre = splits[GenreIndex];
        var readingLevel = Integer.parseInt(splits[ReadingLevelIndex]);
        var copyNumber = Integer.parseInt(splits[CopyNumberIndex]);

        if(!IsValidateGenre(genre)){
            System.out.println("Invalid genre provided:"+ genre+" .Skipping book:"+title);
            return null;
        }
        if(readingLevel <0 || readingLevel > 10) {
            System.out.println("Invalid reading level provided:"+ readingLevel +" .Skipping book:"+ title);
            return null;
        }

        return new Book(isbn, author,title, publisher, year, pageCount, price, genre, readingLevel, copyNumber);
    }

    private boolean IsValidateGenre(String genre) {
        return genre.equals("FIC") || genre.equals("GEN") || genre.equals("SCI") || genre.equals("SOC");
    }

    public void Close() throws IOException {
        _inputStream.close();
    }
}

package com.ola;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
//Title    Author  ISBN    Page_count      Price   Publisher       Genre   Reading level

public class Book {
    public final String Isbn;
    public final String Author;
    public final String Title;
    public final int Year;
    public final String Publisher;
    public final int CopyNum;
    public final int PageCount;
    public final float Price;
    public final Genre Genre;
    public final int ReadingLevel;
    public final long EntryTime;


    public Book(String isbn, String author, String title, String publisher, int year, int pageCount, float price, Genre genre, int readingLevel, long epochSeconds){
        Isbn = isbn;
        Author = author;
        Title = title;
        Publisher = publisher;
        Year = year;
        CopyNum = -1;//user not allowed to input copy number. I will be assigned once the book is submitted to central database
        PageCount = pageCount;
        Price = price;
        Genre = genre;
        ReadingLevel = readingLevel;
        EntryTime = epochSeconds;
    }

    public String GetId(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        var timeOfEntry = dtf.format(LocalDateTime.ofEpochSecond(EntryTime, 0, ZoneOffset.UTC));
        // todo: replace time of entry with copyNum
        String copyNumString = CopyNum ==-1 ? timeOfEntry: Integer.toString(CopyNum);
        return String.join("-", Isbn, Genre.name(), Integer.toString(ReadingLevel), copyNumString);
    }

    public enum Genre{
        FIC,
        SCI,
        SOS,
        REL
    }

    public enum ReadingLevel{
        INF, // infant
        TOD, // toddler
        KGN, // kinder garden
        ELE, // elementary
        MID, // middle
        HIG // High
    }
}

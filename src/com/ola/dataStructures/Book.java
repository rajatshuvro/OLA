package com.ola.dataStructures;

import java.util.Date;
import java.util.HashSet;

public class Book {
    public final long Isbn;
    public final String Author;
    public final String Title;
    public final int Year;
    public final String Publisher;
    public final int CopyNum;
    public final int PageCount;
    public final float Price;
    public final String Genre;
    public final int ReadingLevel;
    public final Date EntryDate;
    public final Date ExpiryDate;

    public Book(long isbn, String author, String title, String publisher, int year, int pageCount,
                float price, String genre, int readingLevel, int copyNum,
                Date entryDate, Date expiryDate){
        Isbn = isbn;
        Author = author;
        Title = title;
        Publisher = publisher;
        Year = year;
        CopyNum = copyNum;
        PageCount = pageCount;
        Price = price;
        Genre = genre;
        ReadingLevel = readingLevel;
        EntryDate = entryDate;
        ExpiryDate = expiryDate;
    }

    public static boolean IsValid(long isbn, String author, String title, String publisher, int year,
                                  int pageCount, float price, String genre, int readingLevel, int copyNumber) {
        return isbn != 0 &&
                author != null &&
                title != null &&
                pageCount > 0 &&
                IsValidateGenre(genre) &&
                ValidateReadingLevel(readingLevel);
    }

    public String GetUserFriendlyId(){
        return String.join("-", Long.toString(Isbn), Genre, Integer.toString(ReadingLevel), '('+Integer.toString(CopyNum)+')');
    }

    public String GetId(){
        return Long.toString(Isbn) +"-("+Integer.toString(CopyNum)+')';
    }

    public static final int MinReadingLevel =1;
    public static final int MaxReadingLevle = 10;
    public static boolean ValidateReadingLevel(int level){
        return level >= MinReadingLevel && level <= MaxReadingLevle;
    }
    //static fields
    public static final String FictionTag = "FIC";
    public static final String ScienceTag = "SCI";
    public static final String SocialScienceTag = "SOC";
    public static final String GeneralTag = "GEN";

    public static final HashSet<String> GenreTags = new HashSet<>(){{
        add(FictionTag);
        add(ScienceTag);
        add(SocialScienceTag);
        add(GeneralTag);
    }};

    public static boolean IsValidateGenre(String genre) {
        return GenreTags.contains(genre);
    }
}

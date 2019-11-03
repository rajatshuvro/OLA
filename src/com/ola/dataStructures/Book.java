package com.ola.dataStructures;

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

    public Book(long isbn, String author, String title, String publisher, int year, int pageCount,
                float price, String genre, int readingLevel, int copyNum){
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
    }

    public String GetId(){
        return String.join("-", Long.toString(Isbn), Genre, Integer.toString(ReadingLevel), '('+Integer.toString(CopyNum)+')');
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
}

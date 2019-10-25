package com.ola;
//Title    Author  ISBN    Copy_num        Page_count      Price   Publisher       Genre   Reading level

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

    public Book(String isbn, String author, String title, String publisher, int year, int copyNum, int pageCount, float price, Genre genre, int readingLevel){
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
        return String.join("-", Isbn, Genre.name(), Integer.toString(ReadingLevel), Integer.toString(CopyNum));
    }

    public enum Genre{
        FIC,
        SCI,
        SOS,
        REL
    }

    public enum ReadingLevel{
        Infant,
        Toddler,
        KinderGarden,
        Elementary,
        Middle,
        High
    }
}

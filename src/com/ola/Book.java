package com.ola;

public class Book {
    public final String Isbn;
    public final String Author;
    public final String Title;
    public final Integer Year;
    public final String Publisher;

    public Book(String isbn, String author, String title, String publisher, Integer year ){
        Isbn = isbn;
        Author = author;
        Title = title;
        Publisher = publisher;
        Year = year;
    }
}

package com.ola;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class BookDb {
    private InputStream _inputStream;
    private HashMap<String, Book> _books;

    public BookDb(InputStream inputStream) {
        _inputStream = inputStream;
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
                AddBook(line);
                count++;
            }
        }
        System.out.println("Loaded book database. Book count:"+ count);
        return count;
    }
//Title    Author  ISBN    Page count      Price   Publisher       Genre   Reading level
    private int TitleIndex = -1;
    private int AuthorIndex = -1;
    private int IsbnIndex = -1;
    private int PageCountIndex = -1;
    private int PriceIndex = -1;
    private int PublisherIndex = -1;
    private int GenreIndex = -1;
    private int ReadingLevelIndex = -1;

    public final String TitleTag = "Title";
    public final String AuthorTag = "Author";
    public final String IsbnTag = "ISBN";
    public final String PageCountTag = "Page count";
    public final String PriceTag = "Price";
    public final String PublisherTag = "Publisher";
    public final String GenreTag = "Genre";
    public final String ReadingLevelTag = "Reading level";

    private void SetColumnIndices(String headerLine){
        var splits = headerLine.split("\t");
        TitleIndex = Arrays.binarySearch(splits, TitleTag);
        AuthorIndex = Arrays.binarySearch(splits, AuthorTag);
        IsbnIndex = Arrays.binarySearch(splits, IsbnTag);
        PageCountIndex = Arrays.binarySearch(splits, PageCountTag);
        PriceIndex = Arrays.binarySearch(splits, PriceTag);
        PublisherIndex = Arrays.binarySearch(splits, PublisherTag);
        GenreIndex = Arrays.binarySearch(splits, GenreTag);
        ReadingLevelIndex = Arrays.binarySearch(splits, ReadingLevelTag);
    }

    private void AddBook(String line) {
        var splits = line.split("\t");

    }

}

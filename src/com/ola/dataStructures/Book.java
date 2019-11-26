package com.ola.dataStructures;

import com.ola.utilities.StringUtilities;

import java.util.Date;
import java.util.HashMap;
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
    private final Date EntryDate;
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
                !(author == null || author.equals("")) &&
                !(title == null || title.equals("")) &&
                !(publisher == null || publisher.equals("")) &&
                price > 0 &&
                year > 0 &&
                pageCount > 0 &&
                IsValidateGenre(genre) &&
                ValidateReadingLevel(readingLevel);
    }

    public static long GenerateIsbn(String title, String author, String publisher, int year, int pageCount) {
        // since the spelling of the title, author and publisher is subjective,
        // we use a more reliable and stable parameter - word counts of these strings
        var titleWordCount = StringUtilities.GetWordCount(title);
        var authorWordCount = StringUtilities.GetWordCount(author);
        var publisherWordCount = StringUtilities.GetWordCount(publisher);
        var isbnString = Integer.toString(titleWordCount) + authorWordCount + publisherWordCount
                        + year + pageCount;
        return Long.parseLong(isbnString);
    }

    public String GetUserFriendlyId(){
        return String.join("-", Long.toString(Isbn), GetAbbreviation(Genre), Integer.toString(ReadingLevel), '('+Integer.toString(CopyNum)+')');
    }

    public String GetId(){
        return GenerateId(Isbn, CopyNum);
    }

    public static String GenerateId(long isbn, int copyNum){
        return isbn +"-("+ copyNum +')';
    }

    private static final int MinReadingLevel =1;
    private static final int MaxReadingLevel = 10;
    private static boolean ValidateReadingLevel(int level){
        return level >= MinReadingLevel && level <= MaxReadingLevel;
    }
    //static fields
    // Tags in the files should be user friendly. they may be abbreviated in IDs.
    private static final String FictionTag = "Fiction";
    private static final String ScienceTag = "Science";
    private static final String SociologyTag = "Social";
    private static final String GeneralTag = "General";
    private static final String ReligiousTag = "Religion";
    private static final String HistoryTag = "History";
    private static final String GeographyTag = "Geography";
    private static final String CultureTag = "Culture";
    private static final String BiographyTag = "Biography";
    private static final String FairyTaleTag = "Fairy tale";
    private static final String FactualTag = "Factual";

    private static final HashSet<String> GenreTags = new HashSet<>(){{
        add(FictionTag);
        add(ScienceTag);
        add(SociologyTag);
        add(GeneralTag);
        add(ReligiousTag);
        add(HistoryTag);
        add(GeographyTag);
        add(CultureTag);
        add(BiographyTag);
        add(FairyTaleTag);
        add(FactualTag);

    }};

    private static final HashMap<String, String> GenreAbbreviations = new HashMap<>(){{
        put(FictionTag, "FIC");
        put(ScienceTag, "SCI");
        put(SociologyTag, "SOC");
        put(GeneralTag, "GEN");
        put(ReligiousTag, "REL");
        put(HistoryTag, "HIS");
        put(GeographyTag, "GEO");
        put(CultureTag, "CUL");
        put(BiographyTag, "BIO");
        put(FairyTaleTag, "FAI");
        put(FactualTag, "FAC");

    }};
    private static String GetAbbreviation(String genre){
        if(!GenreAbbreviations.containsKey(genre)) return null;
        return GenreAbbreviations.get(genre);
    }
    private static boolean IsValidateGenre(String genre) {
        return GenreTags.contains(genre);
    }
}

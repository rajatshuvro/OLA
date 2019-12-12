package com.ola.dataStructures;

import com.ola.utilities.StringUtilities;
import com.ola.utilities.TimeUtilities;

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
                IsValidGenreTag(genre) &&
                IsValidReadingLevel(readingLevel);
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
    public static boolean IsValidReadingLevel(int level){
        return level >= MinReadingLevel && level <= MaxReadingLevel;
    }

    @Override
    public String toString(){
        return "Title:\t\t\t"+Title+'\n'+
                "Author:\t\t\t"+Author+'\n'+
                "ISBN:\t\t\t"+ Isbn+'\n'+
                "Publisher:\t\t"+Publisher+'\n'+
                "Year:\t\t\t"+Year+'\n'+
                "Genre:\t\t\t"+Genre+'\n'+
                "Copy number:\t"+CopyNum+'\n'+
                "Page count:\t\t"+ PageCount+'\n'+
                "Price:\t\t\t"+Price+'\n'+
                "Reading level:\t"+ReadingLevel+'\n'+
                "Entry date:\t\t"+ TimeUtilities.ToString(EntryDate)+'\n'+
                "Expiry date:\t\t"+ TimeUtilities.ToString(ExpiryDate);

    }
    //static fields
    // Tags in the files should be user friendly. they may be abbreviated in IDs.
    public static final String FictionTag = "Fiction";
    public static final String ScienceTag = "Science";
    public static final String SociologyTag = "Social";
    public static final String GeneralTag = "General";
    public static final String ReligiousTag = "Religion";
    public static final String HistoryTag = "History";
    public static final String GeographyTag = "Geography";
    public static final String CultureTag = "Culture";
    public static final String BiographyTag = "Biography";
    public static final String FairyTaleTag = "Fairy tale";
    public static final String FactualTag = "Factual";

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

    public static boolean IsValidGenre(String genre){
        return GenreTags.contains(genre) || GenreAbbreviations.containsValue(genre);
    }
    private static String GetAbbreviation(String genres){
        var genreArray = genres.split("/");
        var abbreviation = new StringBuilder();
        boolean isFirst=true;
        for (String genre: genreArray){
            if(!isFirst) abbreviation.append("-");
            abbreviation.append(GenreAbbreviations.get(genre));
            isFirst = false;
            }
        return abbreviation.toString();
        }

    private static boolean IsValidGenreTag(String genres) {
        var genreArray = genres.split("/");
        for (String genre: genreArray) {
            if (!GenreTags.contains(genre)) {
                System.out.println("WARNING!! Invalid genre:" + genre);
                return false;
            }
        }
        return true;
    }
}

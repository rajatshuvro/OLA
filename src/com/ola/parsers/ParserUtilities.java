package com.ola.parsers;

import java.util.ArrayList;
import java.util.Scanner;

public class ParserUtilities {
    // if failed to parse, return -1,
    // java doesn't allow passing by ref
    public static long ParseULong(String s){
        long x= -1;
        try{
            x = Long.parseLong(s);
        }
        catch (Exception e){
            return x;
        }
        return x;
    }
    public static int ParseUInt(String s){
        int x= -1;
        try{
            x = Integer.parseInt(s);
        }
        catch (Exception e){
            return x;
        }
        return x;
    }

    public static float ParseUFloat(String s) {
        float x= (float)-1.0;
        try{
            x = Float.parseFloat(s);
        }
        catch (Exception e){
            return x;
        }
        return x;
    }

    public static boolean IsNullOrEmpty(String genre) {
        return genre == null || genre.equals("");
    }

    public static String GetBookFileHeader(){
        var sb = new StringBuilder();
        sb.append("#Onkur library books\n");
        sb.append("#Schema number: 1\n");
        sb.append("#Title = Book title. Value = <String>\n");
        sb.append("#Author = Author name. Value = <String>\n");
        sb.append("#ISBN = ISBN number (-1 if unknown). Value = <Integer>\n");
        sb.append("#Publisher = Publisher name. Value = <String>\n");
        sb.append("#Year = Year of publication. Value = <Integer>");
        sb.append("#Genre = Book genre. Value = General/Fiction/Science/Social/Religion/History/Geography/Culture/Biography/Fairy tale/Factual\n");
        sb.append("#Page count = Page count. Value = <Integer>\n");
        sb.append("#Price = Book price (to be charged if lost). Value = <Decimal>\n");
        sb.append("#Reading level = Estimated reading level. Value = [1,2,3...10]\n");

        return sb.toString();
    }

    public static String GetBookRecordDelimiter(){
        return "***************************************************************";
    }
}

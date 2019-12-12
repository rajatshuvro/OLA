package com.ola.parsers;

import java.util.ArrayList;
import java.util.Scanner;

public class ParserUtilities {
    public static String[] GetNextRecordLines(Scanner scanner, String endOfRecordPrefix){
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.startsWith("#") || line.isEmpty()) continue;
            if(line.startsWith(endOfRecordPrefix)) break;
            lines.add(line);
        }

        return lines.toArray(new String[lines.size()]);
    }
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
}

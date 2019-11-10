package com.ola.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class parserUtilities {
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
}

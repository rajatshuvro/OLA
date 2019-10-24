package com.ola;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class BookDb {
    private String _filePath;
    private HashMap<String, Book> _books;

    public BookDb(String filePath) {
        _filePath = filePath;
    }

    public int Load() throws IOException {
        Path path = Paths.get(_filePath);
        Integer count=0;
        boolean isFirstLine = true;
        try (Scanner scanner =  new Scanner(path)){
            while (scanner.hasNextLine()){
                //process each line in some way
                String line = scanner.nextLine();
                if(isFirstLine){
                    //skipping the header
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
//Title    Author  ISBN    Copy_num        Page_count      Price   Publisher       Genre   Reading level
    private final int TitleIndex = 0;
    private final int AuthorIndex = 1;
    private final int IsbnIndex = 2;
    private final int CopyNumIndex = 3;


    private void AddBook(String line) {
        var splits = line.split("\t");

    }

}

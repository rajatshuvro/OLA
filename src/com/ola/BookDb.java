package com.ola;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class BookDb {
    private String _filePath;
    public BookDb(String filePath) {
        _filePath = filePath;
    }

    public int Load() throws IOException {
        Path path = Paths.get(_filePath);
        Integer count=0;
        try (Scanner scanner =  new Scanner(path)){
            while (scanner.hasNextLine()){
                //process each line in some way
                String line = scanner.nextLine();
                count++;
            }
        }
        System.out.println("Loaded book database. Book count:"+ count);
        return count;
    }
}

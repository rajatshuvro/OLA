package com.ola.utilities;

import java.io.File;

public class FileUtilities {
    public static boolean Exists(String filePath){
        var file = new File(filePath);
        return file.exists();
    }
}

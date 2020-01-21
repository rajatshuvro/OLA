package com.ola.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FlatObjectParser {
    private InputStream _inputStream;
    private Scanner _scanner;
    private HashMap<String, String> _keyValues;

    public FlatObjectParser(InputStream inputStream, String[] keys){
        _inputStream = inputStream;
        _scanner = new Scanner(_inputStream);
        _keyValues = new HashMap<>(keys.length);
    }
    private void Clear(){
        for (var key: _keyValues.keySet()) {
            _keyValues.put(key, null);
        }
    }
    public HashMap<String, String> GetNextSetOfValues(){
        Clear();
        while (_scanner.hasNextLine()){
            String[] lines = GetNextRecordLines(_scanner);
            if(lines.length == 0) continue;
            for (var line : lines) {
                var keyValue = line.split(":",2);
                if(keyValue.length ==2)
                    _keyValues.put(keyValue[0].strip(), keyValue[1].strip());
            }
            for (var entry: _keyValues.entrySet()) {
                if(entry.getValue()!=null) return _keyValues;
            }
        }

        return null;
    }
    public static final String RecordSeparator = "***************************************************************";

    public static String[] GetNextRecordLines(Scanner scanner){
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.startsWith("#") || line.isEmpty()) continue;
            if(line.equals(RecordSeparator)) break;
            lines.add(line);
        }

        return lines.toArray(new String[lines.size()]);
    }

    public void close() throws IOException {

        _scanner.close();
    }
}

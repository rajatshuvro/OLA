package com.ola.parsers;

import com.ola.dataStructures.IdMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class IdMapParser {
    private InputStream _inputStream;

    private final String ShortIdTag = "Short Id";
    private final String LongIdTag = "Long Id";

    public IdMapParser(InputStream inputStream){
        _inputStream = inputStream;
    }

    public ArrayList<IdMap> GetIdMaps() throws IOException {
        ArrayList<IdMap> idMaps = new ArrayList<>();
        var fobParser = new FlatObjectParser(_inputStream, new String[]{
                ShortIdTag, LongIdTag
        });

        var nextSetOfValues =fobParser.GetNextRecord();
        while ( nextSetOfValues != null){
            var idMap = GetIdMap(nextSetOfValues);
            if (idMap != null)  idMaps.add(idMap);

            nextSetOfValues = fobParser.GetNextRecord();
        }
        fobParser.close();
        return idMaps;
    }

    private IdMap GetIdMap(HashMap<String, String> keyValues) {
        String shortId = null;
        String longId = null;
        for (var entry: keyValues.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            switch (key){
                case ShortIdTag:
                    shortId = value;
                    break;
                case LongIdTag:
                     longId = value;
                    break;
            }

        }

        return new IdMap(shortId, longId);
    }
}

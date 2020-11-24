package com.ola.databases;
import com.ola.dataStructures.IdMap;
import com.ola.parsers.FlatObjectParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Random;

public class IdDb {
    private HashMap<String, String> _shortToLong;
    private HashMap<String, String> _longToShort;
    private final BufferedWriter _appender;
    private OutputStream _outputStream;
    private static final String shortIdChars= "THEEQUUIICKBROOEEWNFOUXJUAAMPSOOVUEERTHELAAZEYDOOG1234567890";
    public static final int ShortIdLength = 5;

    public IdDb(Iterable<IdMap> idMaps, OutputStream outputStream){
        _outputStream = outputStream;
        _shortToLong = new HashMap<>();
        _longToShort = new HashMap<>();

        if(_outputStream != null) _appender = new BufferedWriter(new OutputStreamWriter(_outputStream));
        else _appender = null;

        if(idMaps==null) return;

        for (var idMap: idMaps) {
            _shortToLong.put(idMap.ShortId, idMap.LongId);
            _longToShort.put(idMap.LongId, idMap.ShortId);
        }
    }

    public String GetShortId(String longId){
        if(_longToShort.containsKey(longId)) return _longToShort.get(longId);
        return null;
    }

    public String GetLongId(String shortId){
        if(_shortToLong.containsKey(shortId)) return _shortToLong.get(shortId);
        return null;
    }

    public boolean TryAdd(String shortId, String longId){
        if(_longToShort.containsKey(longId) || _shortToLong.containsKey(shortId)) return false;
        _shortToLong.put(shortId, longId);
        _longToShort.put(longId, shortId);

        return _appender == null? true: Append(shortId, longId);
    }

    private boolean Append(String shortId, String longId) {
        var idMap = new IdMap(shortId, longId);
        try {
            _appender.write(idMap.toString()+'\n');
            _appender.write(FlatObjectParser.RecordSeparator+'\n');
            _appender.flush();
        } catch (IOException e) {
            System.out.println("Failed to append id map.\n"+ idMap.toString());
            return false;
        }

        return true;
    }


    public String GenerateShortId(){
        var shortId = "";
        do {
            shortId = GetRandomShortId();
        }while (_shortToLong.containsKey(shortId));
        return shortId;
    }
    public static String GetRandomShortId(){
        var id = new char[ShortIdLength];
        var random = new Random();
        for (var i=0 ; i < ShortIdLength; i++){
            var index = random.nextInt(shortIdChars.length());
            id[i] = shortIdChars.charAt(index);
        }
        return new String(id);
    }

    public static boolean IsValidShortId(String id){
        if(id.length() != ShortIdLength) return false;
        for (int i=0; i < id.length(); i++) {
            var c = id.charAt(i);
            if(!Character.isAlphabetic(c) && !Character.isDigit(c)) return false;
        }
        return true;
    }

    public void Close() throws IOException {
        if(_appender != null)_appender.close();
        if(_outputStream != null) _outputStream.close();
    }
}

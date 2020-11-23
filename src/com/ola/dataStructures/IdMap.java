package com.ola.dataStructures;

public class IdMap {
    public final String ShortId;
    public final String LongId;

    public IdMap(String id, String longId ){
        ShortId = id;
        LongId = longId;
    }
    @Override
    public String toString(){
        return "Short Id:       "+ShortId+'\n'+
               "Long Id:        "+LongId;
    }
}

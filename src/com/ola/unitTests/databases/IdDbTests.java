package com.ola.unitTests.databases;

import com.ola.dataStructures.IdMap;
import com.ola.databases.IdDb;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IdDbTests {


    private ArrayList<IdMap> GetIdMaps (){
        var idMaps = new ArrayList<IdMap>();
        idMaps.add(new IdMap("CAT12", "12475890876-(3)"));
        idMaps.add(new IdMap("DOG46", "24673562876-(2)"));

        return idMaps;
    }
    @Test
    public void GenerateRandomId(){
        var id = IdDb.GetRandomShortId();
        assertTrue(IdDb.IsValidShortId(id));
        assertEquals(IdDb.ShortIdLength, id.length());
    }
    @Test
    public void GenerateNewId(){
        var idDb = new IdDb(GetIdMaps(), null);

        var id = idDb.GenerateShortId();
        assertTrue(IdDb.IsValidShortId(id));
        assertNull(idDb.GetLongId(id));
    }

    @Test
    public void TryAddId(){
        var idDb = new IdDb(GetIdMaps(), null);

        var shortId = idDb.GenerateShortId();
        var longId = "this-is-long";
        assertTrue(idDb.TryAdd(shortId,longId));
        assertEquals(longId, idDb.GetLongId(shortId));
        assertEquals(shortId, idDb.GetShortId(longId));
    }
}

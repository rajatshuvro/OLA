package com.ola.unitTests.parsers;

import com.ola.databases.IdDb;
import com.ola.parsers.IdMapParser;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IdMapParserTests {
    @Test
    public void ReadIdMaps() throws IOException {
        var parser = new IdMapParser(TestStreams.GetIdMapStream());
        var idDb = new IdDb(parser.GetIdMaps(), null);

        assertEquals("AC564",idDb.GetShortId("7890788-(2)"));
        assertEquals("7890788-(2)",idDb.GetLongId("AC564"));
    }

    @Test
    public void AppendIdMaps() throws IOException {
        var parser = new IdMapParser(TestStreams.GetIdMapStream());
        var memStream = new ByteArrayOutputStream();
        var idDb = new IdDb(parser.GetIdMaps(), memStream);

        assertFalse(idDb.TryAdd("AC564", "7890788-(2)"));
        assertTrue(idDb.TryAdd("CAC69", "baba-black-sheep"));

        var buffer = memStream.toByteArray();
        memStream.close();
        var appendedStream = new ByteArrayInputStream(buffer);
        var newParser = new IdMapParser(appendedStream);
        var newRecords = newParser.GetIdMaps();
        assertEquals(1, newRecords.size());
    }
}

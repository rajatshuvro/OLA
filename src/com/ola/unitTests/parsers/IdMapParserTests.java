package com.ola.unitTests.parsers;

import com.ola.databases.IdDb;
import com.ola.parsers.IdMapParser;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class IdMapParserTests {
    @Test
    public void ReadIdMaps() throws IOException {
        var parser = new IdMapParser(TestStreams.GetIdMapStream());
        var idDb = new IdDb(parser.GetIdMaps());

        assertEquals("AC564",idDb.GetShortId("7890788-(2)"));
        assertEquals("7890788-(2)",idDb.GetLongId("AC564"));
    }
}

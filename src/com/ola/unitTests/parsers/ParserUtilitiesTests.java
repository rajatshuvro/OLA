package com.ola.unitTests.parsers;

import com.ola.parsers.ParserUtilities;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserUtilitiesTests {
    @Test
    public void GetProperCasing(){
        var s1= "LORRENEI LI";
        var s2 = "LORRenei lI";

        var proper= "Lorrenei Li";
        assertEquals(proper, ParserUtilities.ToProperCase(s1));
        assertEquals(proper, ParserUtilities.ToProperCase(s2));
    }
}

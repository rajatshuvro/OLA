package com.ola.unitTests;

import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeUtilitiesTests {
    @Test
    public void ParseGoogleFormTimeStamp(){
        var googleTime = TimeUtilities.parseGoogleDateTime("2020/09/22 8:05:04 AM MDT");
        var time = TimeUtilities.parseDateTime("2020-09-22 8:05:04");
        assertEquals(time, googleTime);
    }
}

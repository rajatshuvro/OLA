package com.ola.unitTests.parsers;

import com.ola.parsers.UserCsvParser;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class UserCsvParserTests {

    @Test
    public void GetNewUsers() throws IOException {
        var parser = new UserCsvParser(TestStreams.GetNewUserCsvStream());

        var user = parser.GetUsers();
        assertEquals(3, user.size());
        assertEquals("Tanni Chakraborty", user.get(0).Name);
    }
}

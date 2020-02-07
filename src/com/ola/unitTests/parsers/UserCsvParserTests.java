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

        var users = parser.GetUsers();
        assertEquals(3, users.size());
        assertEquals("Tanni Chakraborty", users.get(0).Name);
    }
}

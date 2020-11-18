package com.ola.unitTests.parsers;

import com.ola.parsers.CheckoutCsvParser;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckoutCsvParserTests {
    @Test
    public void GetCheckouts() throws IOException {
        var parser = new CheckoutCsvParser(TestStreams.GetCheckoutCsvStream());

        var checkouts = parser.GetCheckouts();
        assertEquals(4, checkouts.size());
        assertEquals("9848494226-(1)", checkouts.get(1).BookId);
        assertEquals(474, checkouts.get(1).UserId);
    }
}

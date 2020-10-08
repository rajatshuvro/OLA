package com.ola.unitTests.databases;

import com.ola.databases.DbUtilities;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class DbUtilitiesTests {
    @Test
    public void ReadCheckout_empty_Test() throws IOException {
        var checkoutStream = TestStreams.GetEmptyCheckoutStream();

        var checkouts = DbUtilities.ReadCheckouts(checkoutStream);
        assertEquals(0, checkouts.size());
    }

    @Test
    public void ReadCheckout_Test() throws IOException {
        var checkoutStream = TestStreams.GetCheckoutStream();

        var checkouts = DbUtilities.ReadCheckouts(checkoutStream);
        assertEquals(2, checkouts.size());
    }
}

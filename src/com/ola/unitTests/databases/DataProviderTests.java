package com.ola.unitTests.databases;

import com.ola.DataProvider;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataProviderTests {

    public static OutputStream GetAppendStream() {
        return new ByteArrayOutputStream();
    }
    @Test
    public void Status_by_book() throws IOException {
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());

        var transaction = provider.TransactionDb.GetLatest("7890788-(2)");
        assertNotNull(transaction);
        assertTrue(provider.GetTransactionString(transaction).contains("7890788-(2)"));
    }
    @Test
    public void Status_by_user() throws IOException {
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());
        var transactions = provider.GetPendingCheckouts( 897);
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertTrue(provider.GetTransactionString(transactions.get(0)).contains("Zohir Chowdhury"));
    }


}

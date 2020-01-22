package com.ola.unitTests.databases;

import com.ola.CheckOut;
import com.ola.DataProvider;
import com.ola.Return;
import com.ola.dataStructures.Transaction;
import com.ola.parsers.ParserUtilities;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataProviderTests {
    @Test
    public void DataProvider_basic() throws IOException {
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream());
        provider.Load();
        var checkoutArgs = new String[]{"co", "-b", "7890788-(2)","-u","564"};
        CheckOut.Run(checkoutArgs, provider.TransactionsDb);

        assertEquals(Transaction.CheckoutTag, provider.TransactionsDb.GetBookStatus("7890788-(2)"));

        var returnArgs = new String[]{"ret", "-b", "456098-(1)"};
        Return.Run(returnArgs, provider.TransactionsDb);
        assertEquals(Transaction.ReturnTag, provider.TransactionsDb.GetBookStatus("456098-(1)"));

    }

    public static OutputStream GetAppendStream() {
        var memStream = new ByteArrayOutputStream();
        return memStream;
    }

}

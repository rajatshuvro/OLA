package com.ola.unitTests.subCommands;

import com.ola.DataProvider;
import com.ola.CheckoutStatus;
import com.ola.unitTests.TestStreams;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.ola.unitTests.databases.DataProviderTests.*;
import static org.junit.jupiter.api.Assertions.*;

public class CheckoutStatusTests {

    @Test
    public void Status_command_line() throws IOException {
        var args = new String[]{"status","-u","897"};
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());
        provider.Load();
        //just run it to make sure it doesn't crash
        CheckoutStatus.Run(args, provider);
    }

    @Test
    public void Status_by_user() throws IOException {
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());
        provider.Load();
        var retString = CheckoutStatus.GetUserCheckouts(provider, 897);
        assertTrue(retString.contains("Checkout status for: Zohir Chowdhury"));
    }

    @Test
    public void Status_by_book() throws IOException {
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());
        provider.Load();
        var retString = CheckoutStatus.GetLatestBookTransaction(provider, "7890788-(2)");
        assertNotNull(retString);
        assertTrue(retString.contains("Transaction status for book: 7890788-(2)"));
    }
    @Test
    public void Status_all() throws IOException {
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());
        provider.Load();

        var retString = CheckoutStatus.GetAllCheckouts(provider);
        assertTrue(retString.contains("id:456098-(1)"));
        assertTrue(retString.contains("2019-10-15 11:01:22"));
    }
}

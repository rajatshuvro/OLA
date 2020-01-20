package com.ola.unitTests;

import com.ola.DataProvider;
import com.ola.Status;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.ola.unitTests.DataProviderTests.*;
import static org.junit.jupiter.api.Assertions.*;

public class StatusTests {

    @Test
    public void Status_command_line() throws IOException {
        var args = new String[]{"status","-u","897"};
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream());
        provider.Load();
        //just run it to make sure it doesn't crash
        Status.Run(args, provider);
    }

    @Test
    public void Status_by_user() throws IOException {
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream());
        provider.Load();
        var retString = Status.GetUserCheckouts(provider, 897);
        assertTrue(retString.contains("Checkout status for: Zohir Chowdhury"));
    }

    @Test
    public void Status_all() throws IOException {
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream());
        provider.Load();

        var retString = Status.GetAllCheckouts(provider);
        assertTrue(retString.contains("id:456098-(1)"));
        assertTrue(retString.contains("2019-10-15 11:01:22"));
    }
}

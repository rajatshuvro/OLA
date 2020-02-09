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
    public void Status_user() throws IOException {
        var args = new String[]{"status","-u","897"};
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());
        provider.Load();
        //just run it to make sure it doesn't crash
        assertEquals(0, CheckoutStatus.Run(args, provider));
    }

    @Test
    public void Status_book() throws IOException {
        var args = new String[]{"status","-b","7890788-(2)"};
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());
        provider.Load();
        //just run it to make sure it doesn't crash
        assertEquals(0, CheckoutStatus.Run(args, provider));

    }

    @Test
    public void Status_isbn() throws IOException {
        var args = new String[]{"status","-b","7890788"};
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());
        provider.Load();
        //just run it to make sure it doesn't crash
        assertEquals(0, CheckoutStatus.Run(args, provider));
    }

    @Test
    public void Status_all() throws IOException {
        var args = new String[]{"status"};
        var provider = new DataProvider(TestStreams.GetBooksStream(), TestStreams.GetUsersStream(), TestStreams.GetTransactionsStream(),
                GetAppendStream(), GetAppendStream(), GetAppendStream());
        provider.Load();
        //just run it to make sure it doesn't crash
        assertEquals(0, CheckoutStatus.Run(args, provider));
    }

}

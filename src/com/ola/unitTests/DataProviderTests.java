package com.ola.unitTests;

import com.ola.CheckOut;
import com.ola.DataProvider;
import com.ola.Return;
import com.ola.dataStructures.Transaction;
import org.junit.jupiter.api.Test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataProviderTests {
    public static InputStream GetTransactionsStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("#Onkur library transactions\n");
        writer.write("***************************\n");
        writer.write("Book Id:\t7890788-(2)\n");
        writer.write("User Id:\t564\n");
        writer.write("Date:\t\t2019-09-13 10:30:31\n");
        writer.write("Type:\t\tCheckout\n");
        writer.write("************************************\n");
        writer.write("Book Id:\t678564-(1)\n");
        writer.write("User Id:\t897\n");
        writer.write("Date:\t\t2019-10-15 11:01:22\n");
        writer.write("Type:\t\tCheckout\n");
        writer.write("************************************\n");
        writer.write("Book Id:\t456098-(1)\n");
        writer.write("User Id:\t897\n");
        writer.write("Date:\t\t2019-11-03 10:33:10\n");
        writer.write("Type:\t\tCheckout\n");
        writer.write("************************************\n");
        writer.write("Book Id:\t7890788-(2)\n");
        writer.write("User Id:\t564\n");
        writer.write("Date:\t\t2019-11-13 10:30:25\n");
        writer.write("Type:\t\tReturn\n");
        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }
    public static InputStream GetBooksStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("#Onkur library books\n");
        writer.write("***************************************************************\n");
        writer.write("Title:\t\t\tAmar Baba\n");
        writer.write("Author:\t\t\tRajat Shuvro Roy\n");
        writer.write("ISBN:\t\t\t7890788\n");
        writer.write("Publisher:\t\tBonosree books and co\n");
        writer.write("Year:\t\t\t2015\n");
        writer.write("Genre:\t\t\tGEN\n");
        writer.write("Copy number:\t1\n");
        writer.write("Page count:\t\t7\n");
        writer.write("Price:\t\t\t5\n");
        writer.write("Reading level:\t4\n");
        writer.write("Entry date:\t\t2018-05-17 11:35:06\n");
        writer.write("Expiry date:\t2018-11-10 11:35:34\n");
        writer.write("***************************************************************\n");
        writer.write("Title:\t\t\tAmar Baba\n");
        writer.write("Author:\t\t\tRajat Shuvro Roy\n");
        writer.write("ISBN:\t\t\t7890788\n");
        writer.write("Publisher:\t\tBonosree books and co\n");
        writer.write("Year:\t\t\t2015\n");
        writer.write("Genre:\t\t\tGEN\n");
        writer.write("Copy number:\t2\n");
        writer.write("Page count:\t\t7\n");
        writer.write("Price:\t\t\t5\n");
        writer.write("Reading level:\t4\n");
        writer.write("Entry date:\t\t2018-05-17 11:35:06\n");
        writer.write("Expiry date:\t\n");
        writer.write("***************************************************************\n");
        writer.write("Title:\t\t\tBhua Bhalobasha\n");
        writer.write("Author:\t\t\tSaber Nabil\n");
        writer.write("ISBN:\t\t\t678564\n");
        writer.write("Publisher:\t\tDakkhin Khan Publishers\n");
        writer.write("Year:\t\t\t2017\n");
        writer.write("Genre:\t\t\tFIC\n");
        writer.write("Copy number:\t1\n");
        writer.write("Page count:\t\t10\n");
        writer.write("Price:\t\t\t10\n");
        writer.write("Reading level:\t5\n");
        writer.write("Entry date:\t\t2018-11-27 11:49:55\n");
        writer.write("Expiry date:\t\n");
        writer.write("***************************************************************\n");
        writer.write("Title:\t\t\tRobindra Prem\n");
        writer.write("Author:\t\t\tNandana Mitra\n");
        writer.write("ISBN:\t\t\t456098\n");
        writer.write("Publisher:\t\tBakura Publishers\n");
        writer.write("Year:\t\t\t2018\n");
        writer.write("Genre:\t\t\tFIC\n");
        writer.write("Copy number:\t1\n");
        writer.write("Page count:\t\t15\n");
        writer.write("Price:\t\t\t10\n");
        writer.write("Reading level:\t6\n");
        writer.write("Entry date:\t\t2019-11-27 11:28:44\n");
        writer.write("Expiry date:\t\n");
        writer.write("***************************************************************\n");

        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }
    public static InputStream GetUsersStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("#Onkur library users\n");
        writer.write("***************************\n");
        writer.write("Name:\tSaber Nabil\n");
        writer.write("Id:\t\t234\n");
        writer.write("Role:\tTeacher\n");
        writer.write("***************************\n");
        writer.write("Name:\tRajat Shuvro Roy\n");
        writer.write("Id:\t\t678\n");
        writer.write("Role:\tVolunteer\n");
        writer.write("***************************\n");
        writer.write("Name:\tZohir Chowdhury\n");
        writer.write("Id:\t\t897\n");
        writer.write("Role:\tAdministrator\n");
        writer.write("***************************\n");
        writer.write("Name:\tTotini Tonu\n");
        writer.write("Id:\t\t564\n");
        writer.write("Role:\tStudent\n");
        writer.write("***************************\n");
        writer.write("Name:\tIshal Khan\n");
        writer.write("Id:\t\t157\n");
        writer.write("Role:\tStudent\n");
        writer.write("***************************\n");
        writer.write("Name:\tAyrah Khan\n");
        writer.write("Id:\t\t167\n");
        writer.write("Role:\tStudent\n");
        writer.write("***************************\n");
        writer.write("Name:\tNoureen Chowdhury\n");
        writer.write("Id:\t\t169\n");
        writer.write("Role:\tStudent\n");
        writer.write("***************************\n");
        writer.write("Name:\tDarayush Ahmed\n");
        writer.write("Id:\t\t456\n");
        writer.write("Role:\tStudent\n");

        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }

    @Test
    public void DataProvider_basic() throws IOException {
        var provider = new DataProvider(GetBooksStream(), GetUsersStream(), GetTransactionsStream(),
                GetAppendStream(), GetAppendStream());
        provider.Load();
        var checkoutArgs = new String[]{"co", "-b", "7890788-(2)","-u","564"};
        CheckOut.Run(checkoutArgs, provider.TransactionsDb);

        assertEquals(Transaction.CheckoutTag, provider.TransactionsDb.GetBookStatus("7890788-(2)"));

        var returnArgs = new String[]{"ret", "-b", "456098-(1)","-u","897"};
        Return.Run(returnArgs, provider.TransactionsDb);
        assertEquals(Transaction.ReturnTag, provider.TransactionsDb.GetBookStatus("456098-(1)"));

    }

    private OutputStream GetAppendStream() {
        var memStream = new ByteArrayOutputStream();
        return memStream;
    }

}

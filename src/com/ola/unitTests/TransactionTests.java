package com.ola.unitTests;

import com.ola.parsers.TransactionParser;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TransactionTests {
    public static InputStream GetStream() throws IOException {
        var memStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(memStream);

        writer.write("#Onkur library transactions\n");
        writer.write("***************************\n");
        writer.write("Book Id:\t7890788-GEN-4-(2)\n");
        writer.write("User Id:\t234\n");
        writer.write("Date:\t\t2019-09-13 10:30:31\n");
        writer.write("Type:\t\tCheckout\n");
        writer.write("************************************\n");
        writer.write("Book Id:\t678564-FIC-5-(1)\n");
        writer.write("User Id:\t123\n");
        writer.write("Date:\t\t2019-10-15 11:01:22\n");
        writer.write("Type:\t\tCheckout\n");
        writer.write("************************************\n");
        writer.write("Book Id:\t456098-FIC-6-(1)\n");
        writer.write("User Id:\t345\n");
        writer.write("Date:\t\t2019-11-03 10:33:10\n");
        writer.write("Type:\t\tCheckout\n");
        writer.write("************************************\n");
        writer.write("Book Id:\t7890788-GEN-4-(2)\n");
        writer.write("User Id:\t234\n");
        writer.write("Date:\t\t2019-11-13 10:30:25\n");
        writer.write("Type:\t\tReturn\n");
        writer.close();

        var buffer = memStream.toByteArray();
        memStream.close();
        return new ByteArrayInputStream(buffer);
    }

    @Test
    public void ParseTransactionsTest() throws IOException {
        var parser = new TransactionParser(GetStream());
        var transactions = parser.GetTransactions();

        assertEquals(4, transactions.size());

        assertEquals("7890788-GEN-4-(2)", transactions.get(0).BookId);
        assertEquals("Checkout", transactions.get(0).Type);
    }
}

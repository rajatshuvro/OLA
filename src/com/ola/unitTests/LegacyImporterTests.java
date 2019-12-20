package com.ola.unitTests;
import com.ola.LegacyImporter;
import com.ola.dataStructures.Book;
import com.ola.parsers.LegacyBookParser;
import com.ola.parsers.ParserUtilities;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyImporterTests {
    public ArrayList<Book> GetLegacyBooks() throws IOException {
        var bookParser = new LegacyBookParser(LegacyParserTests.GetBooksStream());
        return bookParser.GetBooks();
    }

    @Test
    public void WriteLegacyBooks() throws IOException {
        var outputStream = new ByteArrayOutputStream();
        var writer = new OutputStreamWriter(outputStream);

        LegacyImporter.ConvertLegacyRecords(GetLegacyBooks(),writer);

        var bytes = outputStream.toByteArray();
        var inputStream = new ByteArrayInputStream(bytes);
        var scanner =  new Scanner(inputStream);

        var recordCount = 0;
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if (line.equals(ParserUtilities.GetBookRecordDelimiter())) recordCount++;
        }

        assertEquals(20,recordCount);
    }
}
